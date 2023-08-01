// 현재 로그인한 사용자의 정보를 저장할 변수를 선언합니다.
let currentUser = null;

// '/main/current-user' 엔드포인트로 GET 요청을 보내 현재 로그인한 사용자의 정보를 가져옵니다.
$.get("/current-user", function (data) {
    currentUser = data;  // 받아온 사용자 정보를 currentUser 변수에 저장합니다.

    // '/ws' 엔드포인트에 연결하는 새 SockJS 소켓을 생성합니다.
    let socket = new SockJS('/ws');
    // 생성한 소켓을 사용하여 새 STOMP 클라이언트를 생성합니다.
    let stompClient = Stomp.over(socket);
    // 현재 방 ID를 저장할 변수를 선언합니다.
    let currentRoomId = null;

    // 방 목록을 불러오는 함수입니다.
    function loadRoomList() {
        // '/room-list' 엔드포인트로 GET 요청을 보냅니다.
        $.get("/room-list", function (data) {
            // 방 목록을 비웁니다.
            $("#room-list").empty();
            // 받아온 방 목록에 대해 각각의 방에 대한 클릭 이벤트를 추가합니다.
            data.forEach(function (room) {
                // 방 이름으로 새 li 요소를 생성합니다.
                let roomElement = $("<li>" + room.chatRoomName + "</li>");
                // li 요소를 클릭하면 실행할 함수를 설정합니다.
                roomElement.click(function () {
                    // 클릭한 방의 ID를 저장합니다.
                    currentRoomId = room.id;
                    // 채팅방 창을 엽니다.
                    openChatWindow(room);
                });
                // 생성한 li 요소를 방 목록에 추가합니다.
                $("#room-list").append(roomElement);
            });
        });
    }

    // 채팅방 창을 열고 메시지를 보낼 수 있게 설정하는 함수입니다.
    function openChatWindow(room) {
        // 새 창을 엽니다.
        let chatWindow = window.open('', '', 'width=500,height=500');
        // 채팅방 창에 HTML을 작성합니다.
        chatWindow.document.write('<h1>' + room.chatRoomName + '</h1>');
        chatWindow.document.write('<input id="message-input" type="text">');
        chatWindow.document.write('<button id="send-button">Send</button>');
        chatWindow.document.write('<div id="messages"></div>');

        // 'send-button' 요소를 클릭하면 실행할 함수를 설정합니다.
        chatWindow.document.getElementById('send-button').addEventListener('click', function() {
            // 'message-input' 요소의 값을 가져옵니다.
            let message = chatWindow.document.getElementById('message-input').value;
            // 메시지를 전송합니다.
            sendMessage(room, message, chatWindow);
        });

        stompClient.subscribe('/subscribe/' + currentRoomId, function (greeting) {
            // 받은 메시지를 화면에 표시합니다.
            showGreeting(chatWindow, JSON.parse(greeting.body));
        });

        //지 이전 메시지를 불러옵니다.
        $.get("/messages/" + currentRoomId, function (data) {
            // 받아온 각 메시지를 화면에 표시합니다.
            data.forEach(function (data) {
                // console.log("requestDto: ", data);  // 메시지의 내용을 콘솔에 출력합니다.
                showGreeting(chatWindow, data);
            });
        });
    }

    // 메시지를 전송하는 함수입니다.
    function sendMessage(room, message, chatWindow) {
        // currentUser가 null이면 오류 메시지를 출력하고 함수를 종료합니다.
        if (currentUser === null) {
            console.error('Current user is not set');
            return;
        }

        // 채팅 요청 정보를 설정합니다.
        let chatRequest = {
            userId: currentUser.userId,
            userName: currentUser.username,
            chatRoomId: room.id,
            text: message
        };

        // '/publish' 주제로 메시지를 전송합니다.
        stompClient.send("/publish", {}, JSON.stringify(chatRequest));

        // 메시지를 화면에 표시합니다.
        chatWindow.document.getElementById('messages').innerHTML += '<p>' + currentUser.username + ': ' + message + '</p>';

        // 메시지를 서버에 저장합니다.
        $.ajax({
            url: "/messages",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(chatRequest),
            success: function (data) {
                // 저장된 메시지 정보를 콘솔에 출력합니다.
                // console.log("Message saved: ", data);
            }
        });
    }

    // 메시지를 화면에 표시하는 함수입니다.
    function showGreeting(chatWindow, chatRequest) {
        chatWindow.document.getElementById('messages').innerHTML += '<p>' + chatRequest.user.username + ': ' + chatRequest.text + '</p>';
    }

    // 페이지 로드 완료 시 실행할 함수입니다.
    $(function () {
        // 방 목록을 불러옵니다.
        loadRoomList();

        // 방 생성 폼 제출 이벤트 핸들러를 설정합니다.
        $("#create-room-form").on('submit', function (e) {
            // 기본 제출 이벤트를 막습니다.
            e.preventDefault();
            // 'room-name' 요소의 값을 가져옵니다.
            let roomName = $("#room-name").val();
            // '/create-room' 엔드포인트로 POST 요청을 보냅니다.
            $.ajax({
                url: "/create-room",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({name: roomName}),
                success: function (data) {
                    // 생성된 방 정보를 콘솔에 출력합니다.
                    // console.log("Room created: ", data);
                    // 방 목록을 다시 불러옵니다.
                    loadRoomList();
                }
            });
        });
    });
});
