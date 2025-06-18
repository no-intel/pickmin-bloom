function closeRegisterPostModal() {
    document.getElementById('post-name').value = '';
    document.getElementById('post-latitude').value = '';
    document.getElementById('post-longitude').value = '';
    document.getElementById('post-img').value = '';
    document.getElementById('no-img').checked = false;
    document.querySelectorAll('input[name="postType"]').forEach(el => el.checked = false);
}

async function registerPost() {
    const postName = document.getElementById('post-name').value;
    const postLat = document.getElementById('post-latitude').value;
    const postLon = document.getElementById('post-longitude').value;
    const postImg = document.getElementById('post-img').files[0];
    const noImg = document.getElementById('no-img').checked;
    const postType = document.querySelector('input[name="postType"]:checked')?.value;

    try {
        const formData = new FormData();
        formData.append('postName', postName);
        formData.append('postLat', postLat);
        formData.append('postLon', postLon);
        formData.append('noImg', noImg);
        formData.append('postType', postType);
        if (!noImg && postImg) {
            formData.append('postImg', postImg);
        }

        if (!validation(formData)){
            throw new Error("엽서 신청에 필요한 조건을 만족하지 못했습니다.");
        }

        toggleConfirmLoading(true);
        const response = await fetch('/pre-posts', {
            method: 'POST',
            body: formData,
            credentials: 'include'
        });

        if (!response.ok) {
            const result = await response.json();
            alert(result.message);
            return;
        }

        document.getElementById('close-register-btn').click();
        alert("등록 신청이 완료 됐습니다.")
        return null;

    } catch (error) {
        console.error('Error:', error);
    }finally {
        toggleConfirmLoading(false);
    }
}
function validation(formData) {
    if (!formData.get('postName') || formData.get('postName').trim() === "") {
        alert("엽서 이름은 필수입니다!")
        return false;
    }

    if (!formData.get('postLat') || isNaN(formData.get('postLat'))) {
        alert("위도를 입력하세요!")
        return false;
    }

    if (!formData.get('postLon') || isNaN(formData.get('postLon'))) {
        alert("경도를 입력하세요!")
        return false;
    }

    const postType = formData.get('postType');
    const validPostTypes = ["탐험", "버섯", "빅플"];
    if (!validPostTypes.includes(postType)) {
        alert("엽서 구분은 '탐험', '버섯', '빅플' 중 하나여야 합니다!")
        return false;
    }

    const noImg = formData.get('noImg') === "true" || formData.get('noImg') === true;
    if (!noImg) {
        const postImg = formData.get('postImg');
        if (!postImg || !(postImg instanceof File)) {
            alert("이미지를 업로드해 주세요!")
            return false;
        }

        const allowedExtensions = ["image/jpeg", "image/png", "image/jpg", "image/webp"];
        if (!allowedExtensions.includes(postImg.type)) {
            alert("JPEG, JPG, PNG, WEBP 형식의 이미지 파일만 가능합니다.")
            return false;
        }
    }
    return true;
}

function toggleConfirmLoading(show) {
    const loading = document.getElementById('register-loading-overlay');
    if (loading) {
        loading.classList.toggle('d-none', !show);
    }
}

// 모달이 닫힐 때 closeModal() 자동 호출
document.addEventListener('DOMContentLoaded', function () {
    const modalElement = document.getElementById('register-post-modal');
    if (modalElement) {
        $(modalElement).on('hidden.bs.modal', function () {
            closeRegisterPostModal(); // 모달 닫히면 입력값 및 스텝 초기화
        });
    }
});

$(document).on('hidden.bs.modal', '#register-post-modal', function () {
    closeRegisterPostModal();
});