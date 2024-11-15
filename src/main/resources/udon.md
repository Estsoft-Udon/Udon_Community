```mysql
use udon;

CREATE TABLE location
(
    location_id    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    upper_location VARCHAR(255) NOT NULL
);

CREATE TABLE users
(
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    login_id        VARCHAR(50)  NOT NULL UNIQUE,
    name            VARCHAR(50)  NOT NULL,
    nickname        VARCHAR(50)  NOT NULL UNIQUE,
    email           VARCHAR(100) NOT NULL UNIQUE,
    grade           VARCHAR(100) NULL,
    password        VARCHAR(255) NOT NULL,
    password_hint   VARCHAR(255) NOT NULL,
    password_answer VARCHAR(100) NOT NULL,
    created_at      DATETIME     NOT NULL,
    updated_at      DATETIME     NOT NULL,
    deleted_at      DATETIME     NULL,
    is_deleted      BOOLEAN      NOT NULL,
    last_login      DATETIME     NULL,
    location_id     BIGINT       NOT NULL,
    is_promotion_requested BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (location_id) REFERENCES location (location_id)
);

CREATE TABLE articles
(
    article_id  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    location_id BIGINT       NOT NULL,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    category    VARCHAR(255) NOT NULL,
    view_count  BIGINT       NOT NULL DEFAULT 0,
    created_at  DATETIME     NOT NULL,
    updated_at  DATETIME     NOT NULL,
    deleted_at  DATETIME     NULL,
    is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE,
    is_blind    BOOLEAN      NOT NULL DEFAULT FALSE,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (location_id) REFERENCES location (location_id)
);

CREATE TABLE comments
(
    comment_id BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT   NOT NULL,
    user_id    BIGINT   NOT NULL,
    content    TEXT     NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME NULL,
    is_deleted BOOLEAN  NOT NULL,

    FOREIGN KEY (article_id) REFERENCES articles (article_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE articles_like
(
    article_like_id BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    article_id      BIGINT   NOT NULL,
    user_id         BIGINT   NOT NULL,
    created_at      DATETIME NOT NULL,

    FOREIGN KEY (article_id) REFERENCES articles (article_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE comments_like
(
    comment_like_id BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    comment_id      BIGINT   NOT NULL,
    user_id         BIGINT   NOT NULL,
    created_at      DATETIME NOT NULL,

    FOREIGN KEY (comment_id) REFERENCES comments (comment_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE event
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_id  BIGINT       NOT NULL, -- 외래 키
    user_id      BIGINT       NOT NULL, -- 외래 키
    date_time    DATETIME     NOT NULL,
    title        VARCHAR(255) NOT NULL,
    content      TEXT,
    event_type   VARCHAR(255) NOT NULL, -- ENUM 타입 사용
    requested_at DATETIME     NOT NULL,
    updated_at   DATETIME     NOT NULL,
    deleted_at   DATETIME,
    is_accepted  BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (location_id) REFERENCES location (location_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE hashtag
(
    hashtag_id BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE articles_hashtags_join
(
    article_id BIGINT,
    hashtag_id BIGINT,
    PRIMARY KEY (article_id, hashtag_id),
    FOREIGN KEY (article_id) REFERENCES articles (article_id) ON DELETE CASCADE,
    FOREIGN KEY (hashtag_id) REFERENCES hashtag (hashtag_id) ON DELETE CASCADE
);

CREATE TABLE festival_datas
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    start_date VARCHAR(255) NOT NULL,
    end_date   VARCHAR(255) NOT NULL
);



INSERT INTO location (name, upper_location) VALUES
                                                -- 서울특별시
                                                ('종로구', '서울특별시'), ('중구', '서울특별시'), ('용산구', '서울특별시'),
                                                ('성동구', '서울특별시'), ('광진구', '서울특별시'), ('동대문구', '서울특별시'),
                                                ('중랑구', '서울특별시'), ('성북구', '서울특별시'), ('강북구', '서울특별시'),
                                                ('도봉구', '서울특별시'), ('노원구', '서울특별시'), ('은평구', '서울특별시'),
                                                ('서대문구', '서울특별시'), ('마포구', '서울특별시'), ('양천구', '서울특별시'),
                                                ('강서구', '서울특별시'), ('구로구', '서울특별시'), ('금천구', '서울특별시'),
                                                ('영등포구', '서울특별시'), ('동작구', '서울특별시'), ('관악구', '서울특별시'),
                                                ('서초구', '서울특별시'), ('강남구', '서울특별시'), ('송파구', '서울특별시'),
                                                ('강동구', '서울특별시'),

                                                -- 부산광역시
                                                ('중구', '부산광역시'), ('서구', '부산광역시'), ('동구', '부산광역시'),
                                                ('영도구', '부산광역시'), ('부산진구', '부산광역시'), ('동래구', '부산광역시'),
                                                ('남구', '부산광역시'), ('북구', '부산광역시'), ('해운대구', '부산광역시'),
                                                ('사하구', '부산광역시'), ('금정구', '부산광역시'), ('강서구', '부산광역시'),
                                                ('연제구', '부산광역시'), ('수영구', '부산광역시'), ('사상구', '부산광역시'),
                                                ('기장군', '부산광역시'),

                                                -- 대구광역시
                                                ('중구', '대구광역시'), ('동구', '대구광역시'), ('서구', '대구광역시'),
                                                ('남구', '대구광역시'), ('북구', '대구광역시'), ('수성구', '대구광역시'),
                                                ('달서구', '대구광역시'), ('달성군', '대구광역시'),

                                                -- 인천광역시
                                                ('중구', '인천광역시'), ('동구', '인천광역시'), ('미추홀구', '인천광역시'),
                                                ('연수구', '인천광역시'), ('남동구', '인천광역시'), ('부평구', '인천광역시'),
                                                ('계양구', '인천광역시'), ('서구', '인천광역시'), ('강화군', '인천광역시'),
                                                ('옹진군', '인천광역시'),

                                                -- 광주광역시
                                                ('동구', '광주광역시'), ('서구', '광주광역시'), ('남구', '광주광역시'),
                                                ('북구', '광주광역시'), ('광산구', '광주광역시'),

                                                -- 대전광역시
                                                ('동구', '대전광역시'), ('중구', '대전광역시'), ('서구', '대전광역시'),
                                                ('유성구', '대전광역시'), ('대덕구', '대전광역시'),

                                                -- 울산광역시
                                                ('중구', '울산광역시'), ('남구', '울산광역시'), ('동구', '울산광역시'),
                                                ('북구', '울산광역시'), ('울주군', '울산광역시'),

                                                -- 세종특별자치시
                                                ('세종시', '세종시'),

                                                -- 경기도
                                                ('수원시', '경기도'), ('고양시', '경기도'), ('용인시', '경기도'),
                                                ('성남시', '경기도'), ('안산시', '경기도'), ('안양시', '경기도'),
                                                ('부천시', '경기도'), ('화성시', '경기도'), ('남양주시', '경기도'),
                                                ('파주시', '경기도'), ('김포시', '경기도'), ('이천시', '경기도'),
                                                ('평택시', '경기도'), ('광명시', '경기도'), ('오산시', '경기도'),
                                                ('시흥시', '경기도'), ('군포시', '경기도'), ('의왕시', '경기도'),
                                                ('하남시', '경기도'), ('양주시', '경기도'), ('구리시', '경기도'),
                                                ('포천시', '경기도'), ('여주시', '경기도'), ('양평군', '경기도'),
                                                ('연천군', '경기도'), ('가평군', '경기도'), ('광주시', '경기도'),

                                                -- 강원도
                                                ('춘천시', '강원도'), ('원주시', '강원도'), ('강릉시', '강원도'),
                                                ('동해시', '강원도'), ('태백시', '강원도'), ('속초시', '강원도'),
                                                ('삼척시', '강원도'), ('홍천군', '강원도'), ('횡성군', '강원도'),
                                                ('영월군', '강원도'), ('평창군', '강원도'), ('정선군', '강원도'),
                                                ('철원군', '강원도'), ('화천군', '강원도'), ('양구군', '강원도'),
                                                ('인제군', '강원도'), ('고성군', '강원도'), ('양양군', '강원도'),

                                                -- 충청북도
                                                ('청주시', '충청북도'), ('충주시', '충청북도'), ('제천시', '충청북도'),
                                                ('진천군', '충청북도'), ('괴산군', '충청북도'), ('음성군', '충청북도'),
                                                ('단양군', '충청북도'), ('보은군', '충청북도'), ('영동군', '충청북도'),
                                                ('옥천군', '충청북도'),

                                                -- 충청남도
                                                ('천안시', '충청남도'), ('공주시', '충청남도'), ('보령시', '충청남도'),
                                                ('아산시', '충청남도'), ('서산시', '충청남도'), ('논산시', '충청남도'),
                                                ('계룡시', '충청남도'), ('당진시', '충청남도'), ('금산군', '충청남도'),
                                                ('부여군', '충청남도'), ('서천군', '충청남도'), ('청양군', '충청남도'),
                                                ('홍성군', '충청남도'), ('예산군', '충청남도'), ('태안군', '충청남도'),

                                                -- 전라북도
                                                ('전주시', '전라북도'), ('익산시', '전라북도'), ('군산시', '전라북도'),
                                                ('정읍시', '전라북도'), ('남원시', '전라북도'), ('김제시', '전라북도'),
                                                ('완주군', '전라북도'), ('진안군', '전라북도'), ('무주군', '전라북도'),
                                                ('장수군', '전라북도'), ('임실군', '전라북도'), ('순창군', '전라북도'),
                                                ('고창군', '전라북도'), ('부안군', '전라북도'),

                                                -- 전라남도
                                                ('목포시', '전라남도'), ('여수시', '전라남도'), ('순천시', '전라남도'),
                                                ('나주시', '전라남도'), ('광양시', '전라남도'), ('담양군', '전라남도'),
                                                ('곡성군', '전라남도'), ('구례군', '전라남도'), ('고흥군', '전라남도'),
                                                ('보성군', '전라남도'), ('화순군', '전라남도'), ('장흥군', '전라남도'),
                                                ('강진군', '전라남도'), ('해남군', '전라남도'), ('영암군', '전라남도'),
                                                ('무안군', '전라남도'), ('함평군', '전라남도'), ('영광군', '전라남도'),
                                                ('장성군', '전라남도'), ('완도군', '전라남도'), ('진도군', '전라남도'),
                                                ('신안군', '전라남도'),

                                                -- 경상북도
                                                ('포항시', '경상북도'), ('경주시', '경상북도'), ('김천시', '경상북도'),
                                                ('안동시', '경상북도'), ('구미시', '경상북도'), ('영주시', '경상북도'),
                                                ('영천시', '경상북도'), ('상주시', '경상북도'), ('문경시', '경상북도'),
                                                ('경산시', '경상북도'), ('군위군', '경상북도'), ('의성군', '경상북도'),
                                                ('청송군', '경상북도'), ('영양군', '경상북도'), ('영덕군', '경상북도'),
                                                ('청도군', '경상북도'), ('고령군', '경상북도'), ('성주군', '경상북도'),
                                                ('칠곡군', '경상북도'), ('예천군', '경상북도'), ('봉화군', '경상북도'),
                                                ('울진군', '경상북도'), ('울릉군', '경상북도'),

                                                -- 경상남도
                                                ('창원시', '경상남도'), ('진주시', '경상남도'), ('통영시', '경상남도'),
                                                ('사천시', '경상남도'), ('김해시', '경상남도'), ('밀양시', '경상남도'),
                                                ('거제시', '경상남도'), ('양산시', '경상남도'), ('의령군', '경상남도'),
                                                ('함안군', '경상남도'), ('창녕군', '경상남도'), ('고성군', '경상남도'),
                                                ('남해군', '경상남도'), ('하동군', '경상남도'), ('산청군', '경상남도'),
                                                ('함양군', '경상남도'), ('거창군', '경상남도'), ('합천군', '경상남도'),

                                                -- 제주특별자치도
                                                ('제주시', '제주도'), ('서귀포시', '제주도');

select * from users;
```