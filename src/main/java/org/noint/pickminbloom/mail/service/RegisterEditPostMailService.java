package org.noint.pickminbloom.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.mail.MessagingBuildException;
import org.noint.pickminbloom.mail.template.RegisterEditPostTemplate;
import org.noint.pickminbloom.post.event.RegisterEditPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterEditPostMailService {

    private final JavaMailSender javaMailSender;
    private final RegisterEditPostTemplate registerEditPostTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.master}")
    private String master;

    @TransactionalEventListener(classes = RegisterEditPost.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendRegisterMail(RegisterEditPost event) {
        log.info("EVENT - Sending register editPost mail: {}", event);
        String html = registerEditPostTemplate.build(event);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(master);
            helper.setSubject("엽서 수정 신청");
            helper.setFrom(from);
            helper.setText(html, true);
            if (event.editImg() != null) {
                helper.addInline("editImg", registerEditPostTemplate.bindImg(event.editImg()));
            }
        } catch (MessagingException e) {
            log.error("sendRegisterEditPostMail Exception", e);
            throw new MessagingBuildException("RegisterEditPostMail");
        }
        javaMailSender.send(message);
    }
}
