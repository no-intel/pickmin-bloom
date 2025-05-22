const getCurrentLocation = async () => {
    if (!navigator.geolocation) {
        console.warn("Geolocation은 이 브라우저에서 지원되지 않습니다. 기본 좌표를 사용합니다.");
        return { latitude: 37.5665, longitude: 126.9780 }; // 서울시청
    }
    return new Promise((resolve) => {
        navigator.geolocation.getCurrentPosition(
            ({ coords: { latitude, longitude } }) => resolve({ latitude, longitude }),
            () => {
                console.warn("위치 정보를 가져오는 데 실패했습니다. 기본 좌표를 사용합니다.");
                resolve({ latitude: 37.5665, longitude: 126.9780 }); // 서울시청
            }
        );
    });
};

const hoverSelect = new ol.interaction.Select({
    condition: ol.events.condition.pointerMove,
    filter: function(feature) {
        // features라는 속성이 있는 경우는 클러스터이므로 제외
        const clustered = feature.get('features');
        if (feature.get('eventAble') === false) return false;
        return !clustered || clustered.length === 1;
    },
    style: (feature) => {
        const clustered = feature.get('features');
        const single = clustered[0];
        return new ol.style.Style({
            image: new ol.style.Icon({
                anchor: [0.5, 1],
                src: single.get('iconUrl'),
                scale: 1.2
            }),
            zIndex: 10
        });
    }
})

const clickSelect = new ol.interaction.Select({
    condition: ol.events.condition.click,
    filter: function(feature) {
        // features라는 속성이 있는 경우는 클러스터이므로 제외
        const clustered = feature.get('features');
        if (feature.get('eventAble') === false) return false;
        return !clustered || clustered.length === 1;
    },
    style: (feature) => {
        const clustered = feature.get('features');
        const single = clustered[0];
        return new ol.style.Style({
            image: new ol.style.Icon({
                anchor: [0.5, 1],
                src: single.get('iconUrl'),
                scale: 1.2
            }),
            zIndex: 10
        });
    }
})

const postOverlay = new ol.Overlay({
    element: document.getElementById('post-popup'),
    positioning: 'center-left',
    stopEvent: true,
    offset: [10, 0] // 마커 위 약간 띄우기
});

const coorOverlay = new ol.Overlay({
    element: document.getElementById('coor-popup'),
    positioning: 'center-left',
    stopEvent: true,
    offset: [10, 0] // 마커 위 약간 띄우기
});

const rendMap = async () => {
    const { latitude, longitude } = await getCurrentLocation();

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

    map.addInteraction(hoverSelect);
    map.addInteraction(clickSelect);
    map.addOverlay(postOverlay);
    map.addOverlay(coorOverlay);

    return map;
};

function handleFeatureSelection(feature) {
    closeCoorOverlay();

    const postPopup = document.getElementById('post-popup');
    if (!feature) {
        console.log('feature가 없습니다.');
        postPopup.style.display = 'none';
        postOverlay.setPosition(undefined);
        return;
    }

    // ✅ 클러스터 feature인지 확인
    const isCluster = Array.isArray(feature.get('features'));
    const targetFeature = isCluster ? feature.get('features')[0] : feature;
    const coord = feature.getGeometry().getCoordinates();
    const postId = targetFeature.get('postId');
    const post = posts.find(post => post.postId === postId);

    postPopup.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <strong>${post.name}</strong>
            <button class="popup-close-btn" onclick="closePopup()" title="닫기">×</button>
        </div>
        <img src="${post.downloadUrl || "/img/no-img.png"}" onerror="this.onerror=null; this.src='/img/404-img.png';" style="max-width: 100%; height: auto;"><br>
        위치복사: <button class="btn btn-sm btn-light" onclick="copyToClipboard('${post.latitude}, ${post.longitude}')" title="위치 복사">
                <i class="fas fa-copy"></i>
            </button><br>
        엽서구분 : ${post.type}<br>
        <button class="btn btn-primary post-edit-btn" onclick='requestEditPost(${JSON.stringify(post)})' title="수정" data-toggle="modal" data-target="#edit-post-modal">수정</button>
    `;
    postPopup.style.display = 'block';
    postOverlay.setPosition(coord);
    isOnlyViewMoving = true;
    map.getView().setCenter(coord);
}

function closePopup() {
    const popup = document.getElementById('post-popup');
    popup.innerHTML = '';
    popup.style.display = 'none';

    if (typeof postOverlay !== 'undefined') {
        postOverlay.setPosition(undefined);  // 팝업 좌표 제거
    }

    if (typeof clickSelect !== 'undefined') {
        clickSelect.getFeatures().clear();
    }
}

function closeCoorOverlay() {
    const coorPopup = document.getElementById('coor-popup');
    coorPopup.style.display = 'none';
    coorOverlay.setPosition(undefined);
}