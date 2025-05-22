export function getClusterSource(source) {
    return new ol.source.Cluster({
        source: source,
        distance: 40,
    });
}