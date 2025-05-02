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
        const imageSrc = post.presignedUrl || "/img/no-img.png";
        const typeIcon = post.type === "빅플" ? "/img/flower.png" : (post.type === "버섯" ? "/img/mushroom.png" : "/img/explorer.png");

        const cardHTML = `
            <div class="card border-left-success shadow h-100 post-card">
                <div class="card-body d-flex flex-column justify-content-between p-2 h-100">
                    <img src="${imageSrc}" 
                         class="post-img img-fluid mb-2" 
                         alt="${post.name || 'none'}"
                         onerror="this.onerror=null; this.src='/img/no-img.png';" />
                    <img src="${typeIcon}" class="post-type-img" alt="${post.type || 'type'}" />
                </div>
            </div>
        `;

        container.insertAdjacentHTML('beforeend', cardHTML);
    });
}