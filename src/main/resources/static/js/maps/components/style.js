export function getStyle(src) {
    return new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [0.5, 1], // 아이콘 기준점을 하단 중앙으로 설정
            src: src, // 마커 이미지
            scale: 0.8 // 아이콘 크기 조정
        })
    });
}

export function getClusterStyle(feature) {
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