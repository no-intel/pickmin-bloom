package org.noint.pickminbloom.mail.template;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.event.ConfirmPrePostStatus;
import org.noint.pickminbloom.post.event.RejectPrePostStatus;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class RejectPrePostTemplate extends EmailTemplate<RejectPrePostStatus> {

    private final TemplateEngine templateEngine;

    public String build(RejectPrePostStatus event) {
        Context context = new Context();
        context.setVariable("name", event.name());
        context.setVariable("latitude", event.latitude());
        context.setVariable("longitude", event.longitude());

        return templateEngine.process("mail/reject-pre-post-email", context);
    }
}
