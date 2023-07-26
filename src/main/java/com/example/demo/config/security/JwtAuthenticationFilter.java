package com.example.demo.config.security;

import com.example.demo.api.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = jwtProvider.resolveToken(request, "access");

        log.info("[jwt filter] accessToken : " + accessToken);

        if (accessToken != null && !accessToken.equals("")) {
            String[] atSplit = accessToken.split(" ");

            if (!atSplit[0].equals("Bearer")) {
                throw new ServletException("Access Token 값이 유효하지 않습니다.");
            }

            try {
                accessToken = atSplit[1];
            } catch(Exception e) {
                throw new ServletException("Access Token 값이 유효하지 않습니다.");
            }

            Boolean isValidJwtToken = jwtProvider.validationToken(accessToken, "access");

            if (isValidJwtToken) {
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } /*else {
                String refreshToken = jwtProvider.resolveToken(request, "refresh");

                if(refreshToken == null || refreshToken.equals("")) {
                    throw new ServletException("Refresh Token 값이 유효하지 않습니다. 다시 로그인해주세요.");
                }

                refreshToken = refreshToken.split("=")[1];

                if(refreshToken == null || refreshToken.equals("")) {
                    throw new ServletException("Refresh Token 값이 유효하지 않습니다. 다시 로그인해주세요.");
                }

                Optional<RefreshToken> rt = refreshTokenRepository.findById(refreshToken);

                Boolean isValidRefreshToken = jwtProvider.validationToken(refreshToken, "refresh");

                if (isValidRefreshToken && rt.isPresent()) {
                    //String userId = jwtProvider.getUserPK(refreshToken, "refresh");
                    String userId = rt.get().getUserId();
                    List<String> roles = new ArrayList<>();
                    roles.add("ROLE_USER");

                    //accessToken = jwtProvider.createAccessToken(userId, roles);
                    Map<String, Object> atMap = jwtProvider.createAccessToken(userId, roles);
                    accessToken = atMap.get("accessToken").toString();

                    response.setHeader("Authorization", accessToken);
                    response.setHeader("expireAt", atMap.get("expireAt").toString());

                    Authentication authentication = jwtProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }*/

            else {
                throw new ServletException("Access Token 값이 만료됐습니다.");
            }
        }

        chain.doFilter(request, response);
    }
}
