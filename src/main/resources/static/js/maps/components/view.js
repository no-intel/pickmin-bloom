import { getFromLonLat } from '../utils/geoUtil.js';

export function getView(lng, lat) {
    return new ol.View({
        center: getFromLonLat(lng, lat),
        zoom: 15,
        maxZoom: 20,
    });
}