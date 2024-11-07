
// 검색창 보더 스타일 클래스 추가
const searchBox = document.querySelector(".search_box");
if (searchBox){
    searchBox.querySelector("input").addEventListener("focus", () => {
        searchBox.classList.add("on");
    });

    searchBox.querySelector("input").addEventListener("blur", () => {
        searchBox.classList.remove("on");
    });
}

// 게시글 입력 페이지 해시태그 영역 스크립트
const inputField = document.querySelector('.hash_field');
const hashBox = document.querySelector('.hash_wr');
const existingTags = new Set();  // Track added tags
if (inputField) {
    inputField.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            let inputValue = inputField.value.trim();

            // 특수문자 유효성
            if (/[^a-zA-Z0-9가-힣]/.test(inputValue)) {
                alert("특수문자는 사용할 수 없습니다.");
                inputField.value = '';
                return;
            }

            // 텍스트 앞에 # 없으면 붙이고 있으면 그대로 노출
            inputValue = inputValue.startsWith('#') ? inputValue : `#${inputValue}`;

            // 중복 체크
            if (inputValue && !existingTags.has(inputValue)) {
                addTag(inputValue);
                existingTags.add(inputValue);
            } else {
                alert("이미 추가된 해시태그입니다.");
            }

            inputField.value = '';
        }
    });

    function addTag(text) {
        // 해시태그 태그 추가
        const tag = document.createElement('div');
        tag.classList.add('tag');
        tag.textContent = text;

        // 삭제 버튼 추가
        const closeButton = document.createElement('span');
        closeButton.classList.add('close-btn');
        closeButton.textContent = '×';

        // 해시태그 삭제 스크립트
        closeButton.addEventListener('click', () => {
            hashBox.removeChild(tag);
            existingTags.delete(text);
        });


        tag.appendChild(closeButton);
        hashBox.appendChild(tag);
    }
}

function delete_article(article_id) {
    if (confirm('정말 삭제하시겠습니까?')) {
        fetch(`/api/articles/${article_id}`, {
            method: 'DELETE', // 삭제는 DELETE 메서드 사용
        })
            .then(response => {
                if (response.ok) {
                    alert('게시글이 삭제되었습니다.');
                    history.back(); // 이전 페이지로 이동
                } else {
                    alert('게시글 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('게시글 삭제 중 오류가 발생했습니다.');
            });
    }
}