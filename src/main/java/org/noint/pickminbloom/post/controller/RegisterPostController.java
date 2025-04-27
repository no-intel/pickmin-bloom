package org.noint.pickminbloom.post.controller;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.request.RegisterPostRequest;
import org.noint.pickminbloom.post.service.RegisterPostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class RegisterPostController {
    private final RegisterPostService registerPostService;

    @PostMapping
    public void registerPost(@ModelAttribute RegisterPostRequest request) {
        registerPostService.registerPost(request);
    }
}
