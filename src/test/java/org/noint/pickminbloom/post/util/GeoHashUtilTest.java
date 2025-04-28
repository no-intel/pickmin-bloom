package org.noint.pickminbloom.post.util;

import org.junit.jupiter.api.Test;
import org.noint.pickminbloom.post.util.GeoHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeoHashUtilTest {
    @Autowired
    private GeoHashUtil geoHashUtil;

    @Test
    void encode() {
        String encode = geoHashUtil.encode(20.830025195282698, -156.9227092326623);
        String encode1 = geoHashUtil.encode(20.8268588, -156.9203274);
        System.out.println(encode);
        System.out.println(encode1);
    }
}