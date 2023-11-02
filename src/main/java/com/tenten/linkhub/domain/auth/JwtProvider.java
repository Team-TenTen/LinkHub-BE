package com.tenten.linkhub.domain.auth;

import com.tenten.linkhub.domain.member.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration = 1000 * 60 * 60 * 10;

    private String SECRET_KEY = "44407b6f5caf26cf5cc82272d3719441ac716b2510f3bae495f7fffb73dfa608b8ad3ffc53f2a5c3f94421dac6cd2a8b2effe88988c837ea8b432a97a4de20ea";

    byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

    public String generateToken(DefaultOAuth2User defaultOAuth2User) {
        Long memberId = (Long) defaultOAuth2User.getAttributes().get("memberId");
        String socialId = (String) defaultOAuth2User.getAttributes().get("socialId");

        List<String> roles = defaultOAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(socialId)
                .claim("memberId", memberId)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(originalKey, SignatureAlgorithm.HS256)
                .compact();

    }
}
