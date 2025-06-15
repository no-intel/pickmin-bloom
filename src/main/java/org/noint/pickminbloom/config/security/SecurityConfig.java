package org.noint.pickminbloom.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.member.service.SignService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final SignService signService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final Optional<FakeOAuth2AuthFilter> fakeOAuth2AuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            fakeOAuth2AuthFilter.ifPresent(oAuth2AuthFilter -> http.addFilterBefore(oAuth2AuthFilter, UsernamePasswordAuthenticationFilter.class));
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/",
                                    "/sign",
                                    "/favicon.ico",
                                    "/img/**",
                                    "/js/**",
                                    "/css/**",
                                    "/lib/**",
                                    "/actuator/health",
                                    "/actuator/metrics/**",
                                    "/policy/**",
                                    "/login/oauth2/**"
                            ).permitAll()
                            .requestMatchers(HttpMethod.GET, "/posts").permitAll()
                            .anyRequest().authenticated())
                    .oauth2Login(oauth -> oauth
                            .loginPage("/sign")
                            .defaultSuccessUrl("/", true)
                            .userInfoEndpoint(userInfo -> userInfo.userService(signService))
                    ).csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling(handler -> handler
                            .authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler)
                    );
            return http.build();
        } catch (Exception e) {
            log.error("Security config error: {}", e.getMessage());
            throw new SecurityException();
        }
    }
}
