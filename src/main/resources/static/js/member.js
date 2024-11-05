const selectBoxes = document.querySelectorAll('.securityQuestion, #location');

selectBoxes.forEach(selectBox => {
    selectBox.addEventListener('change', function() {
        if (selectBox.value) {
            selectBox.classList.add('selected');
        } else {
            selectBox.classList.remove('selected');
        }
    });

    window.addEventListener('load', function() {
        if (selectBox.value) {
            selectBox.classList.add('selected');
        }
    });
});

function updateLowerLocations() {
    const upperLocation = document.getElementById('upperLocation').value;
    const locationSelect = document.getElementById('locationName');

    // 선택된 상위 지역이 없으면 하위 지역을 초기화
    if (!upperLocation) {
        locationSelect.innerHTML = '<option value="" disabled selected>거주하는 지역을 선택하세요</option>';
        return;
    }

    // AJAX 요청
    // LocationController 값을 가져온다.
    fetch(`/getLowerLocations?upperLocation=${encodeURIComponent(upperLocation)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('네트워크 응답이 좋지 않습니다.');
            }
            return response.json();
        })
        .then(data => {
            // 하위 지역을 업데이트
            locationSelect.innerHTML = '<option value="" disabled selected>하위 지역 선택</option>'; // 초기화

            data.forEach(location => {
                const option = document.createElement('option');
                option.textContent = location.name; // location의 name 값으로 설정
                locationSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('에러 발생:', error);
        });
}

// 에러 메시지가 있으면 알림 창을 표시
const errorMessage = document.getElementById("errorMessage")?.value;
if (errorMessage) {
    alert(errorMessage);
};