package org.noint.pickminbloom.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.mail.MessagingBuildException;
import org.noint.pickminbloom.mail.template.RejectPreEditTemplate;
import org.noint.pickminbloom.post.event.RejectPostEditRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class RejectEditPostMailService {

    private final JavaMailSender javaMailSender;
    private final RejectPreEditTemplate rejectPreEditTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @TransactionalEventListener(classes = RejectPostEditRequest.class, phase = TransactionPhase.AFTER_COMMIT)
    public void confirmPrePost(RejectPostEditRequest event) {
        log.info("EVENT - Sending reject editPost mail: {}", event);
        String html = rejectPreEditTemplate.build(event);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(event.requester().getEmail());
            helper.setSubject("엽서 수정 반려");
            helper.setFrom(from);
            helper.setText(html, true);
            if (event.editImg() != null) {
                helper.addInline("img", rejectPreEditTemplate.bindImg(event.editImg()));
            }
        } catch (MessagingException e) {
            log.error("sendRejectEditPostMail Exception", e);
            throw new MessagingBuildException("RejectEditPostMail");
        }
        javaMailSender.send(message);
    }
}
