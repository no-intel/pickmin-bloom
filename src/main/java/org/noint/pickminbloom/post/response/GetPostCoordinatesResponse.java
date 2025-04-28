package org.noint.pickminbloom.post.response;

import org.noint.pickminbloom.post.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record GetPostCoordinatesResponse(double latitude,
                                         double longitude,
                                         String name,
                                         String geohash,
                                         String type,
                                         String location,
                                         String presignedUrl) {
    public static List<GetPostCoordinatesResponse> create(List<Post> posts, Map<String, String> presignedUrls) {
        List<GetPostCoordinatesResponse> responses = new ArrayList<>();
        posts.forEach(post -> {
            responses.add(new GetPostCoordinatesResponse(
                    post.getCoordinates().getY(),
                    post.getCoordinates().getX(),
                    post.getName(),
                    post.getGeohash(),
                    post.getType(),
                    post.getLocation(),
                    presignedUrls.get(post.getGeohash())
            ));
        });

        return responses;
    }
}
