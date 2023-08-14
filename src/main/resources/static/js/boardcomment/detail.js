// 페이지 로드가 완료되면 댓글을 불러옵니다.
$(document).ready(function () {
    let boardId = $('.edit-button').data('board-id'); // boardId 가져오기
    getComments(boardId, currentPage); // 첫 페이지 댓글 불러오기
});

// 게시글 수정하기 버튼 클릭시 동작
$('.edit-button').click(function (e) {
    e.preventDefault(); // 기본 링크 동작을 막습니다.
    let boardId = $(this).data('board-id'); // data-board-id 속성에서 boardId 가져오기
    let url = '/board/update/validUser?boardId=' + boardId; // 검증 URL에 boardId 포함

    // 사용자 검증 요청을 보냅니다.
    $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            // 검증 성공 시 수정 페이지로 이동합니다.
            debugger
            window.location.href = '/board/update/' + boardId;
        },
        error: function (xhr, status, error) {
            // 검증 실패 시 에러 메시지를 표시합니다.
            alert('유저가 다릅니다. 수정할 수 없습니다.');
        }
    });
});


// READ - 댓글을 받아와서 뿌려주는 로직 (페이징 적용)
let currentPage = 1;

function getComments(boardId, page) {
    $.ajax({
        url: '/board/comment/get/' + boardId,
        method: 'GET',
        data: {page: page - 1},
        success: function (response) {
            let comments = response.content;
            let commentListHtml = '';
            comments.forEach(function (comment) {
                commentListHtml += `<div class="comment" data-comment-id="${comment.boardCommentId}" onmouseover="showActions(this)" onmouseout="hideActions(this)">`;
                commentListHtml += `<span class="author">${comment.userResponse.username}:</span>`;
                commentListHtml += `<div class="content">${comment.content}</div>`;
                commentListHtml += `<div class="actions" style="display:none;">`; // 클래스 이름을 원래대로 돌림
                commentListHtml += `<button onclick="editComment(${comment.boardCommentId})">수정</button>`; // 클래스 제거
                commentListHtml += `<button onclick="deleteComment(${comment.boardCommentId})">삭제</button>`; // 클래스 제거
                commentListHtml += `</div>`;
                commentListHtml += `</div>`;
            });
            $('#comment-list').html(commentListHtml);

            // 페이징 처리
            let totalPages = response.totalPages; // 전체 페이지 수
            let paginationHtml = '';
            for (let i = 1; i <= totalPages; i++) {
                paginationHtml += `<button onclick="getComments(${boardId}, ${i})">${i}</button>`;
            }
            $('#pagination').html(paginationHtml);
        }
    });
}

// 댓글 작성 함수 CREATE
function addComment() {
    let boardId = $('.edit-button').data('board-id');
    let content = $('#comment-input').val();
    $.ajax({
        url: '/board/comment/create/' + boardId, // 경로에 boardId 포함
        method: 'POST',
        contentType: 'application/json', // Content-Type 설정
        data: JSON.stringify({content: content}), // JSON 형식으로 변환
        success: function () {
            // 댓글을 다시 불러온다.
            let boardId = $('.edit-button').data('board-id');
            getComments(boardId, currentPage);
            $('#comment-input').val(''); // 입력창을 비웁니다.
        }
    });
}

// UPDATE - 댓글 수정창을 보여주도록 하는 함수
function editComment(commentId) {
    // 사용자 검증 요청
    $.ajax({
        url: '/board/comment/validComment?commentId=' + commentId,
        type: 'GET',
        success: function (response) {
            // 댓글 요소 선택
            let commentElement = $(`.comment[data-comment-id=${commentId}]`);

            // 현재 댓글 내용 가져오기
            let currentContent = commentElement.find('.content').text();

            // 댓글 내용을 입력창으로 바꾸기
            commentElement.find('.content').html(`
                <textarea class="edit-input">${currentContent}</textarea>
                <button onclick="updateComment(${commentId})">수정 완료</button>
            `);
        },
        error: function (xhr, status, error) {
            alert(xhr.responseText); // 서버에서 보낸 에러 메시지를 표시
        }
    });
}

// UPDATE - 댓글 수정 완료후 저장 요청하는 함수
function updateComment(commentId) {
    let newContent = $(`.comment[data-comment-id=${commentId}] .edit-input`).val();

    // 서버로 수정된 내용 전송
    $.ajax({
        url: '/board/comment/update/' + commentId,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({content: newContent}),
        success: function () {
            // 댓글 목록 다시 불러오기
            let boardId = $('.edit-button').data('board-id');
            getComments(boardId, currentPage);
        },
        error: function (xhr, status, error) {
            alert(xhr.responseText); // 서버에서 보낸 에러 메시지를 표시
        }
    });
}


// 댓글 삭제 함수 DELETE
function deleteComment(commentId) {
    $.ajax({
        url: '/board/comment/delete/' + commentId,
        type: 'DELETE',
        success: function () {
            // alert('댓글이 삭제되었습니다.');

            // 댓글 목록을 다시 불러온다.
            let boardId = $('.edit-button').data('board-id');
            getComments(boardId, currentPage);
        },
        error: function (xhr, status, error) {
            alert('유저가 다릅니다. 삭제할 수 없습니다.');
        }
    });
}


// 마우스 오버 시 액션 버튼 표시
function showActions(element) {
    $(element).find('.actions').show();
}

// 마우스 아웃 시 액션 버튼 숨김
function hideActions(element) {
    $(element).find('.actions').hide();
}


function likePost() {
    alert('좋아요를 눌렀습니다!');
    // 여기에 좋아요 기능을 처리하는 AJAX 코드를 추가할 수 있습니다.
}
