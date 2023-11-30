package com.tenten.linkhub.domain.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    public OAuth2SuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = "https://link-hub.site/oauth2/redirection/kakao"; // 운영 시 수정 예정 ("https://link-hub.site/oauth2/redirection/kakao")

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        if (memberIdExists(oAuth2User)) {
            final String jwt = jwtProvider.generateTokenFromOAuth(oAuth2User);

            String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                    .queryParam("jwt", jwt)
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }

        if (!memberIdExists(oAuth2User)) {
            String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                    .queryParam("socialId", oAuth2User.getAttributes().get("socialId"))
                    .queryParam("provider", oAuth2User.getAttributes().get("provider"))
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }

    }

    private static boolean memberIdExists(OAuth2User oAuth2User) {
        return oAuth2User.getAttributes().get("memberId") != null;
    }

}
