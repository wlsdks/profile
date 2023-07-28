// stompClient는 웹소켓 연결을 관리하는 객체입니다.
let stompClient = null;

// setConnected 함수는 연결 상태에 따라 버튼의 활성 상태를 변경합니다.
function setConnected(connected) {
    // 연결이 되어 있으면 '연결성공' 버튼을 비활성화하고, '연결해제' 버튼을 활성화합니다.
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    // 연결이 되어 있으면 대화 내용을 보여주고, 그렇지 않으면 숨깁니다.
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    // 대화 내용을 초기화합니다.
    $("#greetings").html("");
}

// connect 함수는 웹소켓 연결을 설정합니다.
function connect() {
    // SockJS 객체를 생성하고, 웹소켓 연결을 설정합니다.
    let socket = new SockJS('http://localhost:8080/ws'); // 서버의 URL을 입력합니다.
    stompClient = Stomp.over(socket);
    // 연결을 설정합니다.
    stompClient.connect({}, function (frame) {
        // 연결이 성공하면, 연결 상태를 업데이트하고, 서버로부터 메시지를 받을 준비를 합니다.
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/subscribe/rooms/roomId', function (greeting) {
            // 서버로부터 메시지를 받으면, 메시지를 화면에 표시합니다.
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

// disconnect 함수는 웹소켓 연결을 해제합니다.
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    // 연결이 해제되면, 연결 상태를 업데이트합니다.
    setConnected(false);
    console.log("Disconnected");
}

// sendName 함수는 입력한 이름을 서버에 전송합니다.
function sendName() {
    let name = $("#name").val();
    let chatRequest = {
        roomId: 'roomId',  // 실제 방 ID로 변경해야 합니다.
        message: name
    };
    // '/publish/messages' 경로로 메시지를 보냅니다.
    stompClient.send("/publish/messages", {}, JSON.stringify(chatRequest));
}

// showGreeting 함수는 메시지를 화면에 표시합니다.
function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

// 페이지가 로드되면, 버튼의 클릭 이벤트 핸들러를 설정합니다.
$(function () {
    // form의 기본 동작(페이지 이동)을 막습니다.
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    // '연결성공' 버튼을 누르면 connect 함수를 호출합니다.
    $("#connect").click(function () {
        connect();
    });
    // '연결해제' 버튼을 누르면 disconnect 함수를 호출합니다.
    $("#disconnect").click(function () {
        disconnect();
    });
    // '전송' 버튼을 누르면 sendName 함수를 호출합니다.
    $("#send").click(function () {
        sendName();
    });
});

