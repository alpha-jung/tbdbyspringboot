package com.example.demo.config.security;

import com.example.demo.api.entity.RefreshToken;
import com.example.demo.api.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("${spring.jwt.secret.access}")
    private String secretAccessKey;

    @Value("${spring.jwt.secret.refresh}")
    private String secretRefreshKey;

    @Value("${spring.jwt.expire.access}")
    private int expiredAccessToken;

    @Value("${spring.jwt.expire.refresh}")
    private int expiredRefreshToken;

    private final UserDetailsService userDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    protected void init() {
        secretAccessKey = Base64.getEncoder().encodeToString(secretAccessKey.getBytes());
        secretRefreshKey = Base64.getEncoder().encodeToString(secretRefreshKey.getBytes());
    }

    // JWT Access 토큰 생성
    // Access 토큰 값과 유효기간 Map 에 담아 리턴
    public Map<String, Object> createAccessToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);

        Date now = new Date();
        Date expireAt = new Date(now.getTime() + Duration.ofMinutes(expiredAccessToken).toMillis());

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(SignatureAlgorithm.HS256, secretAccessKey)
                .compact();

        Map<String, Object> atMap = new HashMap<>();
        atMap.put("accessToken", accessToken);
        atMap.put("expireAt", expireAt.getTime());

        return atMap;
    }

    // JWT Refresh 토큰 생성
    // 토큰 생성 후 Redis 에 저장
    // 저장 시, RefreshToken:UserId 형식으로 매핑
    // RefreshToken 을 Set-Cookie 헤더에 세팅하기 위한 ResponseCookie 생성
    public ResponseCookie createRefreshToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);

        Date now = new Date();

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(expiredRefreshToken).toMillis()))
                .signWith(SignatureAlgorithm.HS256, secretRefreshKey)
                .compact();

        RefreshToken refreshToken = new RefreshToken(jwtToken, userPk);
        refreshTokenRepository.save(refreshToken);

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", jwtToken)
                .maxAge(Duration.ofDays(expiredRefreshToken))
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();

        return responseCookie;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPK(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPK(String token) {
        return Jwts.parser().setSigningKey(secretAccessKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request, String type) {
        if(type.equals("access")) {
            return request.getHeader("X-AUTH-TOKEN") != null ? request.getHeader("X-AUTH-TOKEN") : request.getHeader("Authorization");
        } else {
            return request.getHeader("cookie") != null ? request.getHeader("cookie") : "";
        }

    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validationToken(String token, String type) {
        try {
            Jws<Claims> claimsJws = null;

            if(type.equals("access")) {
                claimsJws = Jwts.parser().setSigningKey(secretAccessKey).parseClaimsJws(token);
            } else if(type.equals("refresh")) {
                claimsJws = Jwts.parser().setSigningKey(secretRefreshKey).parseClaimsJws(token);
            }

            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
