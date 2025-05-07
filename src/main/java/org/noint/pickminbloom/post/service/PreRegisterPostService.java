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
public class PreRegisterPostService {

    private final FileCodecUtil fileCodecUtil;
    private final PrePostRepository prePostRepository;
    private final RegisterPrePostValidator registerPrePostValidator;

    public void preRegisterPost(PreRegisterPostDto dto) {
        registerPrePostValidator.validateCoordinates(dto.postLat(), dto.postLon());
        PrePost prePost = createPrePostFromDto(dto);
        prePostRepository.save(prePost);
    }

    private PrePost createPrePostFromDto(PreRegisterPostDto dto) {
        String encodedImage = dto.noImg() ? null : fileCodecUtil.encode(dto.postImg());

        return new PrePost(
                dto.postName(),
                dto.postLat(),
                dto.postLon(),
                dto.type(),
                encodedImage,
                dto.noImg(),
                dto.registerId()
        );
    }
}
