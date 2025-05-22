export const getCurrentLocation = async () => {
    if (!navigator.geolocation) {
        console.warn("Geolocation은 이 브라우저에서 지원되지 않습니다. 기본 좌표를 사용합니다.");
        return { latitude: 37.5665, longitude: 126.9780 }; // 서울시청
    }
    return new Promise((resolve) => {
        navigator.geolocation.getCurrentPosition(
            ({ coords: { latitude, longitude } }) => resolve({ latitude, longitude }),
            () => {
                console.warn("위치 정보를 가져오는 데 실패했습니다. 기본 좌표를 사용합니다.");
                resolve({ latitude: 37.5665, longitude: 126.9780 }); // 서울시청
            }
        );
    });
};