import {getCurrentLocation} from "./utils/location.js";
import {getView} from "./components/view.js";
import {getMap} from "./components/map.js";

export async function initialMap () {
    const { latitude, longitude } = await getCurrentLocation();
    const view = getView(longitude, latitude);
    return getMap(view);
}