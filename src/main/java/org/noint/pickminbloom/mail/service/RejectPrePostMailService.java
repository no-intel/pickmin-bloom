package org.noint.pickminbloom.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.mail.MessagingBuildException;
import org.noint.pickminbloom.mail.template.RejectPrePostTemplate;
import org.noint.pickminbloom.post.event.RejectPrePostStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class RejectPrePostMailService {

    private final JavaMailSender javaMailSender;
    private final RejectPrePostTemplate rejectPrePostTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @TransactionalEventListener(classes = RejectPrePostStatus.class, phase = TransactionPhase.AFTER_COMMIT)
    public void rejectPrePost(RejectPrePostStatus event) {
        log.info("EVENT - Sending reject prePost mail: {}", event);
        String html = rejectPrePostTemplate.build(event);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(event.requester().getEmail());
            helper.setSubject("엽서 등록 반려");
            helper.setFrom(from);
            helper.setText(html, true);
            if (event.img() != null) {
                helper.addInline("img", rejectPrePostTemplate.bindImg(event.img()));
            }
        } catch (MessagingException e) {
            log.error("sendRejectPrePostMail Exception", e);
            throw new MessagingBuildException("RejectPrePostMail");
        }
        javaMailSender.send(message);
    }
}
