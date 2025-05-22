export function getSelect(condition) {
    return new ol.interaction.Select({
        condition: condition,
        filter: function(feature) {
            // features라는 속성이 있는 경우는 클러스터이므로 제외
            const clustered = feature.get('features');
            if (feature.get('eventAble') === false) return false;
            return !clustered || clustered.length === 1;
        },
        style: (feature) => {
            const clustered = feature.get('features');
            const single = clustered[0];
            return new ol.style.Style({
                image: new ol.style.Icon({
                    anchor: [0.5, 1],
                    src: single.get('iconUrl'),
                    scale: 1.2
                }),
                zIndex: 10
            });
        }
    });
}