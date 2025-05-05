package org.noint.pickminbloom.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignController {

    @GetMapping("/sign")
    public String sign() {
        return "/sign";
    }
}
