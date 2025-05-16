package org.noint.pickminbloom.config.mdc;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {

            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("userId", extractUserId(request));
            MDC.put("clientIP", request.getRemoteAddr());
            MDC.put("requestURI", request.getRequestURI());
            MDC.put("httpMethod", request.getMethod());

            filterChain.doFilter(request, response);

        } finally {
            MDC.clear(); // 메모리 누수 방지
        }
    }

    private String extractUserId(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }
}