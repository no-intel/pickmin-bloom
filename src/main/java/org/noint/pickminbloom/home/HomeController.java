package org.noint.pickminbloom.home;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Value("${version}")
    private String version;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User user) {
        model.addAttribute("version", version);
        if (user != null) {
            model.addAttribute("name", user.getAttribute("name"));
            model.addAttribute("email", user.getAttribute("email"));
            model.addAttribute("picture", user.getAttribute("picture"));
            model.addAttribute("isLogin", true);
        }else {
            model.addAttribute("isLogin", false);
        }
        return "index";
    }
}
