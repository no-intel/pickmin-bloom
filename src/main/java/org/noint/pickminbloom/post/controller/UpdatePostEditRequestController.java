package org.noint.pickminbloom.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.member.service.GetMemberService;
import org.noint.pickminbloom.post.dto.UpdatePostEditRequestDto;
import org.noint.pickminbloom.post.service.ConfirmPostEditRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts-edit-request")
public class UpdatePostEditRequestController {
    private final ConfirmPostEditRequestService confirmPostEditRequestService;
    private final GetMemberService getMemberService;

    @PutMapping("/{prePostId}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Void> confirm(@PathVariable Long prePostId,
                                        @AuthenticationPrincipal OAuth2User user) {
        String email = Objects.requireNonNull(user.getAttribute("email"));
        Member member = getMemberService.getMember(email);
        UpdatePostEditRequestDto updatePrePostStatusDto = new UpdatePostEditRequestDto(prePostId, member.getId());
        confirmPostEditRequestService.confirm(updatePrePostStatusDto);
        return ResponseEntity.ok().build();
    }
}
