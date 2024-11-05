function submit_comment() {
    const content = document.getElementById('comment_content').value;

    console.log('submitComment 호출됨:', content, articleId); // 디버깅 로그

    if (!content) {
        alert('댓글 내용을 입력해 주세요.');
        return;
    }

    fetch(`/articles/${articleId}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // JSON 형식으로 데이터 전송
        },
        body: JSON.stringify({ content: content }), // 댓글 내용을 JSON 형식으로 변환
    })
        .then(response => {
            if (response.ok) {
                alert('댓글이 등록되었습니다.'); // 성공 메시지
                location.reload(); // 페이지 새로 고침
            } else {
                alert('댓글 등록에 실패했습니다.'); // 실패 메시지
            }
        })
        .catch(error => {
            console.error('Error:', error); // 오류 로그
            alert('댓글 등록 중 오류가 발생했습니다.'); // 오류 메시지
        });
}


function edit_comment() {

}

function delete_comment() {

}