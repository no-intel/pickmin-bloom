package org.noint.pickminbloom.mail.template;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.event.ConfirmEditPostRequest;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class ConfirmEditPostTemplate extends EmailTemplate<ConfirmEditPostRequest> {

    private final TemplateEngine templateEngine;

    public String build(ConfirmEditPostRequest event) {
        Context context = new Context();
        context.setVariable("latitude", event.latitude());
        context.setVariable("longitude", event.longitude());
        context.setVariable("editName", event.editName());
        context.setVariable("editType", event.editType());

        return templateEngine.process("mail/confirm-edit-post-email", context);
    }
}
