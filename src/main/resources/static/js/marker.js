// 마커 추가를 위한 Feature 생성
const showNearMarker = (map) => {
    const markerFeature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([127.136779, 37.449225])) // 핀 좌표
    });

    // 마커 스타일 설정
    markerFeature.setStyle(
        new ol.style.Style({
            image: new ol.style.Icon({
                anchor: [0.5, 1], // 아이콘 기준점을 하단 중앙으로 설정
                src: 'https://cdn-icons-png.flaticon.com/512/1483/1483336.png', // 마커 이미지
                scale: 0.05 // 아이콘 크기 조정
            })
        })
    );

    // 벡터 소스 및 레이어에 마커 추가
    const vectorSource = new ol.source.Vector({
        features: [markerFeature]
    });

    const markerLayer = new ol.layer.Vector({
        source: vectorSource
    });

    // 지도의 레이어에 마커 레이어 추가
    map.addLayer(markerLayer);

    console.log(map.getView().getCenter());
}

