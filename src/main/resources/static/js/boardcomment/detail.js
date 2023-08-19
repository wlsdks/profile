$(document).ready(function() {
    const showSubComments = sessionStorage.getItem("showSubComments");
    if (showSubComments) {
        toggleSubCommentsList(showSubComments);
        sessionStorage.removeItem("showSubComments"); // 사용 후 값 삭제
    }
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
            window.location.href = '/board/update/' + boardId;
        },
        error: function (xhr, status, error) {
            // 검증 실패 시 에러 메시지를 표시합니다.
            alert('유저가 다릅니다. 수정할 수 없습니다.');
        }
    });
});

let scrollPosition = 0; // 초기 스크롤 위치

// 페이지 로딩 시 스크롤 위치 저장
$(document).ready(function () {
    scrollPosition = $(window).scrollTop();
});

// 댓글 추가 후 리로딩
function addComment() {
    let boardId = $('.edit-button').data('board-id');
    let content = $('#comment-input').val();
    $.ajax({
        url: '/board/comment/create/' + boardId,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({content: content}),
        success: function () {
            // 페이지 리로딩 후 스크롤 위치 이동
            location.reload();
        }
    });
}

// 페이지 리로딩 후 스크롤 위치 이동
$(window).on('load', function () {
    $(window).scrollTop(scrollPosition);
});


// UPDATE - 댓글 수정창을 보여주도록 하는 함수
function editComment(commentId) {
    let commentElement = $(`.comment[data-comment-id=${commentId}]`);
    let currentContent = commentElement.find('.original-content').text();

    // 원래의 댓글 내용을 숨기고 수정 창을 표시합니다.
    commentElement.find('.comment-content').hide();
    commentElement.find('.comment-edit-form').show();
    commentElement.find('.edit-input').val(currentContent);
}

// 댓글 수정창 [취소] 버튼 누르면 동작
function cancelEdit(commentId) {
    let commentElement = $(`.comment[data-comment-id=${commentId}]`);

    // 수정 창을 숨기고 원래의 댓글 내용을 다시 표시합니다.
    commentElement.find('.comment-edit-form').hide();
    commentElement.find('.comment-content').show();
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
            // 페이지를 다시 로드
            location.reload();
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
            // 페이지를 다시 로드
            location.reload();
        },
        error: function (xhr, status, error) {
            alert(xhr.responseText);
        }
    });
}


/** 대댓글 영역 작성/추가 **/
function toggleSubCommentsList(commentId) {
    // 대댓글 리스트를 토글합니다.
    $(`.comment[data-comment-id=${commentId}]`).next('.subcomment-form').next('ul').toggle();
}

function showSubCommentForm(commentId) {
    // 대댓글 작성 폼만 표시합니다.
    $(`#subcomment-form-${commentId}`).show();
}

function hideSubCommentForm(commentId) {
    $("#subcomment-form-" + commentId).hide();
}

// 대댓글 추가하는 함수
function addSubComment(commentId) {
    let content = $(`#subcomment-input-${commentId}`).val();

    $.ajax({
        url: '/board/subcomment/ajax/create',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            boardSubCommentId: commentId,
            content: content
        }),
        success: function () {
            console.log(content);
            // alert('대댓글이 추가되었습니다.');
            sessionStorage.setItem("showSubComments", commentId);
            location.reload();
        },
        error: function (xhr, status, error) {
            alert(xhr.responseText);
        }
    });
}

/** 대댓글 수정/삭제 영역 **/
function editSubComment(subCommentId) {
    let subCommentElement = $(`.sub-comment[data-subcomment-id=${subCommentId}]`);
    let currentSubContent = subCommentElement.find('.original-subcontent').text();

    subCommentElement.find('.original-subcontent').hide();
    subCommentElement.find('.subcomment-edit-form').show();
    subCommentElement.find('.edit-subinput').val(currentSubContent);
}

function cancelSubEdit(subCommentId) {
    let subCommentElement = $(`.sub-comment[data-subcomment-id=${subCommentId}]`);
    subCommentElement.find('.original-subcontent').show();
    subCommentElement.find('.subcomment-edit-form').hide();
}

function updateSubComment(subCommentId) {
    // AJAX 요청을 사용하여 서버에 대댓글 수정 내용 전송
    // 성공적으로 수정되면 원래 내용을 수정된 내용으로 변경하고 수정 폼을 숨깁니다.
}

function deleteSubComment(subCommentId) {
    // AJAX 요청을 사용하여 서버에 대댓글 삭제 요청 전송
    // 성공적으로 삭제되면 해당 대댓글을 DOM에서 제거합니다.
}




function likePost() {
    alert('좋아요를 눌렀습니다!');
    // 여기에 좋아요 기능을 처리하는 AJAX 코드를 추가할 수 있습니다.
}