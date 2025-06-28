package org.noint.pickminbloom.mail.template;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.event.RejectPostEditRequest;
import org.noint.pickminbloom.post.event.RejectPrePostStatus;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class RejectPreEditTemplate extends EmailTemplate<RejectPostEditRequest> {

    private final TemplateEngine templateEngine;

    public String build(RejectPostEditRequest event) {
        Context context = new Context();
        context.setVariable("latitude", event.latitude());
        context.setVariable("longitude", event.longitude());
        context.setVariable("editName", event.editName());
        context.setVariable("editType", event.editType());

        return templateEngine.process("mail/reject-edit-post-email", context);
    }
}
