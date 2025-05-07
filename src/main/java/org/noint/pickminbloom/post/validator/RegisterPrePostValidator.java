package org.noint.pickminbloom.post.validator;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.exception.post.DuplicateCoordinateException;
import org.noint.pickminbloom.post.enums.PrePostStatus;
import org.noint.pickminbloom.post.repository.PostQuerydslRepository;
import org.noint.pickminbloom.post.repository.PrePostQuerydslRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterPrePostValidator {
    private final PrePostQuerydslRepository prePostQuerydslRepository;
    private final PostQuerydslRepository postQuerydslRepository;

    public void validateCoordinates(double lat, double lon) {
        if (postQuerydslRepository.findByCoor(lat, lon).isPresent()) {
            throw new DuplicateCoordinateException(lat + ", " + lon);
        }

        prePostQuerydslRepository.findByCoor(lat, lon)
                .filter(p -> p.getStatus() != PrePostStatus.REJECTED)
                .ifPresent(p -> {
                    throw new DuplicateCoordinateException(lat + ", " + lon);
                });
    }
}
