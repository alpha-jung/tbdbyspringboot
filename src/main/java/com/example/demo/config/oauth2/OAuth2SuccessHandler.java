package com.example.demo.config.oauth2;

import com.example.demo.api.entity.UserMongoDB;
import com.example.demo.config.security.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);
        log.info("oAuth2User.getName() : " + oAuth2User.getName());
        log.info("oAuth2User.getAttributes().toString() : " + oAuth2User.getAttributes().toString());

        String email = oAuth2User.getName();

        // 최초 로그인이라면 회원가입 처리를 한다.
        String targetUrl;
        log.info("토큰 발행 시작");

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");

        Map<String, Object> atMap = jwtProvider.createAccessToken(email, roles);
        ResponseCookie responseCookie = jwtProvider.createRefreshToken(email, roles);

        String accessToken = atMap.get("accessToken").toString();
        String expireAt = atMap.get("expireAt").toString();
        String refreshToken = responseCookie.toString();

        log.info("[oauth2] accessToken : " + accessToken);
        log.info("[oauth2] refreshToken : " + refreshToken);

        targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2Login")
                .queryParam("at", accessToken)
                .queryParam("expireAt", expireAt)
                .build().toUriString();

        log.info("targetUrl : " + targetUrl);

        response.setHeader("Set-Cookie", refreshToken);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
