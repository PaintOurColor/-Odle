// Get the modal
var post_modal = document.getElementById('post_modal');

// When the user clicks on the button, open the modal 
function open_post_popup(post_id) {
    post_modal.style.display = "block";
    get_post(post_id);
}

// When the user clicks on <span> (x), close the modal
function close_button() {
    post_modal.style.display = "none";
    follower_modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == post_modal) {
        post_modal.style.display = "none";
    } else if(event.target == follower_modal) {
        follower_modal.style.display = "none";
    }
}

function get_post(post_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id,
        type: "GET",
        dataType: "json",
        success: function (response) {
            $('#post_popup').empty()
            const post_user_id = response.userId;
            const post_username = response.username;
            let post_profileImage = response.userProfileImage;
            const post_music_id = response.musicId;
            const music_title = response.musicTitle;
            const music_singer = response.musicSinger;
            const music_cover = response.musicCover;
            const like_count = response.likeCount;
            const content = response.content;
            const open_end = response.openOrEnd;
            const emotion = response.emotion;
            const tag_list = response.tagList;
            const comment_count = response.commentCount;
            const created_at = new Date(response.createdAt);
            const modified_at = new Date(response.modifiedAt);
            const createdYear = created_at.getFullYear().toString().slice(2);
            const createdMonth = created_at.getMonth() + 1;
            const createdDate = created_at.getDate();
            const modifiedYear = modified_at.getFullYear().toString().slice(2);
            const modifiedMonth = modified_at.getMonth() + 1;
            const modifiedDate = modified_at.getDate();
            const delete_button_style = (post_user_id === login_userId) ? '' : 'display: none';
            console.log(delete_button_style, post_user_id, login_userId);
            post_profileImage = (post_profileImage ==null)? "http://bwptedu.com/assets/image/default-profile.jpg":post_profileImage;
            const temp_post = `
            <span class="close" onclick="close_button()">&times;</span>
            <div class="post_container">
            <div class="post_info_container">
                <p id="post_userId" style="display: none">${post_user_id}</p>
                <p id="my_profileImage">
                    <img src="${post_profileImage}"></a>
                </p>
                <div class="post_info">
                    <span id="my_username">${post_username}</span>
                    <div class="post_time">
                        <span>작성일: ${createdYear}/${createdMonth < 10 ? '0' + createdMonth : createdMonth}/${createdDate < 10 ? '0' + createdDate : createdDate}</span>
                        <span>마지막 변경일: ${modifiedYear}/${modifiedMonth < 10 ? '0' + modifiedMonth : modifiedMonth}/${modifiedDate < 10 ? '0' + modifiedDate : modifiedDate}</span>
                    </div>
                </div>
            </div>
            <hr style="border: solid 1px lightgray; margin-bottom: 3%; margin-top: 3%;">
            <div class=post_music_container>
            <p id="post_music_id" style="display: none">${post_music_id}</p>
                <div>
                    <span id="open_end" class="music_open_end">${open_end} ${emotion}</span>
                </div>
                <p>
                    <img src="${music_cover}">
                </p>
                <p id="post_music_info" style="font-weight: 600;">${music_title} - ${music_singer}</p>
            </div>
            <hr style="border: solid 1px lightgray; margin-bottom: 3%; margin-top: 3%;">
            <div class="action_button_container">
                <span id="post_like_${post_id}"></span>
                <ion-icon name="trash" style="${delete_button_style}" onclick="delete_post(${post_id})"></ion-icon>
            </div>
            <div>

            </div>
            <div class="post_content_container">
                <p id="like_count" style="font-weight: 600;">좋아요 수 ${like_count}</p>
                <p id="post_content">${content}</p>
                <p id="post_tag">${tag_list}</p>
            </div>
            <hr style="border: solid 1px lightgray; margin-bottom: 3%; margin-top: 3%;">
            <div class="comment_textbox">
            <textarea id = "comment_textbox" name="comment" placeholder="댓글을 입력..."></textarea>
            <ion-icon name="chatbox-outline" onclick="create_comment(${post_id})"></ion-icon>
            </div>
            <hr style="border: solid 1px lightgray; margin-bottom: 3%; margin-top: 3%;">
            <div class="post_comment_container">
                <p id="comment_count" style="font-weight: 600;">댓글 수 ${comment_count}</p>
                <div id="comment_list" class="comment_list">
                </div>
            </div>
        </div>
                `
            showPostLikeButton(post_id);
            $('#post_popup').append(temp_post);
            append_tag_list(tag_list); //태그가 있을 때만 태그 기재
            get_comments(post_id); //코멘트 기재
        }
    });
}

// 태그 붙이기
function append_tag_list(tag_list) {
    if (tag_list !== null) {
        $('#post_tag_list').append(tag_list);
    }
}

