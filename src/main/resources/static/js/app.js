import {initialMap} from "./maps/MapInitializer.js";
import {getOverlay} from "./maps/components/overlay.js";
import {getSelect} from "./maps/components/select.js";
import {getToLonLat, getFromLonLat} from "./maps/utils/geoUtil.js";
import {getFeature} from "./maps/components/feauture.js";
import {getVectorSource} from "./maps/components/vectorSource.js";
import {getClusterSource} from "./maps/components/clusterSource.js";
import {getVectorLayer, getVectorLayerWithCluster} from "./maps/components/vectorLayer.js";
import {getStyle} from "./maps/components/style.js";
import {ajax} from "./maps/utils/ajax.js";
import {rightClickMarkers} from "./maps/rightClick.js";
import {getCurrentLocation} from "./maps/utils/location.js";


let postList = [];
let map = null;
let isMakerClick = false;
let postPopup = null;
let coorPopup = null;
let postOverlay = null;
let coorOverlay = null;
let postLayer = null;
let standardLayer = null;
let rightClickLayer = null;
let clickSelect = null;
let hoverSelect = null;
let currentLatitude = null;
let currentLongitude = null;
let currentLayer = null;
let selectedFeatures = null;

window.onload = async () => {
    postPopup = document.getElementById('post-popup');
    coorPopup = document.getElementById('coor-popup');
    ({ latitude: currentLatitude, longitude: currentLongitude } = await getCurrentLocation());

    map = await initialMap(currentLatitude, currentLongitude);

    postOverlay = getOverlay(postPopup,'center-left',[10, 0]);
    coorOverlay = getOverlay(coorPopup,'center-left',[10, 0]);
    clickSelect = getSelect(ol.events.condition.click);
    hoverSelect = getSelect(ol.events.condition.pointerMove);

    map.addOverlay(postOverlay);
    map.addOverlay(coorOverlay);
    map.addInteraction(clickSelect);
    map.addInteraction(hoverSelect);

    // popup이 떠있을 때 동작
    clickSelect.on('select', (e) => {
        selectedFeatures = clickSelect.getFeatures();
        selectedFeatures.clear();

        const feature = e.selected[0];
        if (feature) {
            selectedFeatures.push(feature);   // 선택 표시 유지
        }

        handleSelection(feature);
    })

    map.on('singleclick', async (e) => {
        closeCoorOverlay();

        if(isMakerClick){
            isMakerClick = false;
            return;
        }

        if(!map.hasFeatureAtPixel(e.pixel)){
            postPopup.style.display = 'none';
            postOverlay.setPosition(undefined);

            const view = map.getView();
            const zoom = view.getZoom();
            const coordinate = e.coordinate; // EPSG:3857 x,y 좌료
            view.setCenter(coordinate);

            const extent = view.calculateExtent(map.getSize());
            const [minLongitude, minLatitude, maxLongitude, maxLatitude] = ol.proj.transformExtent(extent, 'EPSG:3857', 'EPSG:4326');
            const [longitude, latitude] = getToLonLat(coordinate[1], coordinate[0]);

            ajax({
                url: '/posts',
                method: 'GET',
                params: {
                    minLongitude,
                    minLatitude,
                    maxLongitude,
                    maxLatitude,
                    longitude,
                    latitude
                },
                success: (posts) => {
                    setMarkers(map, posts, coordinate[1], coordinate[0]);
                    createPostsImgCard(posts)
                    postList = posts;
                },
                error: (xhr) => {
                    console.log(xhr);
                }
            })
        }
    })

    map.getViewport().addEventListener('contextmenu', function (event) {
        event.preventDefault(); // 기본 우클릭 메뉴 방지

        postPopup.style.display = 'none';
        postOverlay.setPosition(undefined);

        closeCoorOverlay();

        // 1. 클릭한 화면상의 픽셀 위치
        const pixel = map.getEventPixel(event);

        // 2. 픽셀을 지도 좌표로 변환
        const coordinate = map.getCoordinateFromPixel(pixel);

        rightClickLayer = rightClickMarkers(coorPopup, coordinate)
        coorOverlay.setPosition(coordinate);
        map.addLayer(rightClickLayer);
    });

    setCurrentLocationEvent();
}

const setMarkers = (map, posts, x, y) => {
    if (standardLayer) {
        map.removeLayer(standardLayer)
    }

    const standardFeature = getFeature(x, y);
    const vectorSource = getVectorSource([standardFeature]);
    const standardStyle = getStyle('img/pin.png');
    standardLayer = getVectorLayer(vectorSource, standardStyle);

    map.addLayer(standardLayer);

    if (rightClickLayer) {
        map.removeLayer(rightClickLayer);
    }

    // 기존 마커 레이어가 있으면 제거
    if (postLayer) {
        map.removeLayer(postLayer);
    }

    if (posts.length < 1) {return}

    const markerFeatures = posts.map(post => createPostFeature(post));
    postLayer = getPostLayer(markerFeatures);
    map.addLayer(postLayer);
};

function createPostFeature(post) {
    const [lon, lat] = getFromLonLat(post.longitude, post.latitude);
    const icon = post.type === "빅플" ? "img/flower.png" : (post.type === "버섯" ? "img/mushroom.png" : "img/explorer.png" );

    return getFeature(lat, lon, false, post.postId, icon);
}

