import {getView} from "./components/view.js";
import {getMap} from "./components/map.js";

export async function initialMap (latitude, longitude) {
    const view = getView(longitude, latitude);
    const currentLocationElement = getCurrentLocationElement();
    return getMap(view, currentLocationElement);
}

function getCurrentLocationElement() {
    const button = document.createElement('button');
    button.id = 'current-location-btn'
    button.innerHTML = `
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="20" height="20" fill="black">
                    <path d="M12 8a4 4 0 1 1 0 8 4 4 0 0 1 0-8zm10 3h-1.07a8.002 8.002 0 0 0-6.93-6.93V3a1 1 0 1 0-2 0v1.07a8.002 8.002 0 0 0-6.93 6.93H2a1 1 0 1 0 0 2h1.07a8.002 8.002 0 0 0 6.93 6.93V21a1 1 0 1 0 2 0v-1.07a8.002 8.002 0 0 0 6.93-6.93H22a1 1 0 1 0 0-2zM12 18a6 6 0 1 1 0-12 6 6 0 0 1 0 12z"/>
                  </svg>
                `;
    button.title = '현위치로 이동';

    const element = document.createElement('div');
    element.className = 'ol-unselectable ol-control custom-location-control';
    element.appendChild(button);

    return element;
}