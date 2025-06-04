document.addEventListener("DOMContentLoaded", () => {
    // 마우스 휠로 좌우 움직이기
    const scroller = document.querySelector('[data-simplebar] .simplebar-content-wrapper');
    if (scroller) {
        scroller.addEventListener('wheel', (e) => {
            // shift 키를 누르지 않아도 가로 스크롤 처리
            if (Math.abs(e.deltaY) > Math.abs(e.deltaX)) {
                e.preventDefault();
                scroller.scrollLeft += e.deltaY;
            }
        }, { passive: false });
    }
    // 클릭으로 좌우 움직이기
    const wrapper = document.querySelector('[data-simplebar] .simplebar-content-wrapper');
    let isDragging = false;
    let startX;
    let scrollLeft;

    if (wrapper) {
        wrapper.addEventListener('mousedown', (e) => {
            isDragging = true;
            wrapper.classList.add('dragging');
            startX = e.pageX - wrapper.offsetLeft;
            scrollLeft = wrapper.scrollLeft;
            e.preventDefault();
        });

        wrapper.addEventListener('mouseleave', () => {
            isDragging = false;
            wrapper.classList.remove('dragging');
        });

        wrapper.addEventListener('mouseup', () => {
            isDragging = false;
            wrapper.classList.remove('dragging');
        });

        wrapper.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            e.preventDefault();
            const x = e.pageX - wrapper.offsetLeft;
            const walk = (x - startX) * 1.5; // *1.5로 민감도 조절
            wrapper.scrollLeft = scrollLeft - walk;
        });
    }
});