function getPostLayer(features) {
    const vectorSource = getVectorSource(features);
    const clusterSource = getClusterSource(vectorSource);
    return getVectorLayerWithCluster(clusterSource);
}

const createPostsImgCard = (posts) => {
    const container = document.getElementById("post-container");
    container.innerHTML = "";

    posts.forEach(post => {
        const imageSrc = post.downloadUrl || "/img/no-img.png";
        const typeIcon = post.type === "빅플" ? "/img/flower.png" : (post.type === "버섯" ? "/img/mushroom.png" : "/img/explorer.png");

        const card = document.createElement('div');
        card.className = 'card border-left-success shadow h-100 post-card';
        card.dataset.postId = post.postId;

        card.innerHTML = `
            <div class="card-body d-flex flex-column justify-content-between p-2 h-100">
                <img src="${imageSrc}" 
                     class="post-img img-fluid mb-2" 
                     alt="${post.name || 'none'}"
                     onerror="this.onerror=null; this.src='/img/404-img.png';" />
                <img src="${typeIcon}" class="post-type-img" alt="${post.type || 'type'}" />
            </div>
        `;

        card.addEventListener('click', () => {
            const targetPostId = card.dataset.postId;

            // 👉 postVector 레이어 찾기
            const vectorLayer = map.getLayers().getArray()
                .find(l => l.get('name') === 'postVector');
            if (!vectorLayer) return;

            const clusterFeatures = vectorLayer.getSource().getFeatures();

            // 👉 클러스터 내부에서 postId로 feature 탐색
            let matchedFeature = null;
            for (const cluster of clusterFeatures) {
                const innerFeatures = cluster.get('features');
                for (const f of innerFeatures) {
                    if (String(f.get('postId')) === String(targetPostId)) {
                        matchedFeature = f;
                        break;
                    }
                }
                if (matchedFeature) break;
            }

            if (!matchedFeature) {
                console.warn(`해당 postId의 feature 없음: ${targetPostId}`);
                return;
            }

            clickSelect.getFeatures().clear();
            clickSelect.getFeatures().push(matchedFeature);
            handleSelection(matchedFeature);
        });

        // 3) 컨테이너에 붙이기
        container.appendChild(card);
    });
}

function handleSelection(feature) {
    closeCoorOverlay();

    // 빈 맵누르면 post popup 끄기
    if (!feature) {
        postPopup.style.display = 'none';
        postOverlay.setPosition(undefined);
        return;
    }

    // post 마커 눌렀을 때 동작

    // ✅ 클러스터 feature인지 확인
    const isCluster = Array.isArray(feature.get('features'));
    const targetFeature = isCluster ? feature.get('features')[0] : feature;
    const coord = feature.getGeometry().getCoordinates();
    const postId = targetFeature.get('postId');
    const post = postList.find(post => post.postId === postId);

    postPopup.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <strong>${post.name}</strong>
            <button class="post-popup-close-btn" id="post-popup-close-btn" title="닫기">×</button>
        </div>
        <img src="${post.downloadUrl || "/img/no-img.png"}" onerror="this.onerror=null; this.src='/img/404-img.png';" style="max-width: 100%; height: auto;"><br>
        위치복사: <button class="btn btn-sm btn-light" onclick="copyToClipboard('${post.latitude}, ${post.longitude}')" title="위치 복사">
                <i class="fas fa-copy"></i>
            </button><br>
        엽서구분 : ${post.type}<br>
        <button class="btn btn-primary post-edit-btn" id="post-edit-btn" title="수정" data-toggle="modal" data-target="#edit-post-modal">수정</button>
    `;
    document.getElementById('post-edit-btn').addEventListener('click', () => requestEditPost(post))
    document.getElementById('post-popup-close-btn').addEventListener('click', () => closePostOverlay())
    postPopup.style.display = 'block';
    postOverlay.setPosition(coord);
    isMakerClick = true;
    map.getView().setCenter(coord);
}

const requestEditPost = (post) => {
    document.getElementById('edit-post-id').value = post.postId;
    document.getElementById('edit-post-name').value = post.name;
    document.getElementById('edit-post-latitude').value = post.latitude;
    document.getElementById('edit-post-longitude').value = post.longitude;
    document.querySelector(`input[name="edit-post-type"][value="${post.type}"]`).checked = true;
    if (post.downloadUrl) {
        document.getElementById('original-img').src = post.downloadUrl
    }else {
        document.getElementById('original-img').src = "/img/no-img.png";
    }
}

function setCurrentLocationEvent() {
    const btn = document.getElementById('current-location-btn');
    btn.addEventListener('click', () => {
        if (currentLayer){
            map.removeLayer(currentLayer);
        }

        const view = map.getView();
        const [y, x] = getFromLonLat(currentLongitude, currentLatitude);
        view.setCenter([y, x]);

        const currentFeature = getFeature(x, y);
        const vectorSource = getVectorSource([currentFeature]);
        const currentStyle = getStyle('img/standing.png');
        currentLayer = getVectorLayer(vectorSource, currentStyle);

        map.addLayer(currentLayer);
    });
}

function closePostOverlay() {
    postPopup.style.display = 'none';
    postOverlay.setPosition(undefined);
    selectedFeatures.clear();
}

function closeCoorOverlay() {
    if (rightClickLayer) {
        map.removeLayer(rightClickLayer)
        coorPopup.style.display = 'none';
        coorOverlay.setPosition(undefined);
    }
}