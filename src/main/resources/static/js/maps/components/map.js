export function getMap(view) {
    return new ol.Map({
        target: 'map',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM(),
            })
        ],
        view: view
    });
}