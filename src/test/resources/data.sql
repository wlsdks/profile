INSERT INTO USERS (username, email, password, role, created_at, updated_at)
VALUES ('wlsdks12', 'wlsdks12@naver.com', '{noop}wlsdks12', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('user2', 'user2@email.com', '{noop}password2', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('user3', 'user3@email.com', '{noop}password3', 'ROLE_USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into CHAT_ROOM (chatroom_name)
values ('jinanRoom');
