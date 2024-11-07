function updateLowerLocations() {
    const upperLocation = document.getElementById('upperLocation').value;
    const locationSelect = document.getElementById('locationId');

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
                option.value = location.id;
                option.textContent = location.name;
                locationSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('에러 발생:', error);
        });
}

function initializeSelectBox(selectors) {
    const selectBoxes = document.querySelectorAll(selectors);

    selectBoxes.forEach(selectBox => {
        selectBox.addEventListener('change', function() {
            if (selectBox.value) {
                selectBox.classList.add('selected');
            } else {
                selectBox.classList.remove('selected');
            }
        });

        if (selectBox.value) {
            selectBox.classList.add('selected');
        }
    });
}

function checkId() {
    const loginId = document.getElementById('loginId').value.trim();
    if (!loginId) {
        alert('아이디를 입력해주세요.');
        return;
    }

    fetch('/api/checkId', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ loginId })
    })
        .then(response => response.json())
        .then(isDuplicate => {
            const messageElement = document.getElementById('idCheckMessage');

            if (isDuplicate) {
                messageElement.textContent = '이미 사용 중인 아이디입니다.';
                messageElement.style.color = 'red';
            } else {
                messageElement.textContent = '사용 가능한 아이디입니다.';
                messageElement.style.color = 'green';
            }
        })
        .catch(error => {
            console.error('아이디 중복 확인 오류:', error);
        });
}

initializeSelectBox('#upperLocation, #locationId');