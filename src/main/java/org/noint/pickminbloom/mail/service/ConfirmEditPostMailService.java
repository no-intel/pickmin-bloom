package org.noint.pickminbloom.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.mail.MessagingBuildException;
import org.noint.pickminbloom.mail.template.ConfirmEditPostTemplate;
import org.noint.pickminbloom.post.event.ConfirmEditPostRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmEditPostMailService {

    private final JavaMailSender javaMailSender;
    private final ConfirmEditPostTemplate confirmEditPostTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @TransactionalEventListener(classes = ConfirmEditPostRequest.class, phase = TransactionPhase.AFTER_COMMIT)
    public void confirmPrePost(ConfirmEditPostRequest event) {
        log.info("EVENT - Sending confirm editPost mail: {}", event);
        String html = confirmEditPostTemplate.build(event);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(event.requester().getEmail());
            helper.setSubject("엽서 수정 승인");
            helper.setFrom(from);
            helper.setText(html, true);
            if (event.editImg() != null) {
                helper.addInline("img", confirmEditPostTemplate.bindImg(event.editImg()));
            }
        } catch (MessagingException e) {
            log.error("sendConfirmEditPostMail Exception", e);
            throw new MessagingBuildException("ConfirmEditPostMail");
        }
        javaMailSender.send(message);
    }
}
