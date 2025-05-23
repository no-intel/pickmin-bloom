import {getToLonLat} from "./utils/geoUtil.js";
import {getFeature} from "./components/feauture.js";
import {getVectorLayer} from "./components/vectorLayer.js"
import {getStyle} from "./components/style.js";
import {getVectorSource} from "./components/vectorSource.js";

export const rightClickMarkers = (coorPopup, coordinate) => {
    const [lon, lat] = getToLonLat(coordinate[0], coordinate[1]);

    const isLogin = coorPopup.dataset.islogin === 'true';
    coorPopup.innerHTML = `
        <span style="font-size: 15px;">${lat.toFixed(6)}, ${lon.toFixed(6)}</span>
        <br/>
    `;

    if (isLogin) {
        const anchor = document.createElement('a');
        anchor.className = 'btn btn-sm btn-primary';
        anchor.href = 'javascript:void(0)';
        anchor.textContent = '엽서 등록';
        anchor.addEventListener('click', () => registerPostWithCoor(lat, lon));
        coorPopup.appendChild(anchor);
    }
    coorPopup.style.display = 'block';
    return setRightClickLayer(coordinate[1], coordinate[0]);
};

const setRightClickLayer = (x, y) => {
    const feature = getFeature(x, y);
    const style = getStyle('img/right-pin.png');
    const vectorSource = getVectorSource([feature]);
    return getVectorLayer(vectorSource, style);
};

export const registerPostWithCoor = (lat, lon) => {
    document.getElementById('post-latitude').value = lat.toFixed(6);
    document.getElementById('post-longitude').value = lon.toFixed(6);
    document.getElementById('register-post-btn').click();
}