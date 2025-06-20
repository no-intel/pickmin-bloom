package org.noint.pickminbloom.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.RegisterPostDto;
import org.noint.pickminbloom.post.request.RegisterPostRequest;
import org.noint.pickminbloom.post.service.RegisterPostService;
import org.noint.pickminbloom.post.util.GeoHashUtil;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class RegisterPostController {
    private final RegisterPostService registerPostService;
    private final GeoHashUtil geoHashUtil;

    @PostMapping
    public void registerPost(@ModelAttribute RegisterPostRequest request) {
        log.info("Register post: {}", request);
        String geohash = geoHashUtil.encode(request.latitude(), request.longitude());
        RegisterPostDto dto = new RegisterPostDto(geohash, request);
        registerPostService.registerPost(dto);
    }
}
