package com.tenten.linkhub.domain.auth.service;

import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.model.Role;
import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MemberFindResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    public OAuth2UserService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 제공자 및 고유번호 DB 저장
        String socialId = Optional.ofNullable(oAuth2User.getAttribute("id"))
                .map(Object::toString)
                .orElseThrow(() -> new OAuth2AuthenticationException("Social ID not found"));

        String provider = userRequest.getClientRegistration().getRegistrationId();

        MemberFindResponse memberFindResponse = memberService.findMember(socialId, Provider.valueOf(provider));

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("socialId", socialId);
        attributes.put("memberId", memberFindResponse.memberId());
        attributes.put("provider", provider);
        attributes.put("role", Role.USER);

        return new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
    }
}
