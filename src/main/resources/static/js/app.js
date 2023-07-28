//stomp와 sockJS관련 임포트 혹은 html에서 스크립트를 넣어야한다
let socket = new SockJS('/ws');
let stompClient = Stomp.over(socket);

// 웹소켓 최초 연결함수
stompClient.connect({}, onConnected, onError);

// 웹소켓 연결
function onConnected() {
    setConnected(true);
    // console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
}

// 에러 함수
function onError(error) {
    console.error('Error with websocket', error);
}

// 연결세팅하는 함수
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

// 웹소켓 연결 해제
function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
}

// 이름 전송
function sendName() {
    stompClient.send(
        "/app/hello",
        {},
        JSON.stringify({'name': $("#name").val()})
    );
}

// 인사메시지 보여주기
function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

// 폼 제출 함수
$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendName());
});