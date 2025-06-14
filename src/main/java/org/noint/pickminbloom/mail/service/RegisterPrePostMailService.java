package org.noint.pickminbloom.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.mail.MessagingBuildException;
import org.noint.pickminbloom.mail.template.RegisterPrePostTemplate;
import org.noint.pickminbloom.post.event.RegisterPrePost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterPrePostMailService {

    private final JavaMailSender javaMailSender;
    private final RegisterPrePostTemplate registerPrePostTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.master}")
    private String master;

    @TransactionalEventListener(classes = RegisterPrePost.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendRegisterMail(RegisterPrePost event) {
        log.info("EVENT - Sending register prePost mail: {}", event);
        String html = registerPrePostTemplate.build(event);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(master);
            helper.setSubject("엽서 등록 신청");
            helper.setFrom(from);
            helper.setText(html, true);
            helper.addInline("img", registerPrePostTemplate.bindImg(event.img()));
        } catch (MessagingException e) {
            log.error("sendRegisterPrePostMail Exception", e);
            throw new MessagingBuildException("RegisterPrePostMail");
        }
        javaMailSender.send(message);
    }
}
