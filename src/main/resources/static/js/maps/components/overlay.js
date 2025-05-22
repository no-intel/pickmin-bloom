export function getOverlay(element, positon, offset) {
    return new ol.Overlay({
        element: element,
        positioning: positon, // 마커를 Overlay의 어디에 위치 시킬지
        stopEvent: true,
        offset: offset // 마커와의 간격
    });
}