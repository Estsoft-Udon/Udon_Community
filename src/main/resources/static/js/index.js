function openModal() {
    document.getElementById("modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("modal").style.display = "none";
}

window.onclick = function(event) {
    if (event.target === document.getElementById("modal")) {
        closeModal();
    }
}

function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);

    fetch('/api/event', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok.');
            return response.text();
        })
        .then(message => {
            alert(message);
            closeModal();
            event.target.reset();
            updateCalendar();
        })
        .catch(error => console.error('Error:', error));

    return false;
}

let currentDate = new Date();
let events = [];

// 이벤트를 API에서 가져오는 함수
function fetchEvents() {
    fetch('/api/event/accepted') // 승인된 이벤트 데이터를 가져옵니다
        .then(response => response.json()) // 응답을 JSON 형식으로 변환
        .then(data => {
            events = data; // 이벤트 배열에 데이터 업데이트
            updateCalendar(); // 데이터를 받은 후 달력을 갱신
        })
        .catch(error => console.error('Error fetching events:', error));
}

// 캘린더 렌더링 함수
function renderCalendar(year, month) {
    const calendarElement = document.getElementById('calendar');
    calendarElement.innerHTML = ''; // 기존 내용 초기화

    const date = new Date(year, month);
    const monthName = date.toLocaleString('default', { month: 'long' });
    const header = document.createElement('div');
    header.className = 'calendar-header';

    const prevButton = document.createElement('button');
    prevButton.textContent = '<';
    prevButton.onclick = function() {
        changeMonth(-1);
    };

    const monthText = document.createElement('p');
    monthText.className = 'month-text';
    monthText.textContent = `${year}년 ${monthName}`;

    const nextButton = document.createElement('button');
    nextButton.textContent = '>';
    nextButton.onclick = function() {
        changeMonth(1);
    };

    header.appendChild(prevButton);
    header.appendChild(monthText);
    header.appendChild(nextButton);
    calendarElement.appendChild(header);

    const table = document.createElement('table');
    const headerRow = document.createElement('tr');
    const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];

    daysOfWeek.forEach(day => {
        const th = document.createElement('th');
        th.textContent = day;
        headerRow.appendChild(th);
    });
    table.appendChild(headerRow);

    // 달의 첫 번째 날과 마지막 날 찾기
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const firstDayIndex = firstDay.getDay(); // 첫 번째 날의 요일
    const totalDays = lastDay.getDate(); // 해당 달의 총 일수

    let dateCounter = 1;
    const totalRows = Math.ceil((totalDays + firstDayIndex) / 7);

    for (let i = 0; i < totalRows; i++) {
        const row = document.createElement('tr');

        for (let j = 0; j < 7; j++) {
            const cell = document.createElement('td');

            if (i === 0 && j < firstDayIndex) {
                // 첫 번째 주에 빈 칸 추가
                cell.textContent = '';
            } else if (dateCounter > totalDays) {
                // 날짜가 총 일수를 초과하면 빈 칸 추가
                cell.textContent = '';
            } else {
                cell.textContent = dateCounter;

                // 오늘 날짜 강조
                const today = new Date();
                if (dateCounter === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                    cell.classList.add('today');
                }

                // 이벤트가 있는 경우 이벤트를 추가
                if (Array.isArray(events)) {
                    events.forEach(event => {
                        const eventDate = new Date(event.dateTime); // Date 객체로 변환
                        if (
                            eventDate.getDate() === dateCounter &&
                            eventDate.getMonth() === month &&
                            eventDate.getFullYear() === year
                        ) {
                            const eventDiv = document.createElement('div');
                            eventDiv.className = 'event ' + getEventTypeClass(event.eventType); // 이벤트 타입에 따라 클래스 추가
                            eventDiv.textContent = getEventTypeText(event.eventType) + ' ' + event.title; // 타입에 맞는 텍스트 추가
                            eventDiv.onclick = function() {
                                showEventDetails(event); // 클릭 시 상세 정보 표시
                            };
                            cell.appendChild(eventDiv);
                        }
                    });
                }

                dateCounter++; // 다음 날짜로 이동
            }
            row.appendChild(cell);
        }
        table.appendChild(row);
    }

    calendarElement.appendChild(table);
}

// 이벤트 타입에 맞는 클래스 반환 함수
function getEventTypeClass(eventType) {
    switch (eventType) {
        case 'GATHERING':
            return 'gathering';
        case 'FESTIVAL':
            return 'festival';
        default:
            return ''; // 기본 클래스 (없을 경우)
    }
}

// 이벤트 타입에 맞는 텍스트 반환 함수
function getEventTypeText(eventType) {
    switch (eventType) {
        case 'GATHERING':
            return '[소모임]';
        case 'FESTIVAL':
            return '[축제]';
        default:
            return ''; // 기본 텍스트 (없을 경우)
    }
}

// 캘린더 업데이트 함수
function updateCalendar() {
    renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
}

// 월을 변경하는 함수
function changeMonth(direction) {
    currentDate.setMonth(currentDate.getMonth() + direction);
    updateCalendar(); // 달력을 업데이트
}

// 페이지가 로드되면 캘린더를 렌더링하고 이벤트를 가져옵니다.
window.onload = () => {
    fetchEvents(); // 이벤트 데이터를 가져옵니다
    updateCalendar(); // 캘린더를 초기화
};

// 모달 관련 변수
const modal = document.getElementById('eventModal');
const closeModalBtn = document.getElementById('closeModal');

// 이벤트 상세 정보를 모달에 표시하는 함수
function showEventDetails(event) {
    const eventTitle = document.getElementById('eventDetailsTitle');
    const eventType = document.getElementById('eventDetailsType');
    const eventDateTime = document.getElementById('eventDetailsDateTime');
    const eventUser = document.getElementById('eventDetailsUser');
    const eventDescription = document.getElementById('eventDetailsDescription');

    // 모달에 이벤트 데이터 표시
    eventTitle.textContent = event.title;
    eventType.textContent = getEventTypeText(event.eventType);
    eventDateTime.textContent = `${new Date(event.dateTime).toLocaleString("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit"
    })}`;
    eventUser.textContent = event.nickName || '정보 없음';
    eventDescription.textContent = event.content || '상세 설명이 없습니다.';

    // 모달 표시
    modal.style.display = 'block';
}

// 모달 닫기 버튼 클릭 시 모달 숨기기
closeModalBtn.onclick = function() {
    modal.style.display = 'none';
}

// 모달 외부 클릭 시 모달 숨기기
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}

function toggleLocationName() {
    const upperLocation = document.getElementById('upperLocation');
    const locationName = document.getElementById('locationId');
    const submitButton = document.getElementById('submitButton');

    if (upperLocation.value) {
        locationName.disabled = false;
    } else {
        locationName.disabled = true;
        locationName.value = '';
        submitButton.disabled = true;
    }

    locationName.onchange = function() {
        submitButton.disabled = !locationName.value;
    };
}

function redirectToLocationBoard() {
    const locationId = document.getElementById('locationId').value;

    if (!locationId) {
        alert("지역을 선택하세요.");
        return;
    }

    // 지역 정보만 URL에 추가
    let url = `/articles?locationId=${locationId}`;

    // 리디렉션
    window.location.href = url;

    // 선택된 지역 초기화
    document.getElementById('upperLocation').selectedIndex = 0;
    document.getElementById('locationId').selectedIndex = 0;
}

document.getElementById('locationId').disabled = true;

function alertAndRedirect() {
    alert("회원만 작성 가능합니다.");
    window.location.href = '/login';
}