package com.tenten.linkhub.domain.auth.service;

import com.tenten.linkhub.domain.auth.controller.dto.LogoutApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Service
public class AuthService {

    private final WebClient webClient;

    @Value("${kakao.admin-key}")
    private String kakaoAdminKey;

    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://kapi.kakao.com").build();
    }

    public Mono<ResponseEntity<LogoutApiResponse>> logoutKakao(String socialId) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("target_id_type", "user_id");
        formData.add("target_id", socialId);

        return webClient.post()
                .uri("/v1/user/logout")
                .header("Authorization", "KakaoAK " + kakaoAdminKey)
                .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8")))
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(LogoutApiResponse.class)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
