package org.noint.pickminbloom.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.member.service.GetMemberService;
import org.noint.pickminbloom.post.dto.PreRegisterPostDto;
import org.noint.pickminbloom.post.request.PreRegisterPostRequest;
import org.noint.pickminbloom.post.service.RegisterPrePostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pre-posts")
public class RegisterPrePostController {

    private final GetMemberService getMemberService;
    private final RegisterPrePostService registerPrePostService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> preRestPost(@Valid @ModelAttribute PreRegisterPostRequest request,
                                            @AuthenticationPrincipal OAuth2User user) {
        log.info("preRestPost: {}", request);
        String email = Objects.requireNonNull(user.getAttribute("email"));
        Member member = getMemberService.getMember(email);
        PreRegisterPostDto dto = new PreRegisterPostDto(request, member);
        registerPrePostService.preRegisterPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
