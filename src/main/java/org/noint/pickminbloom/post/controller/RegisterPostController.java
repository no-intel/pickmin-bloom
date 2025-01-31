package org.noint.pickminbloom.post.controller;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.dto.RegisterPostDto;
import org.noint.pickminbloom.post.service.RegisterPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterPostController {
    private final RegisterPostService registerPostService;

    @PostMapping("/posts")
    public ResponseEntity<String> registerPost(@RequestBody RegisterPostDto dto) {
        String name = registerPostService.registerPost(dto);
        return new ResponseEntity<>(name, HttpStatus.CREATED);
    }
}
