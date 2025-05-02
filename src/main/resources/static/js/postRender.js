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