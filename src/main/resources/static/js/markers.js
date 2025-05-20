const createMarkerFeature = (post) => {
    const lonLat = ol.proj.fromLonLat([post.longitude, post.latitude]);
    const icon = post.type === "빅플" ? "img/flower.png" : (post.type === "버섯" ? "img/mushroom.png" : "img/explorer.png" );

    return new ol.Feature({
        geometry: new ol.geom.Point(lonLat),
        iconUrl: icon,
        eventAble: false,
        postId: post.postId,
    });
};

// 마커 레이어 생성
const setMarkerLayer = (features) => {
    const vectorSource = new ol.source.Vector({
        features: features,
    });

    const clusterSource = new ol.source.Cluster({
        distance: 40,
        source: vectorSource,
    })

    return new ol.layer.Vector({
        source: clusterSource,
        name: 'postVector',
        style: function (feature) {
            const features = feature.get('features');
            const size = features.length;

            if (size === 1) {
                return new ol.style.Style({
                    image: new ol.style.Icon({
                        anchor: [0.5, 1], // 아이콘 기준점을 하단 중앙으로 설정
                        src: features[0].get('iconUrl'), // 마커 이미지
                        scale: 0.8 // 아이콘 크기 조정
                    })
                });  // 개별 마커의 스타일 사용
            }

            return new ol.style.Style({
                image: new ol.style.Circle({
                    radius: 12 + Math.min(size, 10),
                    stroke: new ol.style.Stroke({
                        color: '#fff',
                        width: 2
                    }),
                    fill: new ol.style.Fill({
                        color: '#3399CC'
                    })
                }),
                text: new ol.style.Text({
                    text: size > 1 ? size.toString() : '',
                    scale: 1.2,
                    offsetY: 1,
                    fill: new ol.style.Fill({
                        color: '#fff'
                    }),
                    stroke: new ol.style.Stroke({
                        color: '#000',
                        width: 3
                    })
                })
            });
        }
    });
};

const setStandardLayer = (x, y) => {
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
                src: 'img/pin.png', // 마커 이미지
                scale: 0.8 // 아이콘 크기 조정
            })
        })
    });
    layer.setZIndex(1000);
    return layer;
};

const markers = (map, posts, x, y) => {
    if (standardLayer) {
        map.removeLayer(standardLayer)
    }
    standardLayer = setStandardLayer(x, y)
    map.addLayer(standardLayer);

    // 기존 마커 레이어가 있으면 제거
    if (markerLayer) {
        map.removeLayer(markerLayer);
    }

    if (rightClickLayer) {
        map.removeLayer(rightClickLayer);
    }

    if (posts.length < 1) {return}

    const markerFeatures = posts.map(post => createMarkerFeature(post));
    markerLayer = setMarkerLayer(markerFeatures);
    map.addLayer(markerLayer);

};
