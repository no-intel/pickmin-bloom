package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.PreRegisterPostDto;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.event.RegisterPrePost;
import org.noint.pickminbloom.post.repository.PrePostRepository;
import org.noint.pickminbloom.post.validator.RegisterPrePostValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RegisterPrePostService {

    private final PrePostRepository prePostRepository;
    private final RegisterPrePostValidator registerPrePostValidator;
    private final ApplicationEventPublisher eventPublisher;

    public void preRegisterPost(PreRegisterPostDto dto) {
        log.info("preRegisterPost: {}", dto);
        registerPrePostValidator.validateCoordinates(dto.postLat(), dto.postLon());
        PrePost prePost = new PrePost(
                dto.postName(),
                dto.postLat(),
                dto.postLon(),
                dto.type(),
                dto.postImg(),
                dto.noImg(),
                dto.requester()
        );
        prePostRepository.save(prePost);
        eventPublisher.publishEvent(new RegisterPrePost(prePost));
    }

//    private PrePost createPrePostFromDto(PreRegisterPostDto dto) {
//        String encodedImage = dto.postImg() == null ? null : fileCodecUtil.encode(dto.postImg());
//
//        return new PrePost(
//                dto.postName(),
//                dto.postLat(),
//                dto.postLon(),
//                dto.type(),
//                encodedImage,
//                dto.registerId()
//        );
//    }
}
