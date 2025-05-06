package org.noint.pickminbloom.post.enums;

import org.noint.pickminbloom.exception.post.NotMatchPostTypeException;

import java.util.Arrays;

public enum PostType {
    EXPLORER("탐험"),
    MUSHROOM("버섯"),
    FLOWER("빅플"),
    ;

    private final String typeKor;

    PostType(String typeKor) {
        this.typeKor = typeKor;
    }

    public static PostType fromString(String typeKor) {
        return Arrays.stream(PostType.values())
                .filter(postType -> postType.typeKor.equals(typeKor))
                .findFirst()
                .orElseThrow(() -> new NotMatchPostTypeException(typeKor));
    }

}
