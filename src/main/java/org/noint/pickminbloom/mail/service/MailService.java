package org.noint.pickminbloom.mail.service;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.mail.template.RegisterPrePostTemplate;
import org.noint.pickminbloom.post.event.RegisterPrePost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RegisterPrePostTemplate registerPrePostTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.master}")
    private String master;

    @EventListener(classes = RegisterPrePost.class)
    public void sendRegisterMail(RegisterPrePost event) throws MessagingException {
        String html = registerPrePostTemplate.build(event);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(master);
        helper.setSubject("엽서 등록 신청");
        helper.setFrom(from);
        helper.setText(html, true);
        helper.addInline("img", registerPrePostTemplate.bindImg(event.img()));

        javaMailSender.send(message);
    }
}
