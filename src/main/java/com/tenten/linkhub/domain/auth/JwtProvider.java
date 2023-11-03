package com.tenten.linkhub.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

    private final Key key;

    @Value("${jwt.expiration}")
    private long expiration;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(DefaultOAuth2User defaultOAuth2User) {
        Long memberId = (Long) defaultOAuth2User.getAttributes().get("memberId");
        String socialId = (String) defaultOAuth2User.getAttributes().get("socialId");
        Boolean isLoggedIn = (Boolean) defaultOAuth2User.getAttributes().get("isLoggedIn");
        String provider = (String) defaultOAuth2User.getAttributes().get("provider");

        List<String> roles = defaultOAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(socialId)
                .claim("memberId", memberId)
                .claim("roles", roles)
                .claim("isLoggedIn", isLoggedIn)
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

        List<String> roles = claims.get("roles", List.class);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        MemberDetails memberDetails = new MemberDetails(memberId, socialId, provider, authorities);

        return new UsernamePasswordAuthenticationToken(memberDetails, token, authorities);
    }


}
