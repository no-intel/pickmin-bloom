package org.noint.pickminbloom.mail.template;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.event.RegisterEditPost;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class RegisterEditPostTemplate extends EmailTemplate<RegisterEditPost> {

    private final TemplateEngine templateEngine;

    public String build(RegisterEditPost event) {
        Context context = new Context();
        context.setVariable("editId", event.editId());
        context.setVariable("orignName", event.post().getName());
        context.setVariable("editName", event.editName());
        context.setVariable("orignType", event.post().getType());
        context.setVariable("editType", event.editType());

        return templateEngine.process("mail/register-edit-post-email", context);
    }
}
