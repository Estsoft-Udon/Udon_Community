```mysql
CREATE DATABASE udon;
use udon;

CREATE TABLE users
(
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    login_id        VARCHAR(50)  NOT NULL UNIQUE,
    name            VARCHAR(50)  NOT NULL,
    nickname        VARCHAR(50)  NOT NULL UNIQUE,
    email           VARCHAR(100) NOT NULL UNIQUE,
    grade           VARCHAR(100) NOT NULL,
    password        VARCHAR(50)  NOT NULL,
    password_hint   VARCHAR(255) NOT NULL,
    password_answer VARCHAR(100) NOT NULL,
    created_at      DATETIME     NOT NULL,
    updated_at      DATETIME     NOT NULL,
    deleted_at      DATETIME     NULL,
    is_deleted      BOOLEAN      NOT NULL,
    last_login      DATETIME     NULL,
    location_id     BIGINT,

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

CREATE TABLE article_like
(
    article_like_id BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    article_id      BIGINT   NOT NULL,
    user_id         BIGINT   NOT NULL,
    created_at      DATETIME NOT NULL,

    FOREIGN KEY (article_id) REFERENCES articles (article_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE comment_like
(
    comment_like_id BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    comment_id      BIGINT   NOT NULL,
    user_id         BIGINT   NOT NULL,
    created_at      DATETIME NOT NULL,

    FOREIGN KEY (comment_id) REFERENCES comments (comment_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE location
(
    location_id    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    upper_location VARCHAR(255) NOT NULL
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

CREATE TABLE articles_hashtags
(
    article_id BIGINT,
    hashtag_id BIGINT,
    PRIMARY KEY (article_id, hashtag_id),
    FOREIGN KEY (article_id) REFERENCES articles (article_id) ON DELETE CASCADE,
    FOREIGN KEY (hashtag_id) REFERENCES hashtag (hashtag_id) ON DELETE CASCADE
);
```