function submitUpgradeRequest(event) {
    event.preventDefault();  // 폼 제출을 막음

    // 등업 요청 버튼 비활성화
    const button = document.getElementById('upgradeRequestBtn');
    button.disabled = true;
    button.textContent = '등업 요청 완료'; // 버튼 텍스트 변경

    // 서버에 요청을 보내고 나서 알림을 띄움
    fetch('/request-promotion', {
        method: 'POST',
        body: new FormData(document.getElementById('promotionForm'))
    })
        .then(response => {
            if (response.ok) {
                alert('등업 요청이 완료되었습니다');
            } else {
                alert('등업 요청에 실패했습니다. 다시 시도해주세요.');
            }
        })
        .catch(error => {
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        });
}