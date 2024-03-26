package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.auth.JwtProvider;
import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Follow;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.model.Role;
import com.tenten.linkhub.domain.member.repository.MemberEmailRedisRepository;
import com.tenten.linkhub.domain.member.repository.dto.FollowDTO;
import com.tenten.linkhub.domain.member.repository.dto.MemberWithProfileImageAndFollowingStatus;
import com.tenten.linkhub.domain.member.repository.follow.FollowRepository;
import com.tenten.linkhub.domain.member.repository.member.MemberRepository;
import com.tenten.linkhub.domain.member.service.dto.MailSendResponse;
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
import com.tenten.linkhub.domain.member.service.dto.MemberSearchRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberSearchResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberUpdateRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberUpdateResponse;
import com.tenten.linkhub.domain.member.service.mapper.MemberMapper;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.ImageFileUploader;
import com.tenten.linkhub.global.exception.DataDuplicateException;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import com.tenten.linkhub.global.infrastructure.ses.AwsSesService;
import com.tenten.linkhub.global.response.ErrorCode;
import com.tenten.linkhub.global.util.email.EmailDto;
import com.tenten.linkhub.global.util.email.VerificationCodeCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private static final String MEMBER_IMAGE_FOLDER = "member-image/";
    private static final String MEMBER_DEFAULT_IMAGE_PATH = "https://linkhub-s3.s3.ap-northeast-2.amazonaws.com/member-image/member-default-v1.png";
    private static final Long CODE_VALID_DURATION = 60 * 5L;

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final AwsSesService emailService;
    private final VerificationCodeCreator verificationCodeCreator;
    private final MemberEmailRedisRepository memberEmailRedisRepository;
    private final ImageFileUploader imageFileUploader;
    private final JwtProvider jwtProvider;
    private final MemberMapper mapper;

    @Transactional
    @Override
    public MailSendResponse sendVerificationEmail(EmailDto emailDto) {
        if (memberRepository.existsMemberByNewsEmail(emailDto.getTo())) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_NEWS_EMAIL);
        }

        final String authKey = verificationCodeCreator.createVerificationCode();
        emailService.sendVerificationCodeEmail(emailDto, authKey);
        memberEmailRedisRepository.saveExpire(authKey, emailDto.getTo(), CODE_VALID_DURATION);

        return new MailSendResponse(CODE_VALID_DURATION);
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
                    throw new DataDuplicateException(ErrorCode.DUPLICATE_SOCIAL_ID);
                });

        if (memberRepository.existsMemberByNewsEmail(memberJoinRequest.newsEmail())) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_NEWS_EMAIL);
        }

        if (memberRepository.existsMemberByNickname(memberJoinRequest.nickname())) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_NICKNAME);
        }

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

    public Optional<ImageInfo> getNewImageInfoOrEmptyImageInfo(MultipartFile file) {
        if (file == null) {
            return Optional.empty();
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, MEMBER_IMAGE_FOLDER);
        return Optional.ofNullable(imageFileUploader.saveImage(imageSaveRequest));
    }

    private ImageInfo getNewImageInfoOrDefaultImageInfo(MultipartFile file) {
        if (file == null) {
            return ImageInfo.of(MEMBER_DEFAULT_IMAGE_PATH, "default-image");
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, MEMBER_IMAGE_FOLDER);
        return imageFileUploader.saveImage(imageSaveRequest);
    }

    @Override
    public MemberProfileResponse getProfile(Long memberId, Long myMemberId) {
        Member member = memberRepository.findByIdWithImageAndCategory(memberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 회원입니다."));

        Long followerCount = followRepository.countFollowers(memberId);
        Long followingCount = followRepository.countFollowing(memberId);

        Boolean isModifiable = Objects.equals(memberId, myMemberId);
        Boolean isFollowing = followRepository.existsByMemberIdAndMyMemberId(memberId, myMemberId);

        return MemberProfileResponse.from(member, followerCount, followingCount, isModifiable, isFollowing);
    }

    @Override
    public MemberMyProfileResponse getMyProfile(Long memberId) {
        Member member = memberRepository.findByIdWithImageAndCategory(memberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 회원입니다."));

        Long followerCount = followRepository.countFollowers(memberId);
        Long followingCount = followRepository.countFollowing(memberId);

        return MemberMyProfileResponse.from(member, followerCount, followingCount);
    }

    @Transactional
    @Override
    public MemberFollowCreateResponse createFollow(Long memberId, Long myMemberId) {
        if (Boolean.TRUE.equals(followRepository.existsByMemberIdAndMyMemberId(memberId, myMemberId))) {
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
        Slice<FollowDTO> followDTOs = followRepository.findFollowingsOfTargetUserWithMyMemberFollowingStatus(memberId, myMemberId, pageRequest);

        return MemberFollowingsFindResponses.from(followDTOs);
    }

    @Override
    public MemberFollowersFindResponses getFollowers(Long memberId, Long myMemberId, PageRequest pageRequest) {
        Slice<FollowDTO> followDTOs = followRepository.findFollowersOfTargetUserWithMyMemberFollowingStatus(memberId,
                myMemberId, pageRequest);

        return MemberFollowersFindResponses.from(followDTOs);
    }

    @Transactional
    @Override
    public MemberUpdateResponse updateProfile(MemberUpdateRequest request) {
        if (!Objects.equals(request.targetMemberId(), request.requestMemberId())) {
            throw new UnauthorizedAccessException("권한이 없는 멤버입니다.");
        }

        Optional<ImageInfo> imageInfo = getNewImageInfoOrEmptyImageInfo(request.file());
        Optional<ProfileImage> profileImage = imageInfo.map(i -> new ProfileImage(i.path(), i.name()));
        FavoriteCategory favoriteCategory = new FavoriteCategory(request.favoriteCategory());

        Member member = memberRepository.getById(request.targetMemberId());

        Member updatedMember = member.update(
                request.nickname(),
                request.aboutMe(),
                request.newsEmail(),
                profileImage,
                favoriteCategory,
                request.isSubscribed()
        );

        return MemberUpdateResponse.from(updatedMember.getId());
    }

    @Override
    public MemberSearchResponses searchMember(MemberSearchRequest memberSearchRequest) {
        Slice<MemberWithProfileImageAndFollowingStatus> memberAndMemberImageSlice = memberRepository.searchMember(
                mapper.toQueryCond(memberSearchRequest));

        return MemberSearchResponses.from(memberAndMemberImageSlice);
    }

    @Override
    public Long findMemberIdByEmail(String email) {
        Long memberId = memberRepository.findMemberIdByEmail(email);

        if (Objects.isNull(memberId)) {
            throw new DataNotFoundException("멤버를 찾을 수 없습니다.");
        }

        return memberId;
    }

}
