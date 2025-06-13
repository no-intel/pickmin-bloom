package org.noint.pickminbloom.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class TestController {
    private final MailService mailService;

    @GetMapping
    public void send() {
        mailService.sendSimpleMail();
    }
}
