function submit_comment() {
    const content = document.getElementById('comment_content').value;

    console.log('submitComment 호출됨:', content, article_id); // 디버깅 로그

    if (!content) {
        alert('댓글 내용을 입력해 주세요.');
        return;
    }

    fetch(`/articles/${article_id}/comments`, {
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

function press_edit(comment_id) {
    const content_element = document.getElementById(`origin_comment_${comment_id}`);
    const input_element = document.getElementById(`edit_comment_${comment_id}`);
    const edit_button = document.getElementById(`comment_edit_btn_${comment_id}`);

    // p 태그를 textarea로 바꾸기
    content_element.style.display = 'none';
    input_element.style.display = 'block';

    // 수정 버튼을 저장 버튼으로 변경
    edit_button.innerText = '저장';
    edit_button.setAttribute('onclick', 'edit_comment(' + comment_id + ')');
}

function edit_comment(comment_id) {
    const content_element = document.getElementById(`origin_comment_${comment_id}`);
    const input_element = document.getElementById(`edit_comment_${comment_id}`);
    const edit_button = document.getElementById(`comment_edit_btn_${comment_id}`);

    const content = input_element.value;

    fetch(`/articles/${article_id}/comments/${comment_id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json', // JSON 형식으로 데이터 전송
        },
        body: JSON.stringify({ content: content }), // 댓글 내용을 JSON 형식으로 변환
    })
        .then(response => {
            if (response.ok) {
                content_element.value = input_element.value;
                alert('댓글이 수정되었습니다.'); // 성공 메시지
                location.reload(); // 페이지 새로 고침
            } else {
                alert('댓글 수정에 실패했습니다.'); // 실패 메시지
            }
        })
        .catch(error => {
            console.error('Error:', error); // 오류 로그
            alert('댓글 수정 중 오류가 발생했습니다.'); // 오류 메시지
        });


    content_element.style.display = 'block';
    input_element.style.display = 'none';

    edit_button.innerText = '수정';
    edit_button.setAttribute('onclick', 'press_edit(' + comment_id + ')');
}

function delete_comment(comment_id) {
    if (confirm('정말 삭제하시겠습니까?')) {
        fetch(`/articles/${article_id}/comments/${comment_id}`, {
            method: 'DELETE', // 삭제는 DELETE 메서드 사용
        })
            .then(response => {
                if (response.ok) {
                    alert('댓글이 삭제되었습니다.');
                    location.reload(); // 페이지 새로 고침
                } else {
                    alert('댓글 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('댓글 삭제 중 오류가 발생했습니다.');
            });
    }
}

function press_like(commentId) {

}