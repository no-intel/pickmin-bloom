package org.noint.pickminbloom.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.member.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        try {
            log.info("loadUser: {}", userRequest);

            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
            log.info("oauth2User: {}", oauth2User);

            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");

            Member member = memberRepository.findByEmail(email)
                    .orElseGet(() -> registerMember(email, name));
            log.info("member: {}", member);

            Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
            attributes.put("role", member.getRole().toAuthority());

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(member.getRole().toAuthority())),
                    attributes,
                    "email"
            );
        } catch (Exception e) {
            log.error("OAuth2 로그인 처리 중 에러", e);
            throw new OAuth2AuthenticationException(e.getMessage());
        }

    }

    protected Member registerMember(String email, String name) {
        Member member = new Member(email, name);
        memberRepository.save(member);
        return member;
    }
}
