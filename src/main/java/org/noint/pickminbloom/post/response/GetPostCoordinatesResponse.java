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
                                         double distance,
                                         String downloadUrl) {
    public static List<GetPostCoordinatesResponse> create(List<GetPostResponseDto> posts, Map<String, String> downloadUrls) {
        List<GetPostCoordinatesResponse> responses = new ArrayList<>();
        posts.forEach(post -> {
            responses.add(new GetPostCoordinatesResponse(
                    post.latitude(),
                    post.longitude(),
                    post.name(),
                    post.geohash(),
                    post.type().getTypeKor(),
                    Math.round(post.distance() * 100.0) / 100.0,
                    downloadUrls.get(post.geohash())
            ));
        });

        return responses;
    }
}
