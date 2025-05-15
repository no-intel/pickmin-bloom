package org.noint.pickminbloom.member.enums;

public enum Role {
    MASTER,
    ADMIN,
    USER
    ;

    public String toAuthority() {
        return "ROLE_" + this.name(); // Spring Security는 "ROLE_" 접두어 필요
    }
}