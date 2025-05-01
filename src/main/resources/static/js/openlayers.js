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
        const [minX, minY, maxX, maxY] = ol.proj.transformExtent(extent, 'EPSG:3857', 'EPSG:4326');

        const posts = await findPosts(minX, minY, maxX, maxY);
        markers(map, posts, coordinate[0], coordinate[1]);
        // TODO : 사이드랜더링 후 포스트 가져올때 좌표기준으로 가까운'순으로 그럼 정렬도 가까운 순으로 될 듯.
        rendPostsImgCard(posts)
    });

    return map;
};


const findPosts = async (minX, minY, maxX, maxY) => {
    try {
        const response = await fetch(`/posts?minX=${minX}&minY=${minY}&maxX=${maxX}&maxY=${maxY}`);

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        console.log('Success:', data);
        return data;

    } catch (error) {
        console.error('Error:', error);
    }
};

const rendPostsImgCard = (posts) => {
    posts.forEach(post => {
        const outerDiv = document.createElement("div");
        const card = document.createElement("div");
        card.className = "card border-left-primary shadow h-100 py-2";

        const cardBody = document.createElement("div");
        cardBody.className = "card-body";

        const row = document.createElement("div");
        row.className = "row no-gutters align-items-center";

        const colText = document.createElement("div");
        colText.className = "col mr-2";

        const postLabel = document.createElement("div");
        postLabel.className = "h5 mb-0 font-weight-bold text-gray-800";

        const postImg = document.createElement("img");
        postImg.src = post.presignedUrl || "/img/no-img.png"; // 기본값
        postImg.className = "col-3";
        postImg.alt = posts.name;
        postImg.onerror = function () {
            this.onerror = null;
            this.src = "/img/no-img.png";
        };

        const typeLabel = document.createElement("div");
        typeLabel.className = "col-auto";

        const typeImg = document.createElement("img");
        typeImg.src = post.type === "빅플" ? "img/flower.png" : (post.type === "버섯" ? "img/mushroom.png" : "img/explorer.png"); // 기본값
        // typeImg.className = "col-3";
        typeImg.alt = posts.type;

        // 조립
        colText.appendChild(postLabel);
        postLabel.appendChild(postImg);
        typeLabel.appendChild(typeImg);

        row.appendChild(colText);
        row.appendChild(typeLabel);

        cardBody.appendChild(row);
        card.appendChild(cardBody);
        outerDiv.appendChild(card);
        document.getElementById("side-post-img").append(outerDiv);
    })
    // <div>
    //     <div className="card border-left-primary shadow h-100 py-2">
    //         <div className="card-body">
    //             <div className="row no-gutters align-items-center">
    //                 <div className="col mr-2">
    //                     <div className="text-xs font-weight-bold text-primary text-uppercase mb-1">
    //                         Earnings (Monthly)
    //                     </div>
    //                     <div className="h5 mb-0 font-weight-bold text-gray-800">$40,000</div>
    //                 </div>
    //                 <div className="col-auto">
    //                     <i className="fas fa-calendar fa-2x text-gray-300"></i>
    //                 </div>
    //             </div>
    //         </div>
    //     </div>
    // </div>
}