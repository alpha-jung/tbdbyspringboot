package com.example.demo.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class FilterExceptionHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain
            filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch(ServletException e) {
            setErrorResponse(response, e.getMessage());
        } catch(Exception e) {
            setErrorResponse(response, "토큰 값이 유효하지 않습니다.");
        }
    }

    private void setErrorResponse(HttpServletResponse response, String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try {
            response.getWriter().write(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
