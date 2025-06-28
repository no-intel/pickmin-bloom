function closeEditPostModal() {
    document.getElementById('edit-post-id').value = '';
    document.getElementById('edit-post-name').value = '';
    document.getElementById('edit-post-img').value = '';
    document.getElementById('edit-no-img').checked = false;
    document.getElementById('original-img').value = '';
    document.querySelectorAll('input[name="edit-post-type"]').forEach(el => el.checked = false);
}

async function registerEditPost() {
    const editPostId = document.getElementById('edit-post-id').value;
    const editName = document.getElementById('edit-post-name').value;
    const editPostImg = document.getElementById('edit-post-img').files[0];
    const editNoImg = document.getElementById('edit-no-img').checked;
    const editType = document.querySelector('input[name="edit-post-type"]:checked')?.value;

    try {
        const formData = new FormData();
        formData.append('editName', editName);
        formData.append('editType', editType);
        formData.append('editNoImg', editNoImg);
        if (!editNoImg) {
            formData.append('editPostImg', editPostImg);
        }

        if (!vaildation(formData)){
            throw new Error("엽서 신청에 필요한 조건을 만족하지 못했습니다.");
        }
        toggleEditLoading(true);

        const response = await fetch(`/posts/${editPostId}`, {
            method: 'PUT',
            body: formData,
            credentials: 'include'
        });

        if (!response.ok) {
            const result = await response.json();
            alert(result.message);
            return;
        }

        document.getElementById('close-edit-btn').click();
        alert("수정 신청이 완료 됐습니다.")
        return null;

    } catch (error) {
        console.error('Error:', error);
    } finally {
        toggleEditLoading(false)
    }
}
function vaildation(formData) {
    if (!formData.get('editName') || formData.get('editName').trim() === "") {
        alert("엽서 이름은 필수입니다!")
        return false;
    }

    const editType = formData.get('editType');
    const validPostTypes = ["탐험", "버섯", "빅플"];
    if (!validPostTypes.includes(editType)) {
        alert("엽서 구분은 '탐험', '버섯', '빅플' 중 하나여야 합니다!")
        return false;
    }

    const editNoImg = formData.get('editNoImg') === "true" || false;
    if (!editNoImg) {
        const editPostImg = formData.get('editPostImg');
        if (!editPostImg || !(editPostImg instanceof File)) {
            alert("이미지를 업로드해 주세요!")
            return false;
        }

        const allowedExtensions = ["image/jpeg", "image/png", "image/jpg", "image/webp"];
        if (!allowedExtensions.includes(editPostImg.type)) {
            alert("JPEG, JPG, PNG, WEBP 형식의 이미지 파일만 가능합니다.")
            return false;
        }
    }
    return true;
}

function toggleEditLoading(show) {
    const loading = document.getElementById('edit-loading-overlay');
    if (loading) {
        loading.classList.toggle('d-none', !show);
    }
}

// 모달이 닫힐 때 closeEditPostModal() 자동 호출
document.addEventListener('DOMContentLoaded', function () {
    const modalElement = document.getElementById('edit-post-modal');
    if (modalElement) {
        $(modalElement).on('hidden.bs.modal', function () {
            closeEditPostModal();
        });
    }
});


$(document).on('hidden.bs.modal', '#edit-post-modal', function () {
    closeEditPostModal();
});