let markerLayer = null;
let standardLayer = null;

const createMarkerFeature = (post) => {
    const lonLat = ol.proj.fromLonLat([post.longitude, post.latitude]);

    return new ol.Feature({
        geometry: new ol.geom.Point(lonLat),
    });
};

// 마커 레이어 생성
const setMarkerLayer = (features) => {
    const vectorSource = new ol.source.Vector({
        features: features
    });

    const clusterSource = new ol.source.Cluster({
        distance: 40,
        source: vectorSource
    })

    return new ol.layer.Vector({
        source: clusterSource,
        style: function (feature) {
            const size = feature.get('features').length;

            if (size === 1) {
                return new ol.style.Style({
                    image: new ol.style.Icon({
                        anchor: [0.5, 1], // 아이콘 기준점을 하단 중앙으로 설정
                        src: 'https://cdn-icons-png.flaticon.com/512/1483/1483336.png', // 마커 이미지
                        scale: 0.05 // 아이콘 크기 조정
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
    });

    const layer = new ol.layer.Vector({
        source: new ol.source.Vector({
            features: [features],
        }),
        style: new ol.style.Style({
            image: new ol.style.Icon({
                anchor: [0.5, 1], // 아이콘 기준점을 하단 중앙으로 설정
                src: 'https://cdn-icons-png.flaticon.com/512/1483/1483285.png', // 마커 이미지
                scale: 0.05 // 아이콘 크기 조정
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

    if (posts.length < 1) return;

    const markerFeatures = posts.map(post => createMarkerFeature(post));
    markerLayer = setMarkerLayer(markerFeatures);
    map.addLayer(markerLayer);

};
