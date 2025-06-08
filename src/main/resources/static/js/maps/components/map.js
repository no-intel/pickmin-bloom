import { getControl } from './Control.js'

export function getMap(view, currentLocationElement) {
    return new ol.Map({
        target: 'map',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM(),
            })
        ],
        view: view,
        controls: [
            new ol.control.Zoom(),
            new ol.control.Attribution(),
            new ol.control.Rotate(),
            getControl(currentLocationElement)
    ]
    });
}