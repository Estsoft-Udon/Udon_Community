function withdrawal() {
    document.getElementById('withdrawal_btn').addEventListener('submit', function (event) {
        event.preventDefault(); // 폼 기본 제출 방지

        if (confirm("정말 탈퇴하시겠습니까?")) {
            fetch('/withdrawal', {
                method: 'POST'  // POST 요청
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('탈퇴 처리에 실패했습니다.');
                    }
                })
                .then(data => {
                    alert("탈퇴 처리가 완료되었습니다.");
                    window.location.href = '/';  // 홈 페이지로 리다이렉트
                })
                .catch(error => {
                    console.error(error);
                    alert("탈퇴 처리 중 오류가 발생했습니다.");
                });
        }
    });
}