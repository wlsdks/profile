// 방 생성 폼 제출 이벤트 핸들러
$("#create-room-form").on('submit', function (e) {
    e.preventDefault();  // 기본 제출 이벤트를 막습니다.
    let roomName = $("#room-name").val();  // 입력한 방 이름을 가져옵니다.
    $.ajax({
        url: "/create-room",  // 방 생성 요청을 보낼 URL입니다.
        type: "POST",  // HTTP 메서드는 POST입니다.
        contentType: "application/json",  // 요청 본문의 타입은 JSON입니다.
        data: JSON.stringify({name: roomName}),  // 방 이름을 JSON으로 변환하여 요청 본문에 포함시킵니다.
        success: function (data) {  // 요청이 성공하면 실행할 콜백 함수입니다.
            console.log("Room created: ", data);  // 생성된 방 정보를 콘솔에 출력합니다.
            loadRoomList();  // 방 목록을 다시 불러옵니다.
        }
    });
});

let socket = new SockJS('/ws');  // '/ws' 엔드포인트에 연결하는 새 SockJS 소켓을 생성합니다.
let stompClient = Stomp.over(socket);  // 생성한 소켓을 사용하여 새 STOMP 클라이언트를 생성합니다.
let currentRoomId = null;  // 현재 방 ID를 저장할 전역 변수입니다.

// 방 목록을 불러오는 함수입니다.
function loadRoomList() {
    $.get("/room-list", function (data) {  // '/room-list' 엔드포인트로 GET 요청을 보냅니다.
        $("#room-list").empty();  // 방 목록을 비웁니다.
        // 받아온 방 목록에 대해 각각의 방에 대한 클릭 이벤트를 추가합니다.
        data.forEach(function (room) {
            let roomElement = $("<li>" + room.chatRoomName + "</li>");  // 방 이름으로 새 li 요소를 생성합니다.
            roomElement.click(function () {  // li 요소를 클릭하면 실행할 함수를 설정합니다.
                currentRoomId = room.id;  // 클릭한 방의 ID를 저장합니다.
                openChatWindow(room);  // 채팅방 창을 엽니다.
            });
            $("#room-list").append(roomElement);  // 생성한 li 요소를 방 목록에 추가합니다.
        });
    });
}

// 채팅방 창을 열고 메시지를 보낼 수 있게 설정
function openChatWindow(room) {
    let chatWindow = window.open('', '', 'width=500,height=500');
    chatWindow.document.write('<h1>' + room.chatRoomName + '</h1>');
    chatWindow.document.write('<input id="message-input" type="text">');
    chatWindow.document.write('<button id="send-button">Send</button>');
    chatWindow.document.write('<div id="messages"></div>');

    chatWindow.document.getElementById('send-button').addEventListener('click', function() {
        let message = chatWindow.document.getElementById('message-input').value;
        sendMessage(room, message, chatWindow);
    });

    // 해당 방의 메시지를 구독합니다.
    stompClient.subscribe('/subscribe/rooms/' + currentRoomId, function (greeting) {
        showGreeting(chatWindow, JSON.parse(greeting.body).content);  // 받은 메시지를 화면에 표시합니다.
    });
}

// 메시지를 전송하는 함수입니다.
function sendMessage(room, message, chatWindow) {
    let chatRequest = {
        senderId: 1,  // 실제 사용자 ID로 변경해야 합니다.
        receiverId: 2,  // 실제 수신자 ID로 변경해야 합니다.
        roomId: room.id,  // 클릭한 방의 ID를 사용합니다.
        message: message  // 입력한 메시지를 설정합니다.
    };
    // '/publish/messages' 주제로 메시지를 전송합니다.
    stompClient.send("/publish/messages", {}, JSON.stringify(chatRequest));

    // 메시지를 화면에 표시합니다.
    chatWindow.document.getElementById('messages').innerHTML += '<p>You: ' + message + '</p>';
}

// 메시지를 화면에 표시하는 함수입니다.
function showGreeting(chatWindow, message) {
    chatWindow.document.getElementById('messages').innerHTML += '<p>Other: ' + message + '</p>';
}

// 페이지 로드 완료 시 실행할 함수입니다.
$(function () {
    loadRoomList();  // 방 목록을 불러옵니다.
    let stompClient = null;  // STOMP 클라이언트를 저장할 변수입니다.

    // 연결 상태를 설정하는 함수입니다.
    function setConnected(connected) {
        $("#connect").prop("disabled", connected);  // 연결 버튼의 활성 상태를 설정합니다.
        $("#disconnect").prop("disabled", !connected);  // 연결 해제 버튼의 활성 상태를 설정합니다.
        if (connected) {
            $("#conversation").show();  // 연결되면 대화 영역을 표시합니다.
        } else {
            $("#conversation").hide();  // 연결이 해제되면 대화 영역을 숨깁니다.
        }
        $("#greetings").html("");  // 메시지 영역을 비웁니다.
    }

    // 서버에 연결하는 함수입니다.
    function connect() {
        let socket = new SockJS('http://localhost:8080/ws');  // 새 SockJS 소켓을 생성합니다.
        stompClient = Stomp.over(socket);  // 생성한 소켓을 사용하여 새 STOMP 클라이언트를 생성합니다.
        stompClient.connect({}, function (frame) {  // 서버에 연결합니다.
            setConnected(true);  // 연결 상태를 설정합니다.
            console.log('Connected: ' + frame);  // 연결 정보를 콘솔에 출력합니다.
            // '/subscribe/rooms/roomId' 주제의 메시지를 구독합니다.
            stompClient.subscribe('/subscribe/rooms/roomId', function (greeting) {
                showGreeting(JSON.parse(greeting.body).content);  // 받은 메시지를 화면에 표시합니다.
            });
        });
    }

    // 서버와의 연결을 끊는 함수입니다.
    function disconnect() {
        if (stompClient !== null) {  // STOMP 클라이언트가 null이 아니라면
            stompClient.disconnect();  // 서버와의 연결을 끊습니다.
        }
        setConnected(false);  // 연결 상태를 설정합니다.
        console.log("Disconnected");  // 연결 해제 상태를 콘솔에 출력합니다.
    }

    // 페이지 로드 완료 시 실행할 함수입니다.
    $(function () {
        $("form").on('submit', function (e) {
            e.preventDefault();  // 기본 제출 이벤트를 막습니다.
        });
        $("#connect").click(function () {
            connect();  // 연결 버튼을 클릭하면 서버에 연결합니다.
        });
        $("#disconnect").click(function () {
            disconnect();  // 연결 해제 버튼을 클릭하면 서버와의 연결을 끊습니다.
        });
        $("#send").click(function () {
            sendMessage();  // 메시지 전송 버튼을 클릭하면 메시지를 전송합니다.
        });
    });
});

