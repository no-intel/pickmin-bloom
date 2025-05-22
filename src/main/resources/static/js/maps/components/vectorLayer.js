import {getStyle, getClusterStyle} from "./style.js";

export function getVectorLayer(source, style) {
    return new ol.layer.Vector({
        source: source,
        style: style,
        zIndex: 1000
    });
}

export function getVectorLayerWithCluster(clusterSource) {
    return new ol.layer.Vector({
        source: clusterSource,
        name: 'postVector',
        style: function (feature) {
            const features = feature.get('features');
            const size = features.length;

            if (size === 1) {
                return getStyle(features[0].get('iconUrl'));
            }

            return getClusterStyle(features);
        }
    });
}