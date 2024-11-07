function checkId() {
    const loginId = document.getElementById('loginId').value.trim();

    // 정규식을 사용하여 아이디 형식 검사
    const idPattern = /^[a-zA-Z0-9]{4,20}$/;
    const messageElement = document.getElementById('idCheckMessage');
    if (!loginId) {
        messageElement.textContent = '아이디를 입력하세요.';
        messageElement.style.color = 'red';
        return;
    }

    // 아이디 형식이 유효한지 검사
    if (!idPattern.test(loginId)) {
        messageElement.textContent = '4자 이상 20자 이하, 영문자와 숫자만 포함.';
        messageElement.style.color = 'red';
        return;
    }

    // 서버로 아이디 중복 체크 요청
    fetch('/api/checkId', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ loginId })
    })
        .then(response => response.json())
        .then(isDuplicate => {
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