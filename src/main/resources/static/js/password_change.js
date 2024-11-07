// 비밀번호 정규식 검증
function validatePassword() {
    const newPassword = document.getElementById('newPassword').value.trim();
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    const passwordMessage = document.getElementById('passwordMessage');

    if (!passwordPattern.test(newPassword)) {
        passwordMessage.textContent = '최소 8자 이상, 영문자, 숫자, 특수문자가 포함.';
        passwordMessage.style.color = 'red';
    } else {
        passwordMessage.textContent = '';
    }
}
// 비밀번호 확인
function checkPasswordMatch() {
    const newPassword = document.getElementById('newPassword').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();
    const messageElement = document.getElementById('passwordMatchMessage');
    if (!newPassword || !confirmPassword) {
        messageElement.textContent = '';
        return;
    }
    if (newPassword !== confirmPassword) {
        messageElement.textContent = '비밀번호가 일치하지 않습니다.';
        messageElement.style.color = 'red';
    } else {
        messageElement.textContent = '비밀번호가 일치합니다.';
        messageElement.style.color = 'green';
    }
}
// 비밀번호 입력 시 실시간 정규식 검사
document.getElementById('newPassword').addEventListener('input', function() {
    validatePassword(); // 비밀번호 정규식 검증
    checkPasswordMatch(); // 비밀번호 일치 여부 확인
});
// 비밀번호 확인 입력 시 실시간 비밀번호 일치 여부 확인
document.getElementById('confirmPassword').addEventListener('input', checkPasswordMatch);