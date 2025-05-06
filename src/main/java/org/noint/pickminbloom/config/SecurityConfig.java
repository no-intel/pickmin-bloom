package org.noint.pickminbloom.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.config.filter.FakeOAuth2AuthFilter;
import org.noint.pickminbloom.member.service.SignService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final SignService signService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            return http
                    .addFilterBefore(new FakeOAuth2AuthFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/", "/sign", "/favicon.ico", "/img/**", "/js/**", "/css/**", "/lib/**", "/posts")
                            .permitAll()
                            .anyRequest().authenticated())
                    .oauth2Login(oauth -> oauth
                            .loginPage("/sign")
                            .defaultSuccessUrl("/")
                            .userInfoEndpoint(userInfo -> userInfo.userService(signService))
                    ).csrf(AbstractHttpConfigurer::disable)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Security config error: {}", e.getMessage());
            throw new SecurityException();
        }
    }
}
