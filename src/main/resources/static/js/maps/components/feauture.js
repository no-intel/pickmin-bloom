export function getFeature(x, y, eventAble, postId, iconUrl) {
    const feature = new ol.Feature({
        geometry: new ol.geom.Point([y, x]),
        eventAble: false,
    });
    if (eventAble != null) {
        feature.set('eventAble', eventAble);
    }
    if (postId != null) {
        feature.set('postId', postId);
    }
    if (iconUrl != null) {
        feature.set('iconUrl', iconUrl);
    }
    return feature;
}