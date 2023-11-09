package com.tenten.linkhub.domain.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenten.linkhub.domain.auth.JwtAuthenticationFilter;
import com.tenten.linkhub.domain.auth.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;

    private final JwtProvider jwtProvider;

    public SecurityConfig(OAuth2UserService oAuth2UserService, JwtProvider jwtProvider) {
        this.oAuth2UserService = oAuth2UserService;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(config ->
                        config
                                .requestMatchers(HttpMethod.GET).permitAll() // 임시로 풀어준 것 운영시에는 허용 주소 관리
                                .requestMatchers("/members/join").permitAll()
                                .requestMatchers("/members/emails/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .successHandler(successHandler())
                )
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {

        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            Map<String, Object> responseData = new HashMap<>();

            if (memberIdExists(defaultOAuth2User)) {
                final String jwt = jwtProvider.generateTokenFromOAuth(defaultOAuth2User);
                responseData.put("jwt", jwt);
            }

            if (!memberIdExists(defaultOAuth2User)) {
                responseData.put("socialId", defaultOAuth2User.getAttributes().get("socialId"));
                responseData.put("provider", defaultOAuth2User.getAttributes().get("provider"));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(responseData);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();
        });
    }

    private static boolean memberIdExists(DefaultOAuth2User defaultOAuth2User) {
        return defaultOAuth2User.getAttributes().get("memberId") != null;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

//        configuration.setAllowedOrigins(Arrays.asList(
//                "https://link-hub.site"
//        ));
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true); // 개발용으로 임시 쿠키 막음

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    public static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException authException) throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            String redirectUrl = "https://api.link-hub.site/oauth2/authorization/kakao";
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("redirectURL", redirectUrl);
            responseBody.put("message", "로그인 페이지로 리다이렉션이 필요합니다.");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(responseBody);

            response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        }

    }

}