//게시글의 코멘트 리스트 가져오기 
function get_comments(post_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/comments",
        type: "GET",
        dataType: "json",
        success: function (response) {
            $('#comment_list').empty()

            let rc = response.content;

            for (let i = 0; i < rc.length; i++) {
                const comment_id = rc[i].commentId;
                const comment_user_id = rc[i].userId;
                const comment_username = rc[i].username;
                let comment_user_profile = rc[i].userProfileImage;
                const comment_content = rc[i].content;
                const comment_like_count = rc[i].likeCount;
                const comment_created_at = rc[i].createdAt;
                const comment_modified_at = rc[i].modifiedAt;
                const delete_button_style = (comment_user_id === login_userId) ? '' : 'display: none'

                if (comment_user_profile == null) {
                    comment_user_profile = '../static/images/avatar.jpg"';
                }

                temp_comment = `
                <div class="comment_box">
                <div class="comment_info">
                <span class="comment_profile">
                    <img src="${comment_user_profile}">
                </span>
                <span id="comment_id" style="display: none">${comment_id}</span>
                <span class="comment_username" style="font-weight: 600;">${comment_username}</span>
                <span class="comment_content">${comment_content}</span>
                </div>
                <div class="comment_like">
                <span id="delete_comment_${comment_id}">
                <ion-icon id="delete_comment" name="trash" onclick="delete_comment(${post_id},${comment_id})" style="${delete_button_style}"></ion-icon>
                </span>
                <span id="comment_like_${comment_id}"></span>
                </div>
                </div>            
                              `

                showCommentLikeButton(post_id, comment_id) // 좋아요 여부 별 좋아요 버튼 불러오기
                $('#comment_list').append(temp_comment) //코멘트 리스트에 코멘트 추가
            }
        }
    });
}


// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ게시글 관련ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

//게시글 좋아요 or 좋아요 안한 상태 버튼 보이게 하기
function showPostLikeButton(post_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/like-or-unlike",
        type: "GET",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            like_button = `<ion-icon id="post_like_state_${post_id}" name="heart" style="color: red;" onclick="unlike_post(${post_id})">좋아요 누른 후</ion-icon>`
            unlike_button = `<ion-icon id="post_like_state_${post_id}" name="heart-outline" onclick="like_post(${post_id})">>좋아요 누르기 전</ion-icon>`

            response['likeOrUnlike']

            let button = null;
            if (response['likeOrUnlike'] === 'like') {
                button = like_button;
            } else if (response['likeOrUnlike'] === 'unlike') {
                button = unlike_button;
            } else { console.log('좋아요여부 반환 에러') }

            $('#post_like_'+post_id).append(button)
        }
    })
};


//게시글 좋아요
function like_post(post_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/like",
        type: "POST",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            close_button()
            open_post_popup(post_id)
        },
        error: function (error) {
            console.log(error)
        }

    });
}


//게시글 좋아요 취소
function unlike_post(post_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/unlike",
        type: "DELETE",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            close_button()
            open_post_popup(post_id)
        },
        error: function (error) {
            console.log(error)
        }

    });
}

//게시글 삭제
function delete_post(post_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id,
        type: "DELETE",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            close_button()
        },
        error: function (error) {
            console.log(error)
        }

    });
}



// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글 관련ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

//댓글 좋아요 or 좋아요 안한 상태 버튼 보이게 하기
function showCommentLikeButton(post_id, comment_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/comments/" + comment_id + "/like-or-unlike",
        type: "GET",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            like_button = `<ion-icon id="comment_like_state_${comment_id}" name="heart" style="color: red;" onclick="unlike_comment(${post_id}, ${comment_id})">좋아요 누른 후</ion-icon>`
            unlike_button = `<ion-icon id="comment_like_state_${comment_id}" name="heart-outline" onclick="like_comment(${post_id}, ${comment_id})">>좋아요 누르기 전</ion-icon>`

            response['likeOrUnlike']

            let button = null;
            if (response['likeOrUnlike'] === 'like') {
                button = like_button;
            } else if (response['likeOrUnlike'] === 'unlike') {
                button = unlike_button;
            } else { console.log('좋아요여부 반환 에러') }

            $('#comment_like_' + comment_id).append(button)
        }
    });
}


//댓글 좋아요
function like_comment(post_id, comment_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/comments/" + comment_id + "/like",
        type: "POST",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            close_button()
            open_post_popup(post_id)
        }
    });
}


//댓글 좋아요 취소
function unlike_comment(post_id, comment_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/comments/" + comment_id + "/unlike",
        type: "DELETE",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            close_button()
            open_post_popup(post_id)
        }
    });
}

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글 작성 및 삭제ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

function create_comment(post_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/comments",
        type: "POST",
        contentType: "application/json; charset=UTF-8",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        data: JSON.stringify({
            content: $('#comment_textbox').val()
        }),
        success: function (response) {
            close_button()
            open_post_popup(post_id)
        }
    });
}

function delete_comment(post_id, comment_id) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/comments/" + comment_id,
        type: "DELETE",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            close_button()
            open_post_popup(post_id)
        }
    });
}
