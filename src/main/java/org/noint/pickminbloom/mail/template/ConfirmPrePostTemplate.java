package org.noint.pickminbloom.mail.template;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.event.ConfirmPrePostStatus;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class ConfirmPrePostTemplate extends EmailTemplate<ConfirmPrePostStatus> {

    private final TemplateEngine templateEngine;

    public String build(ConfirmPrePostStatus event) {
        Context context = new Context();
        context.setVariable("name", event.name());
        context.setVariable("latitude", event.latitude());
        context.setVariable("longitude", event.longitude());

        return templateEngine.process("mail/confirm-pre-post-email", context);
    }
}
