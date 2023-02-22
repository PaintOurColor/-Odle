// Get the modal
var modal = document.getElementById('post_modal');

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close");


// When the user clicks on the button, open the modal 
function open_post_popup(post_id) {
    modal.style.display = "block";
    get_post(post_id);
}

// When the user clicks on <span> (x), close the modal
function close_button() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}



//게시글 좋아요 or 좋아요 안한 상태 버튼 보이게 하기
function showPostLikeButton(post_id, like_count) {
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id + "/like-or-unlike",
        type: "GET",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            console.log('나와랏!!')
            const check = response["likeOrUnlike"] === "like" ? true : false;
            let heart = check ? 'heart': 'heart-outline';
            let heartStyle = check ? 'color:red' : '';
            let onclickChange = check? `likeChange(${post_id},${like_count}); unlike_post(${post_id});` : `likeChange(${post_id},${like_count}); like_post(${post_id});`;

            checkLike = check;
            $('#post_like').html('<ion-icon name=' + heart + ' style=' + heartStyle + '></ion-icon>')
            $('#post_like').attr('onclick', onclickChange)
        
        }
    });
}

let checkLike;
let heart = checkLike ? 'heart': 'heart-outline';
let heartStyle = checkLike ? 'color:red' : '';

const likeChange = (post_id, like_count) => {
    console.log(like_count)
    checkLike = !checkLike
    console.log(checkLike)
    heart = checkLike ? 'heart': 'heart-outline';
    heartStyle = checkLike ? 'color:red' : '';
    let onclickChange = checkLike? `likeChange(${post_id},${like_count}); unlike_post(${post_id});` : `likeChange(${post_id},${like_count}); like_post(${post_id});`;

    $('#post_like').html('<ion-icon name=' + heart + ' style=' + heartStyle + '></ion-icon>')
    $('#post_like').attr('onclick', onclickChange)

    // console.log(checkLike, num)
    // checkLike ? num++ : num--;
    
    let num = Number(like_count)
    console.log(num)

    document.getElementById('like_count').innerText = '좋아요 수' + (checkLike ? num++ : num--)

}



function get_post(post_id) {
    console.log(checkLike);
    $.ajax({
        url: "http://localhost:8080/posts/" + post_id,
        type: "GET",
        dataType: "json",
        success: function (response) {
            $('#post_popup').empty()
            console.log("게시글 번호: " + post_id)
            console.log(response)

            const user_id = response.user_id;
            const username = response.username;
            const user_profile = response.userProfileImage;
            const music_id = response.musicId;
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
            const createdMonth = created_at.getMonth()+1;
            const createdDate = created_at.getDate();
            const modifiedYear = modified_at.getFullYear().toString().slice(2);
            const modifiedMonth = modified_at.getMonth()+1;
            const modifiedDate = modified_at.getDate();
            showPostLikeButton(post_id, like_count);
            const temp_post = `
            <span class="close" onclick="close_button()">&times;</span>
            <div class="post_container">
            <div class="post_info_container">
                <p id="my_userId" style="display: none"></p>
                <p id="my_profileImage">
                    <img src="${user_profile}"></a>
                </p>
                <div class="post_info">
                    <span id="my_username">${username}</span>
                    <div class="post_time">
                        <span>작성일: ${createdYear}/${createdMonth<10?'0'+createdMonth:createdMonth}/${createdDate<10?'0'+createdDate:createdDate}</span>
                        <span>마지막 변경일: ${modifiedYear}/${modifiedMonth<10?'0'+modifiedMonth:modifiedMonth}/${modifiedDate<10?'0'+modifiedDate:modifiedDate}</span>
                    </div>
                </div>
            </div>
            <hr style="border: solid 1px lightgray; margin-bottom: 3%; margin-top: 3%;">
            <div class=post_music_container>
                <div>
                    <span id="open_end" class="music_open_end">${open_end} ${emotion}</span>
                </div>
                <p>
                    <img src="${music_cover}">
                </p>
                <p id="post_music_info" style="font-weight: 600;">${music_title} - ${music_singer}</p>
            </div>
            <hr style="border: solid 1px lightgray; margin-bottom: 3%; margin-top: 3%;">
            <div id ="post_like" class="action_button_container" onclick="likeChange(${post_id}, ${like_count});  like_post(${post_id});">
                <ion-icon name="${heart}" style="${heartStyle}"></ion-icon>
            </div>
            <div class="post_content_container">
                <p id="like_count" style="font-weight: 600;">좋아요 수 ${like_count}</p>
                <p id="post_content">${content}</p>
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
                comment_id = rc[i].commentId;
                comment_user_id = rc[i].userId;
                comment_username = rc[i].username;
                comment_user_profile = rc[i].userProfileImage;
                comment_content = rc[i].content;
                comment_like_count = rc[i].likeCount;
                comment_created_at = rc[i].createdAt;
                comment_modified_at = rc[i].modifiedAt;

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
                <span id="delete_comment_${comment_id}"></span>
                <span id="comment_like_${comment_id}"></span>
                </div>
                </div>            
                              `
                // show_delete_button(comment_id, comment_user_id)//본인일 경우만 삭제버튼 보이게
                showCommentLikeButton(post_id, comment_id) // 좋아요 여부 별 좋아요 버튼 불러오기
                $('#comment_list').append(temp_comment) //코멘트 리스트에 코멘트 추가
            }
        }
    });
}


// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ게시글 관련ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


//게시글 좋아요
function like_post(post_id) {
    console.log('열받게하지마라')
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
        error: function(error){
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
        error: function(error){
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

            if (response.likeOrUnlike == "like") {
                button = like_button
            } else if (response.likeOrUnlike == "unlike") {
                button = unlike_button
            } else {
                console.error()
            }

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
            alert('좋아요 취소 완료!')
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

function delete_comment(comment_id) {
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

// function show_delete_button(comment_id, comment_user_id){
//     console.log("내아이디: " + my_userId)
//     temp_delete_button = `<ion-icon id="delete_comment_${comment_id}" name="trash" onclick="delete_comment(${comment_id},${user_id})"></ion-icon>`

//     if(my_userId==comment_user_id) {
//         $('#delete_comment_'+comment_id).append(temp_delete_button)
//     }
// }