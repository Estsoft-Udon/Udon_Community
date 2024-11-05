document.addEventListener('DOMContentLoaded', function() {
    const searchBox = document.querySelector(".search_box");

    searchBox.querySelector("input").addEventListener("focus", () => {
        searchBox.classList.add("on");
    });

    searchBox.querySelector("input").addEventListener("blur", () => {
        searchBox.classList.remove("on");
    });

    window.loadArticlesByHashtag = function(hashtagId) {
        fetch(`/api/hashtag/${hashtagId}/articles`)
            .then(response => response.json())
            .then(data => updateArticleList(data))
            .catch(error => console.error('Error fetching articles by hashtag:', error));
    };

    window.loadArticlesByCategory = function(category) {
        fetch(`/api/category/${category}/articles`)
            .then(response => response.json())
            .then(data => updateArticleList(data))
            .catch(error => console.error('Error fetching articles by category:', error));
    };

    function updateArticleList(articles) {
        const postList = document.querySelector('.post_list');
        postList.innerHTML = '';

        articles.forEach(article => {
            const listItem = document.createElement('li');
            listItem.classList.add('post_item');
            listItem.innerHTML = `
                <div class="l_box">
                    <div class="t_box">
                        <span class="post_num">${article.id}.</span>
                        <a href="/articles/${article.id}" class="post_title">${article.title}</a>
                    </div>
                    <div class="post_author">${article.nickname}</div>
                </div>
                <div class="post_stats">
                    <div class="views"><span class="view_icon icon_box"></span><span>${article.viewCount}</span></div>
                    <div class="likes"><span class="thumb_icon icon_box"></span>좋아요 수</div>
                    <div class="comments"><span class="comment_icon icon_box"></span>댓글 수</div>
                </div>
            `;
            postList.appendChild(listItem);
        });
    }
});
