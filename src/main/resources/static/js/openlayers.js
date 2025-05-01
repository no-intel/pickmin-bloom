const getCurrentLocation = async () => {
    if (!navigator.geolocation) {
        console.warn("Geolocation은 이 브라우저에서 지원되지 않습니다. 기본 좌표를 사용합니다.");
        return { latitude: 0, longitude: 0 };
    }
    return new Promise((resolve) => {
        navigator.geolocation.getCurrentPosition(
            ({ coords: { latitude, longitude } }) => resolve({ latitude, longitude }),
            () => {
                console.warn("위치 정보를 가져오는 데 실패했습니다. 기본 좌표를 사용합니다.");
                resolve({ latitude: 0, longitude: 0 });
            }
        );
    });
};

const rendMakers = async () => {
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

    map.on('click', async (e) => {
        const view = map.getView();
        const zoom = view.getZoom();
        const coordinate = e.coordinate; // EPSG:3857 기준 x,y 좌료
        view.setCenter(coordinate);

        const extent = view.calculateExtent(map.getSize());
        const [minLongitude, minLatitude, maxLongitude, maxLatitude] = ol.proj.transformExtent(extent, 'EPSG:3857', 'EPSG:4326');
        const [longitude, latitude] = new ol.proj.transform(coordinate, 'EPSG:3857', 'EPSG:4326');

        const posts = await findPosts(minLongitude, minLatitude, maxLongitude, maxLatitude, longitude, latitude);
        markers(map, posts, coordinate[0], coordinate[1]);
        rendPostsImgCard(posts)
    });

    return map;
};


const findPosts = async (minLongitude, minLatitude, maxLongitude, maxLatitude, longitude, latitude) => {
    try {
        const params = new URLSearchParams({minLongitude, minLatitude, maxLongitude, maxLatitude, longitude, latitude});
        const response = await fetch(`/posts?${params.toString()}`);

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        return await response.json();

    } catch (error) {
        console.error('Error:', error);
    }
};

const rendPostsImgCard = (posts) => {
    document.getElementById("post-container").innerHTML = "";
    posts.forEach(post => {
        const outerDiv = document.createElement("div");
        outerDiv.className = "card border-left-success shadow h-100 post-card";

        const cardBody = document.createElement("div");
        cardBody.className = "card-body d-flex flex-column justify-content-between p-2 h-100";

        // 메인 이미지
        const postImg = document.createElement("img");
        postImg.className = "post-img";
        postImg.src = post.presignedUrl || "/img/no-img.png";
        postImg.alt = post.name || "none";
        postImg.className = "img-fluid mb-2";
        postImg.onerror = function () {
            this.onerror = null;
            this.src = "/img/no-img.png";
        };

        // 타입 아이콘 이미지
        const typeImg = document.createElement("img");
        typeImg.className = "post-type-img";
        typeImg.src = post.type === "빅플"
            ? "/img/flower.png"
            : post.type === "버섯"
                ? "/img/mushroom.png"
                : "/img/explorer.png";
        typeImg.alt = post.type || "type";

        // 조립
        cardBody.appendChild(postImg);
        cardBody.appendChild(typeImg);
        outerDiv.appendChild(cardBody);

        document.getElementById("post-container").appendChild(outerDiv);
    })
}