package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.auth.JwtProvider;
import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Follow;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.model.Role;
import com.tenten.linkhub.domain.member.repository.MemberEmailRedisRepository;
import com.tenten.linkhub.domain.member.repository.follow.FollowRepository;
import com.tenten.linkhub.domain.member.repository.member.MemberRepository;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberAuthInfo;
import com.tenten.linkhub.domain.member.service.dto.MemberFindResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowCreateResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowersFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowingsFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberMyProfileResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberProfileResponse;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import com.tenten.linkhub.global.exception.DataDuplicateException;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import com.tenten.linkhub.global.infrastructure.ses.AwsSesService;
import com.tenten.linkhub.global.response.ErrorCode;
import com.tenten.linkhub.global.util.email.EmailDto;
import com.tenten.linkhub.global.util.email.VerificationCodeCreator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private static final String MEMBER_IMAGE_FOLDER = "member-image/";
    private static final String MEMBER_DEFAULT_IMAGE_PATH = "https://team-10-bucket.s3.ap-northeast-2.amazonaws.com/member-image/member-default.png";

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final AwsSesService emailService;
    private final VerificationCodeCreator verificationCodeCreator;
    private final MemberEmailRedisRepository memberEmailRedisRepository;
    private final S3Uploader s3Uploader;
    private final JwtProvider jwtProvider;

    public MemberServiceImpl(
            MemberRepository memberRepository,
            FollowRepository followRepository, AwsSesService emailService,
            VerificationCodeCreator verificationCodeCreator,
            MemberEmailRedisRepository memberEmailRedisRepository,
            S3Uploader s3Uploader,
            JwtProvider jwtProvider
    ) {
        this.followRepository = followRepository;
        this.emailService = emailService;
        this.verificationCodeCreator = verificationCodeCreator;
        this.memberEmailRedisRepository = memberEmailRedisRepository;
        this.memberRepository = memberRepository;
        this.s3Uploader = s3Uploader;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    @Override
    public void sendVerificationEmail(EmailDto emailDto) {
        if (memberRepository.existsMemberByNewsEmail(emailDto.getTo())) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_NEWS_EMAIL);
        }

        final String authKey = verificationCodeCreator.createVerificationCode();
        emailService.sendVerificationCodeEmail(emailDto, authKey);
        memberEmailRedisRepository.saveExpire(authKey, emailDto.getTo(), 60 * 3L);
    }

    @Override
    public MailVerificationResponse verificateEmail(MailVerificationRequest request) {
        String emailFoundByCode = memberEmailRedisRepository.getEmail(request.code());
        if (emailFoundByCode == null || !emailFoundByCode.equals(request.email())) {
            return new MailVerificationResponse(false);
        }

        return new MailVerificationResponse(true);
    }

    @Override
    public MemberInfos findMemberInfosByMemberIds(List<Long> memberIds) {
        List<Member> members = memberRepository.findMemberWithProfileImageByMemberIds(memberIds);

        return MemberInfos.from(members);
    }

    @Transactional
    @Override
    public MemberFindResponse findMember(String socialId, Provider provider) {
        Optional<Member> member = memberRepository.findBySocialIdAndProvider(socialId, provider);

        return MemberFindResponse.from(member);
    }

    @Transactional
    @Override
    public MemberJoinResponse join(MemberJoinRequest memberJoinRequest) {
        memberRepository.findBySocialIdAndProvider(memberJoinRequest.socialId(), memberJoinRequest.provider())
                .ifPresent(m -> {
                    throw new UnauthorizedAccessException("이미 가입한 회원입니다.");
                });

        ImageInfo imageInfo = getNewImageInfoOrDefaultImageInfo(memberJoinRequest.file());

        Member member = new Member(
                memberJoinRequest.socialId(),
                memberJoinRequest.provider(),
                Role.USER,
                memberJoinRequest.nickname(),
                memberJoinRequest.aboutMe(),
                memberJoinRequest.newsEmail(),
                memberJoinRequest.isSubscribed(),
                new ProfileImage(imageInfo.path(), imageInfo.name()),
                new FavoriteCategory(memberJoinRequest.favoriteCategory())
        );

        Member savedMember = memberRepository.save(member);

        String jwt = jwtProvider.generateTokenFromMember(MemberAuthInfo.from(savedMember));

        return MemberJoinResponse.from(jwt);
    }


    private ImageInfo getNewImageInfoOrDefaultImageInfo(MultipartFile file) {
        if (file == null) {
            return ImageInfo.of(MEMBER_DEFAULT_IMAGE_PATH, "default-image");
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, MEMBER_IMAGE_FOLDER);
        return s3Uploader.saveImage(imageSaveRequest);
    }

    @Override
    public MemberProfileResponse getProfile(Long memberId, Long myMemberId) {
        Member member = memberRepository.findByIdWithImageAndCategory(memberId)
                .orElseThrow(() -> new UnauthorizedAccessException("존재하지 않는 회원입니다."));

        Long followerCount = followRepository.countFollowers(memberId);
        Long followingCount = followRepository.countFollowing(memberId);

        Boolean isFollowing = followRepository.existsByMemberIdAndMyMemberId(memberId, myMemberId);

        return MemberProfileResponse.from(member, followerCount, followingCount, isFollowing);
    }

    @Override
    public MemberMyProfileResponse getMyProfile(Long memberId) {
        Member member = memberRepository.findByIdWithImageAndCategory(memberId)
                .orElseThrow(() -> new UnauthorizedAccessException("존재하지 않는 회원입니다."));

        Long followerCount = followRepository.countFollowers(memberId);
        Long followingCount = followRepository.countFollowing(memberId);

        return MemberMyProfileResponse.from(member, followerCount, followingCount);
    }

    @Transactional
    @Override
    public MemberFollowCreateResponse createFollow(Long memberId, Long myMemberId) {
        if (followRepository.existsByMemberIdAndMyMemberId(memberId, myMemberId)) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_FOLLOWING);
        }

        Member followingMember = memberRepository.getById(myMemberId);
        Member followedMember = memberRepository.getById(memberId);

        Follow follow = followRepository.save(new Follow(followingMember, followedMember));

        return MemberFollowCreateResponse.from(follow.getFollower());
    }

    @Transactional
    @Override
    public Long deleteFollow(Long memberId, Long myMemberId) {
        Follow follow = followRepository.findByMemberIdAndMyMemberId(memberId, myMemberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 팔로우 또는 유저입니다."));

        followRepository.delete(follow);

        return memberId;
    }

    @Override
    public MemberFollowingsFindResponses getFollowings(Long memberId, Long myMemberId, PageRequest pageRequest) {

        // 팔로잉 아이디를 고정하고 다수의 팔로워 아이디를 가지는 팔로우 리스트
        Slice<Follow> follows = followRepository.findByFollowingId(memberId, pageRequest);

        // 소프트 딜리트 되지 않은 팔로워 아이디 리스트를 Long 타입으로 건네 받음
        List<Long> followedMemberIds = follows.getContent().stream()
                .filter(follow -> !follow.getFollower().getIsDeleted())
                .map(follow -> follow.getFollower().getId())
                .collect(Collectors.toList());

        // 내 멤버 아이디가 팔로우하는 다수의 팔로워 아이디 리스트
        Set<Long> followedByMyMember = followRepository.findFollowedMemberIdsByFollowingId(myMemberId, followedMemberIds);

        // 순차적으로 팔로워 아이디들을 팔로우하는지 판단
        List<Boolean> isMyMemberFollowingList = followedMemberIds.stream()
                .map(followedByMyMember::contains)
                .toList();

        // 유저가 팔로우 중인 소프트 딜리트 되지 않은 멤버 리스트 프로필 이미지와 카테고리와 함께 건네 받음
        List<Member> followings = memberRepository.findMembersWithProfileImageAndCategoryByIds(followedMemberIds);

        Slice<Member> followingsSlice = new SliceImpl<>(followings, follows.getPageable(), follows.hasNext());

        return MemberFollowingsFindResponses.from(followingsSlice, isMyMemberFollowingList);
    }

    @Override
    public MemberFollowersFindResponses getFollowers(Long memberId, Long myMemberId, PageRequest pageRequest) {

        Slice<Follow> follows = followRepository.findByFollowerId(memberId, pageRequest);

        List<Long> followingMemberIds = follows.getContent().stream()
                .filter(follow -> !follow.getFollowing().getIsDeleted())
                .map(follow -> follow.getFollowing().getId())
                .collect(Collectors.toList());

        Set<Long> followedByMyMember = followRepository.findFollowedMemberIdsByFollowingId(myMemberId, followingMemberIds);

        List<Boolean> isMyMemberFollowingList = followingMemberIds.stream()
                .map(followedByMyMember::contains)
                .toList();

        List<Member> followers = memberRepository.findMembersWithProfileImageAndCategoryByIds(followingMemberIds);

        Slice<Member> followersSlice = new SliceImpl<>(followers, follows.getPageable(), follows.hasNext());

        return MemberFollowersFindResponses.from(followersSlice, isMyMemberFollowingList);
    }

}
