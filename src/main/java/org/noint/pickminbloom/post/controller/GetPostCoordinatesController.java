package org.noint.pickminbloom.post.controller;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesByViewDto;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesDto;
import org.noint.pickminbloom.post.request.GetPostCoordinatesByViewRequest;
import org.noint.pickminbloom.post.response.GetPostCoordinatesResponse;
import org.noint.pickminbloom.post.service.GetPostCoordinatesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class GetPostCoordinatesController {
    private final GetPostCoordinatesService getPostCoordinatesService;

//    @GetMapping
//    public ResponseEntity<List<GetPostCoordinatesResponse>> getPostCoordinates(@ModelAttribute GetPostCoordinatesDto dto) {
//        List<GetPostCoordinatesResponse> response = getPostCoordinatesService.getPostCoordinates(dto);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<List<GetPostCoordinatesResponse>> getPostCoordinates(@ModelAttribute GetPostCoordinatesByViewRequest request) {
        List<GetPostCoordinatesResponse> postCoordinates = getPostCoordinatesService.getPostCoordinates(request);
        return new ResponseEntity<>(postCoordinates, HttpStatus.OK);
    }
}
