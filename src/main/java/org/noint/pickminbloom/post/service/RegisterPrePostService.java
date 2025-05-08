package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.dto.PreRegisterPostDto;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.repository.PrePostRepository;
import org.noint.pickminbloom.post.util.FileCodecUtil;
import org.noint.pickminbloom.post.validator.RegisterPrePostValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterPrePostService {

    private final PrePostRepository prePostRepository;
    private final RegisterPrePostValidator registerPrePostValidator;

    public void preRegisterPost(PreRegisterPostDto dto) {
        registerPrePostValidator.validateCoordinates(dto.postLat(), dto.postLon());
        PrePost prePost = new PrePost(
                dto.postName(),
                dto.postLat(),
                dto.postLon(),
                dto.type(),
                dto.postImg(),
                dto.requester()
        );
        prePostRepository.save(prePost);
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
