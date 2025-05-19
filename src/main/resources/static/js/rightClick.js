const setRightClickLayer = (x, y) => {
    let features = new ol.Feature({
        geometry: new ol.geom.Point([x, y]),
        eventAble: false,
    });

    const layer = new ol.layer.Vector({
        source: new ol.source.Vector({
            features: [features],
        }),
        style: new ol.style.Style({
            image: new ol.style.Icon({
                anchor: [0.5, 1], // 아이콘 기준점을 하단 중앙으로 설정
                src: 'img/right-pin.png', // 마커 이미지
                scale: 0.8 // 아이콘 크기 조정
            })
        })
    });
    layer.setZIndex(1000);
    return layer;
};

const rightClickMarkers = (map, coordinate) => {
    const coorPopup = document.getElementById('coor-popup');
    const [lon, lat] = ol.proj.toLonLat(coordinate);

    if (rightClickLayer) {
        map.removeLayer(rightClickLayer)
        coorPopup.style.display = 'none';
        coorOverlay.setPosition(undefined);
    }

    rightClickLayer = setRightClickLayer(coordinate[0], coordinate[1])
    map.addLayer(rightClickLayer);

    const isLogin = coorPopup.dataset.islogin === 'true';
    console.log(isLogin);
    coorPopup.innerHTML = `
        <span style="font-size: 15px;">${lat.toFixed(6)}, ${lon.toFixed(6)}</span>
        <br/>
        ${isLogin ? `
        <a class="btn btn-sm btn-primary" href="javascript:void(0)" onclick="registerPostWithCoor(${lat}, ${lon})">
            엽서 등록
        </a>` : ''}

    `;
    coorPopup.style.display = 'block';
    coorOverlay.setPosition(coordinate);
};

const registerPostWithCoor = (lat, lon) => {
    document.getElementById('post-latitude').value = lat.toFixed(6);
    document.getElementById('post-longitude').value = lon.toFixed(6);
    document.getElementById('register-post-btn').click();
}