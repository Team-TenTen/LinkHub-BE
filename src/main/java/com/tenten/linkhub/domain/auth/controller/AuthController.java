package com.tenten.linkhub.domain.auth.controller;


import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.auth.controller.dto.LogoutApiResponse;
import com.tenten.linkhub.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "auth", description = "auth API Document")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 카카오 로그아웃 API
     */
    @Operation(
            summary = "카카오 로그아웃 API",
            description = "JWT를 받아 카카오 서비스에서 로그아웃 합니다. 호출 전 카카오와의 세션을 끊고, 호출 후 JWT를 프론트엔드에서 버려야 합니다. 세부적인 순서는 프론트 구현에 따라 달라질 수 있습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "401",
                            description = "로그아웃 요청을 보낸 경우에는 홈페이지 또는 로그아웃 전 기존 페이지로, 그 외의 경우는 권한이 없는 접근을 한 것이므로 소셜 로그인 페이지로 리다이렉션 해야합니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "RedirectRequired",
                                            value = "{\"errorCode\":\"G002\",\"message\":\"리다이렉션이 필요합니다.\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping(value = "/kakao/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LogoutApiResponse>> logoutKakao(@AuthenticationPrincipal MemberDetails memberDetails) {

        return authService.logoutKakao(memberDetails.socialId());
    }

}