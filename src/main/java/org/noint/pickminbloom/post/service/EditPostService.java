package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.s3.S3TaskException;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.enums.PostType;
import org.noint.pickminbloom.post.event.UpdatePostEditRequestStatus;
import org.noint.pickminbloom.post.util.FileCodecUtil;
import org.noint.pickminbloom.post.util.GeoHashUtil;
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
    private final GeoHashUtil geoHashUtil;
    private final S3util s3Util;
    private final FileCodecUtil fileCodecUtil;

    @TransactionalEventListener(classes = UpdatePostEditRequestStatus.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void registerPost(UpdatePostEditRequestStatus event) {
        log.info("EVENT - Edit post: {}", event);
        Post post = getPostService.getPost(event.postId());
        String editGeohash = geoHashUtil.encode(event.editLatitude(), event.editLongitude());
        boolean isEditNoImg = event.editImg() == null;

        // 이미지 변경있으면 파일 삭제 후 업로드
        if (!isEditNoImg) {
            MultipartFile editImg = fileCodecUtil.decodeToMultipartFile(editGeohash, event.editImg());
            s3Util.deleteFile(post.getType() + "-" + post.getGeohash());
            s3Util.uploadFile(event.editType() + "-" + editGeohash, editImg);
        } else if (isNeedRename(post, event)){
            s3Util.renameFile(
                    post.getType() + "-" + post.getGeohash(),
                    event.editType() + "-" + editGeohash
            );
        }
        post.editPost(
                event.editName(),
                editGeohash,
                event.editLatitude(),
                event.editLongitude(),
                event.editType()
        );
    }

    private boolean isNeedRename(Post post, UpdatePostEditRequestStatus event) {
        return Double.compare(post.getLatitude(), event.editLatitude()) != 0 ||        // 경도 변경 체크
                Double.compare(post.getLongitude(), event.editLongitude()) != 0 ||     // 위도 변경 체크
                post.getType() != event.editType();                                    // 타입 변경 체크
    }
}
