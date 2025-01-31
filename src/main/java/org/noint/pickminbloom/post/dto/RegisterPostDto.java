package org.noint.pickminbloom.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPostDto {
    private String name;
    private String coordinates;
    private String type;
    private String location;
}
