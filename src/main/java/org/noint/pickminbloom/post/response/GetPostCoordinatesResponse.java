package org.noint.pickminbloom.post.response;

import org.noint.pickminbloom.post.dto.GetPostResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record GetPostCoordinatesResponse(double latitude,
                                         double longitude,
                                         String name,
                                         String geohash,
                                         String type,
                                         String location,
                                         double distance,
                                         String presignedUrl) {

    public static List<GetPostCoordinatesResponse> create(List<GetPostResponseDto> posts, Map<String, String> presignedUrls) {
        List<GetPostCoordinatesResponse> responses = new ArrayList<>();
        posts.forEach(post -> {
            responses.add(new GetPostCoordinatesResponse(
                    post.coordinates().getY(),
                    post.coordinates().getX(),
                    post.name(),
                    post.geohash(),
                    post.type(),
                    post.location(),
                    Math.round(post.distance() * 100.0) / 100.0, // 소수점 2번째 자리까지 반올림
                    presignedUrls.get(post.geohash())
            ));
        });

        return responses;
    }
}
