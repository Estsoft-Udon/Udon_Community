document.addEventListener('DOMContentLoaded', function () {
    // 페이지 로드 시 기본 데이터를 요청 (검색어가 비어있을 경우)
    const searchInput = document.getElementById('search-input');
    const keyword = searchInput.value.trim();  // 초기값은 빈값으로 설정

    // 페이지 로드 시 기본적으로 데이터를 로드 (keyword가 비어 있으면 모든 사용자 목록)
    searchUsers(keyword, 0);

    const searchForm = document.getElementById('search-form');
    searchForm.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 제출 동작 방지
        const keyword = searchInput.value; // 검색어 가져오기
        searchUsers(keyword, 0);  // 첫 번째 페이지부터 데이터 요청
    });
});

function searchUsers(keyword, page) {
    // 검색어가 없을 경우 빈값으로 전송하여 기본 데이터 요청
    const url = `/api/admin/member_list?keyword=${encodeURIComponent(keyword)}&page=${page}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            updateTable(data.content);  // 테이블 업데이트
            updatePagination(data, page);  // 페이지네이션 업데이트
        })
        .catch(error => {
            console.error('검색 중 오류가 발생했습니다.', error);
        });
}

function updateTable(users) {
    const tbody = document.getElementById('user-table-body');
    tbody.innerHTML = '';  // 기존 데이터 삭제
    users.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td><a href="/admin/member/member_edit/${user.id}">${user.loginId}</a></td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.nickname}</td>
            <td>${user.location.name}</td>
            <td>${user.createdAt}</td>
            <td>${user.displayName}</td>
            <td>${user.isPromotionRequested? 'O' : 'X'}</td>
        `;
        tbody.appendChild(row);
    });
}

function updatePagination(data, currentPage) {
    const pagination = document.querySelector('.pagination ul');
    pagination.innerHTML = '';  // 기존 페이지네이션 삭제

    const totalPages = data.totalPages;
    const totalElements = data.totalElements;
    const size = data.size;

    // 이전 페이지 링크
    if (currentPage > 0) {
        const prevLi = document.createElement('li');
        prevLi.classList.add('page-item');
        const prevA = document.createElement('a');
        prevA.classList.add('page-link');
        prevA.href = 'javascript:void(0)';
        prevA.textContent = '이전';
        prevA.addEventListener('click', function() {
            searchUsers(document.getElementById('search-input').value, currentPage - 1);
        });
        prevLi.appendChild(prevA);
        pagination.appendChild(prevLi);
    }

    // 페이지 링크 생성
    for (let i = 0; i < totalPages; i++) {
        const li = document.createElement('li');
        li.classList.add('page-item');
        if (i === currentPage) {
            li.classList.add('active');
        }

        const a = document.createElement('a');
        a.classList.add('page-link');
        a.href = 'javascript:void(0)';
        a.textContent = i + 1;
        a.addEventListener('click', function() {
            searchUsers(document.getElementById('search-input').value, i); // 해당 페이지로 데이터 요청
        });

        li.appendChild(a);
        pagination.appendChild(li);
    }

    // 다음 페이지 링크
    if (currentPage < totalPages - 1) {
        const nextLi = document.createElement('li');
        nextLi.classList.add('page-item');
        const nextA = document.createElement('a');
        nextA.classList.add('page-link');
        nextA.href = 'javascript:void(0)';
        nextA.textContent = '다음';
        nextA.addEventListener('click', function() {
            searchUsers(document.getElementById('search-input').value, currentPage + 1);
        });
        nextLi.appendChild(nextA);
        pagination.appendChild(nextLi);
    }
}
