package org.noint.pickminbloom.post.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetPostCoordinatesResponse {
    private final double latitude;
    private final double longitude;

    public static List<GetPostCoordinatesResponse> create(List<Post> posts) {
        List<GetPostCoordinatesResponse> responses = new ArrayList<>();
        posts.forEach(post -> {
            responses.add(new GetPostCoordinatesResponse(post.getCoordinates().getY(), post.getCoordinates().getX()));
        });

        return responses;
    }
}
