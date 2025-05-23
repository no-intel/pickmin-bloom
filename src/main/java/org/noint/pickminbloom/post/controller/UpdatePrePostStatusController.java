package org.noint.pickminbloom.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.member.service.GetMemberService;
import org.noint.pickminbloom.post.dto.UpdatePrePostStatusDto;
import org.noint.pickminbloom.post.enums.PrePostStatus;
import org.noint.pickminbloom.post.service.ConfirmPrePostService;
import org.noint.pickminbloom.post.service.RejectPrePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pre-posts")
public class UpdatePrePostStatusController {

    private final ConfirmPrePostService confirmPrePostService;
    private final RejectPrePostService rejectPrePostService;
    private final GetMemberService getMemberService;

    @PutMapping("/{prePostId}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Void> confirm(@PathVariable Long prePostId,
                                        @AuthenticationPrincipal OAuth2User user) {
        log.info("Confirm pre post: {}", prePostId);
        String email = Objects.requireNonNull(user.getAttribute("email"));
        Member member = getMemberService.getMember(email);
        UpdatePrePostStatusDto updatePrePostStatusDto = new UpdatePrePostStatusDto(prePostId, PrePostStatus.CONFIRMED, member.getId());
        confirmPrePostService.confirm(updatePrePostStatusDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{prePostId}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Void> reject(@PathVariable Long prePostId,
                                        @AuthenticationPrincipal OAuth2User user) {
        log.info("Reject pre post: {}", prePostId);
        String email = Objects.requireNonNull(user.getAttribute("email"));
        Member member = getMemberService.getMember(email);
        UpdatePrePostStatusDto updatePrePostStatusDto = new UpdatePrePostStatusDto(prePostId, PrePostStatus.REJECTED, member.getId());
        rejectPrePostService.reject(updatePrePostStatusDto);
        return ResponseEntity.ok().build();
    }

    // 개발용 편의기능
    @PutMapping("/confirm/all")
    @PreAuthorize("hasAnyRole('MASTER')")
    public ResponseEntity<Void> confirmAll() {
        confirmPrePostService.confirmAll();
        return ResponseEntity.ok().build();
    }
}
