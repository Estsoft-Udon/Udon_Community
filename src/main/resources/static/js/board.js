// 게시글 입력 페이지 해시태그 영역 스크립트
const inputField = document.querySelector('.hash_field');
const hashBox = document.querySelector('.hash_wr');
let existingTags = new Set();  // Track added tags

if (inputField) {
    inputField.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            let inputValue = inputField.value.trim();

            // 특수문자 유효성 검사
            if (/[^a-zA-Z0-9가-힣]/.test(inputValue)) {
                alert("특수문자는 사용할 수 없습니다.");
                inputField.value = '';
                return;
            }

            // 텍스트 앞에 #가 없으면 추가
            inputValue = inputValue.startsWith('#') ? inputValue : `#${inputValue}`;

            // 중복 체크 후 태그 추가
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
        const tag = document.createElement('div');
        tag.classList.add('tag');
        tag.textContent = text;

        const closeButton = document.createElement('span');
        closeButton.classList.add('close-btn');
        closeButton.textContent = '×';

        closeButton.addEventListener('click', () => {
            hashBox.removeChild(tag);
            existingTags.delete(text);
        });

        tag.appendChild(closeButton);
        hashBox.appendChild(tag);
    }
}

async function delete_article(article_id) {
    if (confirm('정말 삭제하시겠습니까?')) {
        try {
            const response = await fetch(`/api/articles/${article_id}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                alert('게시글이 삭제되었습니다.');
                history.back();
            } else {
                alert('게시글 삭제에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('게시글 삭제 중 오류가 발생했습니다.');
        }
    }
}

async function article_press_like(articleId) {
    try {
        const response = await fetch(`/api/like/articlesLike`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ articleId: articleId })
        });
        const data = await response.json();
        if (data.success) {
            const likeCountElement = document.getElementById(`article_like_count_${articleId}`);
            likeCountElement.textContent = data.newLikeCount;
        } else {
            alert('좋아요 처리에 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('좋아요 중 오류가 발생했습니다.');
    }
}

async function post_article() {
    const selectedCategory = document.getElementById("category").value;
    const title = document.getElementById("title").value;
    const tagsArray = Array.from(existingTags);
    const content = document.getElementById("post_content").value;
    const selectedLocationId = document.getElementById("locationId").value;

    try {
        const response = await fetch('/articles/new', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                category: selectedCategory,
                title: title,
                hashtagName: tagsArray,
                content: content,
                locationId: selectedLocationId
            })
        });
        alert('글 작성이 완료되었습니다.');
        window.location.href = '/articles';
    } catch (error) {
        alert('글 작성이 실패했습니다: ' + error);
    }
}

async function edit_article(article_id) {
    const selectedCategory = document.getElementById("category").value;
    const title = document.getElementById("title").value;
    const content = document.getElementById("edit_content").value;
    const selectedLocationId = document.getElementById("locationId").value;
    const updatedHashtags = Array.from(existingTags);

    try {
        const hashtagsFromServer = await loadHashtags(article_id);

        const response = await fetch(`/articles/edit/${article_id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                category: selectedCategory,
                title: title,
                hashtagName: updatedHashtags,
                content: content,
                locationId: selectedLocationId
            })
        });

        if (response.ok) {
            alert('글 수정이 완료되었습니다.');
            window.location.href = `/articles/${article_id}`;
        } else {
            alert('글 수정에 실패했습니다.');
        }
    } catch (error) {
        console.error("Error in edit_article:", error);
        alert('글 수정 중 오류가 발생했습니다: ' + error);
    }
}


async function loadHashtags(articleId) {
    try {
        const response = await fetch(`/article/${articleId}/hashtags`);
        const hashtags = await response.json();
        hashBox.innerHTML = '';

        existingTags = new Set(hashtags);

        hashtags.forEach(hashtag => {
            addTag(hashtag);
        });

        return Array.from(existingTags);
    } catch (error) {
        console.error('Error loading hashtags:', error);
        throw error;
    }
}

function toggleGradeInfo() {
    const gradeInfo = document.querySelector('.grade_info');
    const gradeContent = document.querySelector('.grade_content');

    gradeInfo.classList.toggle('open');
    gradeContent.style.display = gradeContent.style.display === 'none' || !gradeContent.style.display ? 'block' : 'none';
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelector('.grade_content').style.display = 'none';
});
