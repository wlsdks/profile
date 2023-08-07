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
        // 팝업창의 크기를 설정합니다.
        const width = 500;
        const height = 500;

        // 화면의 정중앙에 팝업창이 위치하도록 좌표를 계산합니다.
        const left = (window.screen.width / 2) - (width / 2);
        const top = (window.screen.height / 2) - (height / 2);

        // 새 창을 엽니다.
        let chatWindow = window.open('', '', `width=${width},height=${height},left=${left},top=${top}`);

        // 채팅방 창에 HTML과 CSS를 작성합니다.
        chatWindow.document.write(`
        <html>
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                    display: flex;
                    flex-direction: column;
                    height: 100%;
                }
                h1 {
                    background-color: #333;
                    color: #fff;
                    margin: 0;
                    padding: 10px;
                    text-align: center;
                }
                #messages {
                    flex: 1;
                    overflow-y: auto;
                    padding: 10px;
                    border-bottom: 1px solid #ddd;
                }
                #message-input {
                    width: 80%;
                    padding: 10px;
                    border: none;
                    border-radius: 4px;
                }
                #send-button {
                    width: 18%;
                    margin-left: 2%;
                    padding: 10px;
                    border: none;
                    background-color: #007BFF;
                    color: #fff;
                    border-radius: 4px;
                    cursor: pointer;
                }
                #send-button:hover {
                    background-color: #0056b3;
                }
                .message {
                    padding: 10px;
                    margin: 5px;
                    border-radius: 10px;
                    max-width: 70%;
                    clear: both;
                    display: block;
                }
                
                .my-message {
                    background-color: #DCF8C6;
                    margin-right: 30%; /* 왼쪽 정렬을 위한 마진 */
                    text-align: left;
                }
                
                .other-message {
                    background-color: #ECECEC;
                    margin-left: 30%; /* 오른쪽 정렬을 위한 마진 */
                    text-align: right;
                }
            </style>
        </head>
        <body>
            <h1>${room.chatRoomName}</h1>
            <div id="messages"></div>
            <div style="display: flex; padding: 10px;">
                <input id="message-input" type="text">
                <button id="send-button">Send</button>
            </div>
        </body>
        </html>
        `);

        // 'send-button' 요소를 클릭하면 실행할 함수를 설정합니다.
        chatWindow.document.getElementById('send-button').addEventListener('click', function() {
            // 'message-input' 요소의 값을 가져옵니다.
            let message = chatWindow.document.getElementById('message-input').value;
            // 메시지를 전송합니다.
            sendMessage(room, message, chatWindow);
            // 입력 필드를 비웁니다.
            chatWindow.document.getElementById('message-input').value = '';
        });

        // 'message-input' 요소에서 엔터 키를 누르면 'send-button' 요소의 클릭 이벤트를 실행합니다.
        chatWindow.document.getElementById('message-input').addEventListener('keyup', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                // 'message-input' 요소의 값을 가져옵니다.
                let message = chatWindow.document.getElementById('message-input').value;
                // 입력 필드가 비어 있지 않은 경우에만 메시지를 전송합니다.
                if (message.trim() !== '') {
                    chatWindow.document.getElementById('send-button').click();
                }
            }
        });

        stompClient.subscribe('/subscribe/' + currentRoomId, function (greeting) {
            // 받은 메시지를 화면에 표시합니다.
            showGreeting(chatWindow, JSON.parse(greeting.body));
        });

        // 이전 메시지를 불러옵니다.
        $.get("/messages/" + currentRoomId, function (data) {
            // 받아온 각 메시지를 화면에 표시합니다.
            data.forEach(function (data) {
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
        let displayChatRequest = {
            user: currentUser,
            text: message
        };

        // 메시지를 화면에 표시합니다.
        showGreeting(chatWindow, displayChatRequest);

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

        // 메시지를 보낸 후에는 입력 필드를 비웁니다.
        chatWindow.document.getElementById('message-input').value = '';
    }

    /**
     * 메시지를 화면에 표시하는 함수입니다.
     * innerHTML를 사용하여 요소의 내용을 업데이트하면 해당 요소의 기존 내용이 모두 삭제되고 새로운 내용으로 대체된다.
     * 따라서 새로운 메시지를 추가할 때 기존 메시지를 유지하려면 innerHTML 대신 insertAdjacentHTML 메서드를 사용해야 한다.
     */
    function showGreeting(chatWindow, chatRequest) {
        let messageClass = chatRequest.user.userId === currentUser.userId ? 'my-message' : 'other-message';
        let messageElement = `<div class="message ${messageClass}">${chatRequest.user.username}: ${chatRequest.text}</div>`;
        let messagesDiv = chatWindow.document.getElementById('messages');

        // 메시지를 추가합니다.
        messagesDiv.insertAdjacentHTML('beforeend', messageElement);

        // 스크롤을 최하단으로 이동시킵니다.
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
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
