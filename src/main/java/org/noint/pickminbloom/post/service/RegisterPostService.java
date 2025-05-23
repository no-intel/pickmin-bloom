package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.RegisterPostDto;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.event.UpdatePrePostStatus;
import org.noint.pickminbloom.post.repository.PostRepository;
import org.noint.pickminbloom.post.util.FileCodecUtil;
import org.noint.pickminbloom.post.util.GeoHashUtil;
import org.noint.pickminbloom.post.util.s3.S3util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterPostService {

    private final PostRepository postRepository;
    private final GeoHashUtil geoHashUtil;
    private final S3util s3Util;
    private final FileCodecUtil fileCodecUtil;

    @Transactional
    public void registerPost(RegisterPostDto dto) {
//        Post post = new Post(dto.geohash(),
//                dto.name(),
//                dto.latitude(),
//                dto.longitude(),
//                PostType.MUSHROOM
////                dto.type()
//        );
//        postRepository.save(post);
//        s3Util.uploadFile(dto.geohash(), dto.image());
    }

    @TransactionalEventListener(classes = UpdatePrePostStatus.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void registerPost(UpdatePrePostStatus event) {
        log.info("EVENT - Register post: {}", event);
        String geohash = geoHashUtil.encode(event.latitude(), event.longitude());
        MultipartFile img = fileCodecUtil.decodeToMultipartFile(geohash, event.img());
        s3Util.uploadFile(event.type() + "-" + geohash, img);
        Post post = new Post(
                geohash,
                event.name(),
                event.latitude(),
                event.longitude(),
                event.type(),
                event.requester(),
                event.noImg(),
                event.confirmedBy()
        );
        postRepository.save(post);
    }
}
