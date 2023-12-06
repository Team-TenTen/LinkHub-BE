package com.tenten.linkhub.domain.auth;

import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.model.Role;
import com.tenten.linkhub.domain.member.service.dto.MemberAuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    private final Key key;

    @Value("${jwt.expiration}")
    private long expiration;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateTokenFromOAuth(OAuth2User oAuth2User) {
        Long memberId = (Long) oAuth2User.getAttributes().get("memberId");
        String socialId = (String) oAuth2User.getAttributes().get("socialId");
        String provider = (String) oAuth2User.getAttributes().get("provider");
        Role role = (Role) oAuth2User.getAttributes().get("role");

        return generateJwt(memberId, socialId, provider, role);
    }

    public String generateTokenFromMember(MemberAuthInfo memberAuthInfo) {
        Long memberId = memberAuthInfo.memberId();
        String socialId = memberAuthInfo.socialId();
        Provider provider = memberAuthInfo.provider();
        Role role = memberAuthInfo.role();

        return generateJwt(memberId, socialId, provider.getValue(), role);
    }

    private String generateJwt(
            Long memberId,
            String socialId,
            String provider,
            Role role) {
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(socialId)
                .claim("memberId", memberId)
                .claim("role", role.getCode())
                .claim("provider", provider)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        String socialId = claims.getSubject();
        Long memberId = claims.get("memberId", Long.class);
        String provider = claims.get("provider", String.class);
        String role = claims.get("role", String.class);

        MemberDetails memberDetails = new MemberDetails(memberId, socialId, provider, Role.valueOf(role));

        return new UsernamePasswordAuthenticationToken(memberDetails, token, Collections.emptyList());
    }


}
