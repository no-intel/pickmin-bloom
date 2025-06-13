package org.noint.pickminbloom.mail.template;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.event.RegisterPrePost;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class RegisterPrePostTemplate extends EmailTemplate<RegisterPrePost> {

    private final TemplateEngine templateEngine;

    public String build(RegisterPrePost event) {
        Context context = new Context();
        context.setVariable("id", event.id());
        context.setVariable("name", event.name());
        context.setVariable("latitude", event.latitude());
        context.setVariable("longitude", event.longitude());

        return templateEngine.process("register-pre-post-email", context);
    }
}
