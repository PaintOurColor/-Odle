const api_url = "http://localhost:8080/"; //공통 Url
const url_postId = document.location.href.split('=')[1];

$(document).ready(function () {
    getUserList() //유저 목록 가져오기
});

$(document).ready(function () {
    var pageId = $('body').attr('id');
    if (pageId === 'admin_activation') {
        getUserList();
    } else if (pageId === 'admin_post') {
        getPosts();
    } else if (pageId === 'admin_comment') {
        getPost(url_postId);
        getComment(url_postId);
    }
});

// 유저 목록 가져오기
function getUserList() {
    $.ajax({
        url: api_url + "users",
        type: "GET",
        contentType: "json",
        success: function (response) {
            $('#user_card_list').empty(); // 유저 틀 초기화

            for (let i = 0; i < response.length; i++) {
                const userId = response[i]['userId'] // 유저 아이디
                const username = response[i]['username'] // 유저 이름
                const email = response[i]['email'] //유저 이메일
                let activation = response[i]['activation'] //활성화 여부

                const user_temp = `
                <div id="user_card">
                <span>유저 아이디: </span><span id="user_id">${userId}</span>
                <span>유저 이메일: </span><span id="user_email">${email}</span>
                <span>유저 이름: </span><span id="user_username">${username}</span>
                <span>활성화 여부: </span><span id="user_activation_${userId}">${activation}</span>
                <button id="user_active" onclick = "activateUser(${userId})">활성화</button>
                <button id="user_inactive" onclick = "inactivateUser(${userId})">비활성화</button>
                </div>
                `
                $('#user_card_list').append(user_temp)
            }
        }
    });
}

//유저 활성화
function activateUser(userId) {
    $.ajax({
        url: api_url + "users/" + userId + "/activation/admin",
        type: "PATCH",
        contentType: "application/json; charset=UTF-8",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        data: JSON.stringify({
            "password": $('#admin_password').val()
        }),
        success: function (response) {
            $('#user_activation_' + userId).text("ACTIVE")
        },
        error: function (response) {
            const errorMessage = response.responseJSON['errorMessage']
            if (errorMessage) {
                alert(errorMessage);
            } else {
                alert("에러: " + response.status)
            }
        }
    });
}

//유저 비활성화
function inactivateUser(userId) {
    $.ajax({
        url: api_url + "users/" + userId + "/activation/admin",
        type: "PATCH",
        contentType: "application/json; charset=UTF-8",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        data: JSON.stringify({
            "password": $('#admin_password').val()
        }),
        success: function (response) {
            $('#user_activation_' + userId).text("INACTIVE")
        },
        error: function (response) {
            const errorMessage = response.responseJSON['errorMessage']
            if (errorMessage) {
                alert(errorMessage);
            } else {
                alert("에러: " + response.status)
            }
        }
    });
}


// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ게시글 삭제 관련ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

// 게시글 목록 가져오기
function getPosts() {
    $.ajax({
        url: api_url + "posts",
        type: "GET",
        contentType: "json",
        success: function (response) {
            $('#post_list').empty(); // 게시글 틀 초기화

            for (let i = 0; i < response.length; i++) {
                const postId = response[i]['id'] // 게시글 아이디
                const username = response[i]['username'] // 작성자
                const createdAt = response[i]['createdAt'] // 작성 시간
                const openOrEnd = response[i]['openOrEnd'] // 여는/닫는 노래
                const musicTitle = response[i]['musicTitle'] // 노래 제목

                const post_temp = `
                <div id="post_box">
                <a onclick="window.location.href='./admin_comment.html?postId=${postId}'" role="link" tabindex="0">
                <span>게시글 id: </span><span id="post_id">${postId}</span>
                <span>작성자: </span><span id="post_username">${username}</span>
                <span>작성 시간: </span><span id="post_createdAt">${createdAt}</span>
                <span>여는/닫는 노래: </span><span id="post_openOrEnd"${openOrEnd}></span>
                <span>노래 이름: </span><span id="post_musicId">${musicTitle}</span>
                <button id="post_delete${postId}" onclick = "deletePost(${postId})">게시글 삭제</button>
                </a>
                </div>
                `
                $('#post_list').append(post_temp)
            }
        }
    });
}

//게시글 삭제
function deletePost(postId) {
    $.ajax({
        url: api_url + "posts/" + postId + "/admin",
        type: "DELETE",
        contentType: "application/json; charset=UTF-8",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            $('#post_delete' + postId).text("삭제 완료")
            alert(response['message'])
        },
        error: function (response) {
            const errorMessage = response.responseJSON['errorMessage']
            if (errorMessage) {
                alert(errorMessage);
            } else {
                alert("에러: " + response.status)
            }
        }
    });
}

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글 삭제 관련ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

//게시글 1개 불러오기
function getPost(url_postId) {
    $.ajax({
        url: api_url + "posts/" + url_postId,
        type: "GET",
        contentType: "json",
        success: function (response) {
            $('#post_info').empty(); // 게시글 틀 초기화

            const postId = response['id'] // 게시글 아이디
            const username = response['username'] // 작성자
            const createdAt = response['createdAt'] // 작성 시간
            const openOrEnd = response['openOrEnd'] // 여는/닫는 노래
            const musicTitle = response['musicTitle'] // 노래 제목

            const post_temp = `
            <div id="post_box">
            <span>게시글 id: </span><span id="comment_postId">${postId}</span>
            <span>작성자: </span><span id="comment_postUsername">${username}</span>
            <span>작성 시간: </span><span id="comment_postCreatedAt">${createdAt}</span>
            <span>여는/닫는 노래: </span><span id="comment_postOpenOrEnd">${openOrEnd}</span>
            <span>노래 이름: </span><span id="comment_postMusicTitle">${musicTitle}</span>
            <button id="post_delete${postId}" onclick="deletePost(${postId})">게시글 삭제</button>
            `
            $('#post_info').append(post_temp)
        }
    });
}

//댓글 조회
function getComment(postId) {
    $.ajax({
        url: api_url + "posts/" + postId + "/comments",
        type: "GET",
        contentType: "json",
        success: function (response) {
            $('#comment_list').empty(); // 댓글 틀 초기화
            const responseContent = response['content'];
            console.log(responseContent)

            for (let i = 0; i < responseContent.length; i++) {
                const commentId = responseContent[i]['commentId'] // 게시글 아이디
                const username = responseContent[i]['username'] // 작성자
                const content = responseContent[i]['content'] // 작성 시간
                const comment_temp = `
                <div id="comment_box">
                <span>댓글 id: </span><span id="comment_id">${commentId}</span>
                <span>작성자: </span><span id="comment_username">${username}</span>
                <span>내용: </span><span id="comment_content">${content}</span>
                <button id="comment_delete${commentId}" onclick="deleteComment(${commentId})">댓글 삭제</button>
                </div>
                `
                $('#comment_list').append(comment_temp)
            }
        }
    });
}




//댓글 
function deleteComment(commentId) {
    $.ajax({
        url: api_url + "posts/" + url_postId + "/comments/" + commentId + "/admin",
        type: "DELETE",
        contentType: "application/json; charset=UTF-8",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            $('#comment_delete' + commentId).text("삭제 완료")
            alert(response['message'])
        },
        error: function (response) {
            const errorMessage = response.responseJSON['errorMessage']
            if (errorMessage) {
                alert(errorMessage);
            } else {
                alert("에러: " + response.status)
            }
        }
    });
}
