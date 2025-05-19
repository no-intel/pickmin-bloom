package org.noint.pickminbloom.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.member.service.GetMemberService;
import org.noint.pickminbloom.post.dto.RequestEditPostDto;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.request.RequestEditPostRequest;
import org.noint.pickminbloom.post.service.GetPostService;
import org.noint.pickminbloom.post.service.RequestEditPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class RequestEditPostController {

    private final GetPostService getPostService;
    private final GetMemberService getMemberService;
    private final RequestEditPostService requestEditPostService;

    @PutMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> requestEditPost(@PathVariable Long postId,
                                                @Valid @ModelAttribute RequestEditPostRequest request,
                                                @AuthenticationPrincipal OAuth2User user) {
        Post post = getPostService.getPost(postId);
        String email = Objects.requireNonNull(user.getAttribute("email"));
        Member member = getMemberService.getMember(email);

        RequestEditPostDto dto = new RequestEditPostDto(post, request, member);
        requestEditPostService.requestEditPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
