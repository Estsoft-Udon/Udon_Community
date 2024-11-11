// 아이디 중복확인
function checkId() {
    const loginId = document.getElementById('loginId').value.trim();
    const messageElement = document.getElementById('idCheckMessage');
    const idPattern = /^[a-zA-Z0-9]{4,20}$/;

    if (!loginId) {
        messageElement.textContent = '아이디를 입력하세요.';
        messageElement.style.color = 'red';
        return false;
    }

    if (!idPattern.test(loginId)) {
        messageElement.textContent = '4자 이상 20자 이하, 영문자와 숫자만 포함';
        messageElement.style.color = 'red';
        return false;
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
            if (isDuplicate) {
                messageElement.textContent = '이미 사용 중인 아이디입니다.';
                messageElement.style.color = 'red';
                return false;
            } else {
                messageElement.textContent = '사용 가능한 아이디입니다.';
                messageElement.style.color = 'green';
                return true;
            }
        })
        .catch(error => {
            console.error('아이디 중복 확인 오류:', error);
            return false;
        });
}

// 닉네임 중복확인
async function checkNickname() {
    const nickname = document.getElementById('nickname').value.trim();
    const messageElement = document.getElementById('nicknameCheckMessage');
    const nicknamePattern = /^[a-zA-Z0-9가-힣]{2,10}$/;

    if (!nickname) {
        messageElement.textContent = '닉네임을 입력하세요.';
        messageElement.style.color = 'red';
        return false;
    }

    if (!nicknamePattern.test(nickname)) {
        messageElement.textContent = '2자 이상 10자 이하, 한글, 영문자 또는 숫자만 포함.';
        messageElement.style.color = 'red';
        return false;
    }

    try {
        const response = await fetch('/api/checkNickname', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nickname })
        });

        const isDuplicate = await response.json();

        if (isDuplicate) {
            messageElement.textContent = '이미 사용 중인 닉네임입니다.';
            messageElement.style.color = 'red';
            return false;
        } else {
            messageElement.textContent = '사용 가능한 닉네임입니다.';
            messageElement.style.color = 'green';
            return true;
        }
    } catch (error) {
        console.error('닉네임 중복 확인 오류:', error);
        return false;
    }
}

// 이메일 중복확인
async function checkEmail() {
    const email = document.getElementById('email').value.trim();
    const messageElement = document.getElementById('emailCheckMessage');
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email) {
        messageElement.textContent = '이메일을 입력하세요.';
        messageElement.style.color = 'red';
        return false;
    }

    if (!emailPattern.test(email)) {
        messageElement.textContent = '유효한 이메일 주소를 입력하세요.';
        messageElement.style.color = 'red';
        return false;
    }

    try {
        const response = await fetch('/api/checkEmail', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email })
        });

        const isDuplicate = await response.json();

        if (isDuplicate) {
            messageElement.textContent = '이미 사용 중인 이메일입니다.';
            messageElement.style.color = 'red';
            return false;
        } else {
            messageElement.textContent = '사용 가능한 이메일입니다.';
            messageElement.style.color = 'green';
            return true;
        }
    } catch (error) {
        console.error('이메일 중복 확인 오류:', error);
        return false;
    }
}

// 폼 제출 시 아이디, 닉네임, 이메일 유효성 체크
document.getElementById('signupForm').addEventListener('submit', async function(event) {
    const isIdValid = await checkId();
    const isNicknameValid = await checkNickname();
    const isEmailValid = await checkEmail();

    if (!isIdValid || !isNicknameValid || !isEmailValid) {
        event.preventDefault();
        alert('아이디, 닉네임, 이메일을 확인 해주세요');
    }
});