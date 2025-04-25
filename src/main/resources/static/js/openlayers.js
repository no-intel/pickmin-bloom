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

const rendMakers = async () => {
    console.log("initMap");
    const { latitude, longitude } = await getCurrentLocation();
    console.log(latitude, longitude);

    const view = new ol.View({
        center: ol.proj.fromLonLat([longitude, latitude]),
        zoom: 15,
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

    map.on('click', async (e) => {
        const view = map.getView();
        const zoom = view.getZoom();
        const coordinate = e.coordinate; // EPSG:3857 기준 x,y 좌료
        const lonLat = ol.proj.toLonLat(coordinate); // 경도 위도

        let posts = await findPosts(lonLat[1], lonLat[0], zoom);
        markers(map, posts, coordinate[0], coordinate[1]);
        view.setCenter(coordinate);
    });

    return map;
};

const findPosts = async (latitude, longitude, zoomLevel) => {
    try {
        const response = await fetch(`/posts?latitude=${latitude}&longitude=${longitude}&zoomLevel=${Math.floor(zoomLevel)}`);

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        console.log('Success:', data);
        return data;

    } catch (error) {
        console.error('Error:', error);
    }
};
