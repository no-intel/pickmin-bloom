package org.noint.pickminbloom.config.mdc;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            String requestId = UUID.randomUUID().toString();
            String userId = extractUserId((HttpServletRequest) request); // JWT나 세션에서 파싱

            MDC.put("requestId", requestId);

            if (userId != null) {
                MDC.put("userId", userId);
            }

            chain.doFilter(request, response); // 요청 계속 진행

        } finally {
            MDC.clear(); // 메모리 누수 방지
        }
    }

    private String extractUserId(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }
}