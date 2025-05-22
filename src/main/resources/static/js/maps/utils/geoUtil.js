import { fromLonLat, toLonLat } from '/lib/openlayers/proj.js';

// EPSG:4326 -> EPSG:3857
export function getFromLonLat(lng, lat) {
    return fromLonLat([lng, lat]);
}

// EPSG:3857 -> EPSG:4326
export function getToLonLat(x, y) {
    return toLonLat([x, y]);
}
