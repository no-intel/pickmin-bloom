let editStep = 1;
const editTotalSteps = 4;

function showEditPostStep(step) {
    document.querySelectorAll('.edit-step').forEach((el, idx) => {
        el.classList.toggle('d-none', idx + 1 !== step);
    });

    const prevBtn = document.querySelector('.modal-footer .btn-secondary');
    const nextBtn = document.querySelector('.modal-footer .btn-primary');

    // '이전' 버튼: 1스텝일 경우 자리만 차지 (숨김)
    prevBtn.style.visibility = (step === 1) ? 'hidden' : 'visible';

    // '다음' 버튼 텍스트 변경
    nextBtn.textContent = (step === editTotalSteps) ? '제출' : '다음';
}

function editPostNextStep() {
    if (editStep < editTotalSteps) {
        editStep++;
        showEditPostStep(editStep);
    } else {
        registerEditPost();
    }
}

function editPostPrevStep() {
    if (editStep > 1) {
        editStep--;
        showEditPostStep(editStep);
    }
}

function closeEditPostModal() {
    document.getElementById('edit-post-id').value = '';
    document.getElementById('edit-post-name').value = '';
    document.getElementById('edit-post-latitude').value = '';
    document.getElementById('edit-post-longitude').value = '';
    document.getElementById('edit-post-img').value = '';
    document.getElementById('edit-no-img').checked = false;
    document.getElementById('edit-post-type').value = 'null';
    document.getElementById('original-img').value = '';
    editStep = 1;
    showEditPostStep(editStep);
}

async function registerEditPost() {
    const editPostId = document.getElementById('edit-post-id').value;
    const editName = document.getElementById('edit-post-name').value;
    const editLatitude = document.getElementById('edit-post-latitude').value;
    const editLongitude = document.getElementById('edit-post-longitude').value;
    const editPostImg = document.getElementById('edit-post-img').files[0];
    const editNoImg = document.getElementById('edit-no-img').checked;
    const editType = document.getElementById('edit-post-type').value;

    try {
        const formData = new FormData();
        formData.append('editName', editName);
        formData.append('editLatitude', editLatitude);
        formData.append('editLongitude', editLongitude);
        formData.append('editType', editType);
        formData.append('editNoImg', editNoImg);
        if (!editNoImg) {
            formData.append('editPostImg', editPostImg);
        }

        if (!vaildation(formData)){
            throw new Error("엽서 신청에 필요한 조건을 만족하지 못했습니다.");
        }
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
        alert("등록 신청이 완료 됐습니다.")
        return null;

    } catch (error) {
        console.error('Error:', error);
    }
}
function vaildation(formData) {
    if (!formData.get('editName') || formData.get('editName').trim() === "") {
        alert("엽서 이름은 필수입니다!")
        return false;
    }

    if (!formData.get('editLatitude') || isNaN(formData.get('editLatitude'))) {
        alert("위도를 입력하세요!")
        return false;
    }

    if (!formData.get('editLongitude') || isNaN(formData.get('editLongitude'))) {
        alert("경도를 입력하세요!")
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

// 초기화
window.addEventListener('DOMContentLoaded', () => showEditPostStep(editStep));
// 모달이 닫힐 때 closeEditPostModal() 자동 호출
document.addEventListener('DOMContentLoaded', function () {
    const modalElement = document.getElementById('edit-post-modal');
    if (modalElement) {
        $(modalElement).on('hidden.bs.modal', function () {
            closeEditPostModal();
        });
    }

    // 스텝 초기화도 DOM 로드 시점에 실행
    showEditPostStep(editStep);
});


$(document).on('hidden.bs.modal', '#edit-post-modal', function () {
    closeEditPostModal();
});