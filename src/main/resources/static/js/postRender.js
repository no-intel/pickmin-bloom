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
            handleFeatureSelection(matchedFeature);
        });

        // 3) 컨테이너에 붙이기
        container.appendChild(card);
    });
}