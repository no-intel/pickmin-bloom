package org.noint.pickminbloom.post.dto;

import org.noint.pickminbloom.post.request.RegisterPostRequest;
import org.springframework.web.multipart.MultipartFile;

public record RegisterPostDto(
        String geohash,
        Double latitude,
        Double longitude,
        MultipartFile image,
        String name,
        String type,
        String location
) {
    public RegisterPostDto(String geohash, RegisterPostRequest request) {
        this(geohash, request.latitude(), request.longitude(), request.image(), request.name(), request.type(), request.location());
    }
}
