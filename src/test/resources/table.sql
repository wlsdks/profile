CREATE TABLE IF NOT EXISTS USERS
(
    user_id     BIGINT         NOT NULL    AUTO_INCREMENT,
    login_id    VARCHAR(45)    NOT NULL,
    password    VARCHAR(45)    NOT NULL,
    username    VARCHAR(45)    NOT NULL,
    email       VARCHAR(45)    NOT NULL,
    role        VARCHAR(16)    NOT NULL,
    status      VARCHAR(16)    NOT NULL,
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP      NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS BOARD
(
    board_id    BIGINT         NOT NULL    AUTO_INCREMENT,
    user_id     BIGINT,
    title       VARCHAR(45),
    content     CLOB,
    views       INT,
    likes       INT,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    PRIMARY KEY (board_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id)
);

CREATE TABLE IF NOT EXISTS BOARD_COMMENT
(
    board_comment_id  BIGINT      NOT NULL    AUTO_INCREMENT,
    board_id          BIGINT,
    user_id           BIGINT,
    content           CLOB,
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP,
    PRIMARY KEY (board_comment_id),
    FOREIGN KEY (board_id) REFERENCES BOARD(board_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id)
);

CREATE TABLE IF NOT EXISTS CHAT_ROOM
(
    chatroom_id    BIGINT         NOT NULL    AUTO_INCREMENT,
    chatroom_name  VARCHAR(45),
    PRIMARY KEY (chatroom_id)
);

CREATE TABLE IF NOT EXISTS CHAT_MAP
(
    chat_map_id    BIGINT         NOT NULL    AUTO_INCREMENT,
    chat_map_user_id  BIGINT,
    chat_map_chatroom_id BIGINT,
    PRIMARY KEY (chat_map_id),
    FOREIGN KEY (chat_map_user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (chat_map_chatroom_id) REFERENCES CHAT_ROOM(chatroom_id)
);


CREATE TABLE IF NOT EXISTS MESSAGE
(
    message_id   BIGINT      NOT NULL    AUTO_INCREMENT,
    user_id      BIGINT,
    chatroom_id  BIGINT,
    text         CLOB,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    PRIMARY KEY (message_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (chatroom_id) REFERENCES CHAT_ROOM(chatroom_id)
);

CREATE TABLE IF NOT EXISTS BOARD_SUB_COMMENT
(
    board_sub_comment_id  BIGINT      NOT NULL    AUTO_INCREMENT,
    board_comment_id      BIGINT,
    user_id               BIGINT,
    content               CLOB,
    created_at            TIMESTAMP,
    updated_at            TIMESTAMP,
    PRIMARY KEY (board_sub_comment_id),
    FOREIGN KEY (board_comment_id) REFERENCES BOARD_COMMENT(board_comment_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id)
);

CREATE TABLE IF NOT EXISTS USER_DETAILS
(
    user_details_id  BIGINT         NOT NULL    AUTO_INCREMENT,
    user_id          BIGINT,
    provider         VARCHAR(45),
    provider_id      VARCHAR(45),
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    PRIMARY KEY (user_details_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id)
);

CREATE TABLE IF NOT EXISTS FILE
(
    file_id     BIGINT         NOT NULL    AUTO_INCREMENT,
    user_id     BIGINT,
    board_id    BIGINT,
    file_name   VARCHAR(45),
    file_url    VARCHAR(45),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    PRIMARY KEY (file_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (board_id) REFERENCES BOARD(board_id)
);

CREATE TABLE IF NOT EXISTS BOARD_REPORT
(
    board_report_id  BIGINT      NOT NULL    AUTO_INCREMENT,
    reporter_id      BIGINT,
    reported_id      BIGINT,
    board_id         BIGINT,
    reason           CLOB,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    PRIMARY KEY (board_report_id),
    FOREIGN KEY (board_id) REFERENCES BOARD(board_id),
    FOREIGN KEY (reporter_id) REFERENCES USERS(user_id),
    FOREIGN KEY (reported_id) REFERENCES USERS(user_id)
);

CREATE TABLE IF NOT EXISTS BOARD_COMMENT_REPORT
(
    board_comment_report_id  BIGINT      NOT NULL    AUTO_INCREMENT,
    comment_reporter_id      BIGINT,
    comment_reported_id      BIGINT,
    board_comment_id         BIGINT,
    reason                   CLOB,
    created_at               TIMESTAMP,
    updated_at               TIMESTAMP,
    PRIMARY KEY (board_comment_report_id),
    FOREIGN KEY (board_comment_id) REFERENCES BOARD_COMMENT(board_comment_id),
    FOREIGN KEY (comment_reporter_id) REFERENCES USERS(user_id),
    FOREIGN KEY (comment_reported_id) REFERENCES USERS(user_id)
);

CREATE TABLE IF NOT EXISTS BOARD_SUB_COMMENT_REPORT
(
    board_sub_comment_report_id  BIGINT      NOT NULL    AUTO_INCREMENT,
    board_sub_reporter_id        BIGINT,
    board_sub_reported_id        BIGINT,
    board_sub_comment_id         BIGINT,
    reason                       CLOB,
    created_at                   TIMESTAMP,
    updated_at                   TIMESTAMP,
    PRIMARY KEY (board_sub_comment_report_id),
    FOREIGN KEY (board_sub_comment_id) REFERENCES BOARD_SUB_COMMENT(board_sub_comment_id),
    FOREIGN KEY (board_sub_reporter_id) REFERENCES USERS(user_id),
    FOREIGN KEY (board_sub_reported_id) REFERENCES USERS(user_id)
);

CREATE TABLE IF NOT EXISTS MESSAGE_REPORT
(
    message_report_id  BIGINT      NOT NULL    AUTO_INCREMENT,
    reporter_id        BIGINT,
    reported_id        BIGINT,
    message_id         BIGINT,
    reason             CLOB,
    created_at         TIMESTAMP,
    updated_at         TIMESTAMP,
    PRIMARY KEY (message_report_id),
    FOREIGN KEY (reporter_id) REFERENCES USERS(user_id),
    FOREIGN KEY (reported_id) REFERENCES USERS(user_id),
    FOREIGN KEY (message_id) REFERENCES MESSAGE(message_id)
);