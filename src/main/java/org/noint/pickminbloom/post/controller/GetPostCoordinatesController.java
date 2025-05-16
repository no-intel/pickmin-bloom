package org.noint.pickminbloom.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesByViewDto;
import org.noint.pickminbloom.post.request.GetPostCoordinatesByViewRequest;
import org.noint.pickminbloom.post.response.GetPostCoordinatesResponse;
import org.noint.pickminbloom.post.service.GetPostCoordinatesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class GetPostCoordinatesController {
    private final GetPostCoordinatesService getPostCoordinatesService;

    @GetMapping
    public ResponseEntity<List<GetPostCoordinatesResponse>> getPostCoordinates(@ModelAttribute GetPostCoordinatesByViewRequest request) {
        log.info("Get post By coordinates: {}", request);
        GetPostCoordinatesByViewDto dto = new GetPostCoordinatesByViewDto(request);
        List<GetPostCoordinatesResponse> postCoordinates = getPostCoordinatesService.getPostCoordinates(dto);
        return new ResponseEntity<>(postCoordinates, HttpStatus.OK);
    }
}
