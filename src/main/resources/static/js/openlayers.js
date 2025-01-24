const getCurrentLocation = async () => {
    if (!navigator.geolocation) {
        console.warn("Geolocation은 이 브라우저에서 지원되지 않습니다. 기본 좌표를 사용합니다.");
        return { latitude: 0, longitude: 0 };
    }
    return new Promise((resolve) => {
        navigator.geolocation.getCurrentPosition(
            ({ coords: { latitude, longitude } }) => resolve({ latitude, longitude }),
            () => {
                console.warn("위치 정보를 가져오는 데 실패했습니다. 기본 좌표를 사용합니다.");
                resolve({ latitude: 0, longitude: 0 });
            }
        );
    });
};

const initMap = async () => {
    console.log("initMap");
    const { latitude, longitude } = await getCurrentLocation();
    console.log(latitude, longitude);

    const view = new ol.View({
        center: ol.proj.fromLonLat([longitude, latitude]),
        zoom: 20,
        maxZoom: 20,
    });

    const map = new ol.Map({
        target: 'map',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM(),
            })
        ],
        view: view,
    });

    map.on('moveend', () => {
        console.log("moveend");
        const zoom = view.getZoom();
        const center = ol.proj.toLonLat(view.getCenter());
        console.log(`Triggered after delay. Center: ${center}, Zoom: ${zoom}`);
        console.log(`Triggered after delay. Center: ${ol.proj.toLonLat(map.getView().getCenter())}, Zoom: ${ map.getView().getZoom()}`);
        renderMarker(map);
    });

    return map;
};

