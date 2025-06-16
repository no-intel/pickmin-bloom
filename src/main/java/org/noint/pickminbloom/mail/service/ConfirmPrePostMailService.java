package org.noint.pickminbloom.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.mail.MessagingBuildException;
import org.noint.pickminbloom.mail.template.ConfirmPrePostTemplate;
import org.noint.pickminbloom.post.event.ConfirmPrePostStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmPrePostMailService {

    private final JavaMailSender javaMailSender;
    private final ConfirmPrePostTemplate confirmPrePostTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @TransactionalEventListener(classes = ConfirmPrePostStatus.class, phase = TransactionPhase.AFTER_COMMIT)
    public void confirmPrePost(ConfirmPrePostStatus event) {
        log.info("EVENT - Sending confirm prePost mail: {}", event);
        String html = confirmPrePostTemplate.build(event);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(event.requester().getEmail());
            helper.setSubject("엽서 등록 승인");
            helper.setFrom(from);
            helper.setText(html, true);
            helper.addInline("img", confirmPrePostTemplate.bindImg(event.img()));
        } catch (MessagingException e) {
            log.error("sendConfirmPrePostMail Exception", e);
            throw new MessagingBuildException("ConfirmPrePostMail");
        }
        javaMailSender.send(message);
    }
}
