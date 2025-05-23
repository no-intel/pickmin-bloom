package org.noint.pickminbloom.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.member.NotExistMemberException;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMemberService {

    private final MemberRepository memberRepository;

    public Member getMember(Long memberId) {
        log.info("getMember: {}", memberId);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotExistMemberException("id"));
    }

    public Member getMember(String email) {
        log.info("getMember: {}", email);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotExistMemberException("email"));
    }
}
