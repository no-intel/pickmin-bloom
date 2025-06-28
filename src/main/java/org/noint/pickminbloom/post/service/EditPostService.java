package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.enums.PostType;
import org.noint.pickminbloom.post.event.ConfirmPostEditRequest;
import org.noint.pickminbloom.post.util.FileCodecUtil;
import org.noint.pickminbloom.post.util.s3.S3util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EditPostService {
    private final GetPostService getPostService;
    private final S3util s3Util;
    private final FileCodecUtil fileCodecUtil;

    @TransactionalEventListener(classes = ConfirmPostEditRequest.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void registerPost(ConfirmPostEditRequest event) {
        log.info("EVENT - Edit post: {}", event);
        Post post = getPostService.getPost(event.postId());

        changeS3File(event, post);

        post.editPost(
                event.editName(),
                event.editType()
        );
    }

    // 이미지 변경있으면 파일 삭제 후 업로드
    private void changeS3File(ConfirmPostEditRequest event, Post post) {
        if (isChangedImg(event.editImg())) {
            MultipartFile editImg = fileCodecUtil.decodeToMultipartFile(post.getGeohash(), event.editImg());
            s3Util.deleteFile(post.getType() + "-" + post.getGeohash());
            s3Util.uploadFile(event.editType() + "-" + post.getGeohash(), editImg);
            return;
        }

        if (isChangedType(event.editType(), post.getType())) {
            s3Util.renameFile(
                    post.getType() + "-" + post.getGeohash(),
                    event.editType() + "-" + post.getGeohash()
            );
        }
    }

    private boolean isChangedImg(byte[] editImg){
        return editImg != null;
    }

    private boolean isChangedType(PostType editType, PostType pontType) {
        return editType != pontType;
    }
}
