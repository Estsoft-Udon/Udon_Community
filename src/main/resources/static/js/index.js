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

// 서버에서 전달된 이벤트 배열 (타임리프를 통해 초기화)
const events = /*[[${userEvents}]]*/ [];

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

                // 이벤트 표시
                events.forEach(event => {
                    const eventDate = new Date(event.dateTime);
                    if (eventDate.getDate() === dateCounter && eventDate.getMonth() === month && eventDate.getFullYear() === year) {
                        const eventDiv = document.createElement('div');
                        eventDiv.className = 'event';
                        eventDiv.textContent = event.title; // 이벤트 제목
                        cell.appendChild(eventDiv);
                    }
                });

                dateCounter++; // 다음 날짜로 이동
            }
            row.appendChild(cell);
        }
        table.appendChild(row);
    }

    calendarElement.appendChild(table);
}

function updateCalendar() {
    renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
}

function changeMonth(direction) {
    currentDate.setMonth(currentDate.getMonth() + direction);
    updateCalendar(); // 달력을 업데이트
}

window.onload = () => updateCalendar();

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

    console.log(locationId);
    window.location.href = `/articles?locationId=${locationId}`;

    // 선택된 지역 초기화
    document.getElementById('upperLocation').selectedIndex = 0;
    document.getElementById('locationId').selectedIndex = 0;
}

document.getElementById('locationId').disabled = true;