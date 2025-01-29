let markerLayer = null;

const createMarkerStyle = () => {
    return new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [0.5, 1], // 아이콘 기준점을 하단 중앙으로 설정
            src: 'https://cdn-icons-png.flaticon.com/512/1483/1483336.png', // 마커 이미지
            scale: 0.05 // 아이콘 크기 조정
        })
    });
};

const createMarkerFeature = (post, markerStyle) => {
    const markerFeature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([post.longitude, post.latitude]))
    });
    markerFeature.setStyle(markerStyle);

    return markerFeature;
};

// 마커 레이어 생성
const createMarkerLayer = (features) => {
    const vectorSource = new ol.source.Vector({
        features: features
    });

    return new ol.layer.Vector({
        source: vectorSource
    });
};

const renderMarker = (map, posts) => {
    console.log('renderMarker');

    // 기존 마커 레이어가 있으면 제거
    if (markerLayer) {
        map.removeLayer(markerLayer);
    }

    if (posts.length < 1) return;

    const markerStyle = createMarkerStyle();

    const markerFeatures = posts.map(post => createMarkerFeature(post, markerStyle));

    console.log('markerFeatures')
    console.log(markerFeatures)

    markerLayer = createMarkerLayer(markerFeatures);

    map.addLayer(markerLayer);
};
