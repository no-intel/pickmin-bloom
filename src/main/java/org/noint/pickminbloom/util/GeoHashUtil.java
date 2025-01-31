package org.noint.pickminbloom.util;

import ch.hsr.geohash.GeoHash;
import org.springframework.stereotype.Component;

@Component
public class GeoHashUtil {

    // Geohash 변환
    public String encode(double latitude, double longitude) {
        return GeoHash.withCharacterPrecision(latitude, longitude, 12).toBase32();
    }

    // Geohash 복호화 (좌표로 변환)
    public double[] decode(String geohash) {
        GeoHash geoHash = GeoHash.fromGeohashString(geohash);
        return new double[]{
                geoHash.getBoundingBoxCenter().getLatitude(),
                geoHash.getBoundingBoxCenter().getLongitude()
        };
    }
}