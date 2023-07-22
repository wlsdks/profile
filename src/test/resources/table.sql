CREATE TABLE USERS
(
    user_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(45) NOT NULL,
    email       VARCHAR(45) NOT NULL,
    password    VARCHAR(45) NOT NULL,
    role        VARCHAR(45) NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL
);

CREATE TABLE BOARD
(
    board_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT,
    title      VARCHAR(45),
    content    CLOB,
    views      INT,
    likes      INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE BOARD_COMMENT
(
    board_comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id         BIGINT,
    user_id          BIGINT,
    content          CLOB,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP
);

CREATE TABLE MESSAGE
(
    message_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id   BIGINT,
    receiver_id BIGINT,
    content     CLOB,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE BOARD_SUB_COMMENT
(
    board_sub_comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id           BIGINT,
    user_id              BIGINT,
    content              CLOB,
    created_at           TIMESTAMP,
    updated_at           TIMESTAMP
);

CREATE TABLE USER_DETAILS
(
    user_details_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT,
    provider        VARCHAR(45),
    provider_id     VARCHAR(45),
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP
);

CREATE TABLE FILE
(
    file_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT,
    board_id   BIGINT,
    file_name  VARCHAR(45),
    file_url   VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE BOARD_REPORT
(
    board_report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id     BIGINT,
    reportee_id     BIGINT,
    board_id        BIGINT,
    reason          CLOB,
    created_at      TIMESTAMP
);

CREATE TABLE BOARD_COMMENT_REPORT
(
    board_comment_report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id             BIGINT,
    reportee_id             BIGINT,
    comment_id              BIGINT,
    reason                  CLOB,
    created_at              TIMESTAMP
);

CREATE TABLE BOARD_SUB_COMMENT_REPORT
(
    board_sub_comment_report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id                 BIGINT,
    reportee_id                 BIGINT,
    sub_comment_id              BIGINT,
    reason                      CLOB,
    created_at                  TIMESTAMP
);

CREATE TABLE MESSAGE_REPORT
(
    message_report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id       BIGINT,
    reportee_id       BIGINT,
    message_id        BIGINT,
    reason            CLOB,
    created_at        TIMESTAMP
);


-- BOARD
ALTER TABLE BOARD ADD FOREIGN KEY (user_id) REFERENCES USERS(user_id);

-- BOARD_COMMENT
ALTER TABLE BOARD_COMMENT ADD FOREIGN KEY (board_id) REFERENCES BOARD(board_id);
ALTER TABLE BOARD_COMMENT ADD FOREIGN KEY (user_id) REFERENCES USERS(user_id);

-- MESSAGE
ALTER TABLE MESSAGE ADD FOREIGN KEY (sender_id) REFERENCES USERS(user_id);
ALTER TABLE MESSAGE ADD FOREIGN KEY (receiver_id) REFERENCES USERS(user_id);

-- BOARD_SUB_COMMENT
ALTER TABLE BOARD_SUB_COMMENT ADD FOREIGN KEY (comment_id) REFERENCES BOARD_COMMENT(board_comment_id);
ALTER TABLE BOARD_SUB_COMMENT ADD FOREIGN KEY (user_id) REFERENCES USERS(user_id);

-- USER_DETAILS
ALTER TABLE USER_DETAILS ADD FOREIGN KEY (user_id) REFERENCES USERS(user_id);

-- FILE
ALTER TABLE FILE ADD FOREIGN KEY (user_id) REFERENCES USERS(user_id);
ALTER TABLE FILE ADD FOREIGN KEY (board_id) REFERENCES BOARD(board_id);

-- BOARD_REPORT
ALTER TABLE BOARD_REPORT ADD FOREIGN KEY (board_id) REFERENCES BOARD(board_id);
ALTER TABLE BOARD_REPORT ADD FOREIGN KEY (reporter_id) REFERENCES USERS(user_id);
ALTER TABLE BOARD_REPORT ADD FOREIGN KEY (reportee_id) REFERENCES USERS(user_id);

-- BOARD_COMMENT_REPORT
ALTER TABLE BOARD_COMMENT_REPORT ADD FOREIGN KEY (comment_id) REFERENCES BOARD_COMMENT(board_comment_id);
ALTER TABLE BOARD_COMMENT_REPORT ADD FOREIGN KEY (reporter_id) REFERENCES USERS(user_id);
ALTER TABLE BOARD_COMMENT_REPORT ADD FOREIGN KEY (reportee_id) REFERENCES USERS(user_id);

-- BOARD_SUB_COMMENT_REPORT
ALTER TABLE BOARD_SUB_COMMENT_REPORT ADD FOREIGN KEY (sub_comment_id) REFERENCES BOARD_SUB_COMMENT(board_sub_comment_id);
ALTER TABLE BOARD_SUB_COMMENT_REPORT ADD FOREIGN KEY (reporter_id) REFERENCES USERS(user_id);
ALTER TABLE BOARD_SUB_COMMENT_REPORT ADD FOREIGN KEY (reportee_id) REFERENCES USERS(user_id);

-- MESSAGE_REPORT
ALTER TABLE MESSAGE_REPORT ADD FOREIGN KEY (message_id) REFERENCES MESSAGE(message_id);
ALTER TABLE MESSAGE_REPORT ADD FOREIGN KEY (reporter_id) REFERENCES USERS(user_id);
ALTER TABLE MESSAGE_REPORT ADD FOREIGN KEY (reportee_id) REFERENCES USERS(user_id);
