export function ajax({ url, method = 'GET', params = {}, data = {}, headers = {}, isMultipart = false, success, error }) {
    let fullUrl = url;
    // GET 요청이면 params → 쿼리스트링
    if (method.toUpperCase() === 'GET' && Object.keys(params).length > 0) {
        const queryString = new URLSearchParams(params).toString();
        fullUrl += '?' + queryString;
    }

    $.ajax({
        url: fullUrl,
        method,
        headers,
        processData: !isMultipart,
        contentType: isMultipart ? false : 'application/json',
        data: method.toUpperCase() === 'GET' ? undefined :
            isMultipart ? data : JSON.stringify(data),

        success: function (res) {
            if (res?.message) {
                console.log('✅ 요청 성공:', res.message);
            }
            if (typeof success === 'function') {success(res);}
        },
        error: function (xhr) {
            const status = xhr.status;

            if (status === 401) {
                console.warn('🔴 인증 실패');
                window.location.href = '/';
            } else if (status === 403) {
                console.warn('🔴 권한 없음');
                window.location.href = '/';
            } else if (status >= 500) {
                console.warn('🔴 서버 에러');
                alert('서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.');
            }

            if (typeof error === 'function') error(xhr);
        }
    });
}
