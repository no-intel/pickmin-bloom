package org.noint.pickminbloom.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.member.service.GetMemberService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//@Profile("!product")
@Component
@Order(1)
@RequiredArgsConstructor
public class FakeOAuth2AuthFilter extends OncePerRequestFilter {

    private final GetMemberService getMemberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("fake ")) {
            String email = authHeader.substring(5).trim();
            Member member = getMemberService.getMember(email);

            Map<String, Object> attributes = Map.of("email", email);

            OAuth2User fakeUser = new DefaultOAuth2User(
                    List.of(new SimpleGrantedAuthority(member.getRole().toAuthority())),
                    attributes,
                    "email" // name attribute key
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(fakeUser, null, fakeUser.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}