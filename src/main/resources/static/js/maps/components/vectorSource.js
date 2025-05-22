export function getVectorSource(features = []) {
    return new ol.source.Vector({
        features: features,
    })
}