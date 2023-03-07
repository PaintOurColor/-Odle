let login_feed_userId;

// 페이지가 로딩될 때 실행되는 거
jQuery(document).ready(function ($) {
    getMySimpleProfile();
    getPost();
});

// 상단바에 보이는 내 프로필
function getMySimpleProfile() {
    var settings = {
        "url": "http://localhost:8080/users/profile/simple",
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response, status) {
        login_feed_userId = response['userId']
        $('#myProfileUsername').text(response['username'])
        $('#myProfileUsername2').text(response['username'])
        $('#myProfileUsername3').text(response['username'])
        $('#myProfileUsername4').text(response['username'])
        $('#myProfileUsername5').text(response['username'])
        $('#myProfileUsername6').text(response['username'])
        $('#myProfileUsername7').text(response['username'])
        $('#myProfileUsername8').text(response['username']) // 9~10 post_search.js 에 있음
        $('#myProfileImage').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        $('#myProfileImage2').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        $('#myProfileImage3').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        $('#myProfileImage4').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        $('#myProfileImage5').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        $('#myProfileImage6').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        $('#myProfileImage7').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        $('#myProfileImage8').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        if (status === 403) { // 권한이 없는 것이니까 로그인으로 보내면 됨
            window.location = "/login.html"
        }
        const userId = response['userId']
        const tokenExpiration = response['tokenExpiration']
        const expiration_time = new Date(tokenExpiration).getTime();
        timer(expiration_time)
        $('#myProfile1').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile2').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile3').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile4').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile5').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile6').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile7').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile8').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile9').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile10').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile11').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile12').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile13').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile14').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile15').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile16').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile17').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
    });
}

// 게시글
function getPost() {
    var settings = {
        "url": "http://localhost:8080/posts",
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response) {
        for (let i = 0; i < response.length; i++) {
            const obj = response[i];
            const postId = obj['id']
            const userId = obj['userId']
            const username = obj['username']
            const profileImage = obj['userProfileImage']
            const openOrEnd = obj['openOrEnd']
            const emotion = obj['emotion']
            const musicTitle = obj['musicTitle']
            const musicSinger = obj['musicSinger']
            const musicCover = obj['musicCover']
            const content = obj['content']
            const tagList = obj['tagList']
            const likeCount = obj['likeCount']
            const commentCount = obj['commentCount']
            const createdAt = new Date(obj['createdAt'])
            const modifiedAt = new Date(obj['modifiedAt'])

            if (login_feed_userId !== userId) {
                showFollowButton(userId, postId)
            }

            showFeedLikeButton(postId)

            $('#feed_popup').empty()
            const tempHtml = `
                  <div class="photo">
                      <header class="photo__header">
                          <a onclick="window.location.href='./profile.html?userId=${userId}'" role="link" tabindex="0">
                              <img alt="프로필 사진" src="${profileImage == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : profileImage}" id="profileImage" class="photo__avatar"/>
                          </a>
                          <div class="photo__user-info">
                              <div>
                                  <a onclick="window.location.href='./profile.html?userId=${userId}'" id="getProfile"  role="link" tabindex="0">
                                      <span class="photo__author" id="profileUsername">${username}</span>
                                  </a>
                              </div>
                              <div class="arr2">
                                <div id="feed_follow_${postId}">
                                </div>
                              </div>
                          </div>
                          
                      </header>
                      <div class="feed__open_close" id="openOrEnd">
                          <span>${openOrEnd} / ${emotion}</span>
                      </div>
                      <div class="feed__musics">
                          <div class="feed__musicInfo" id="titleAndSinger">
                              <span>${musicTitle} - ${musicSinger}</span>
                          </div>
                          <img alt="앨범 커버 사진" src="${musicCover}" id="albumCover"/>
                      </div>
                      <div class="photo__info">
                          <div class="photo__actions">
                              <span class="photo__action" id="showFeedLikeButton_${postId}">
                              </span>
                              <span class="photo__action">
                                <ion-icon name="chatbubble-outline" style="font-size: 20pt;" onclick="open_feed_popup(${postId})"></ion-icon>
                              </span>
                          </div>
                          <div class="photo__likes" id="likeCount">
                              <span>좋아요 ${likeCount}개</span>
                          </div>
                          <div class="photo__contents" id="content">
                              <a>${content}</a>
                          </div>
                          <div class="photo__tags" id="tagList">
                              <a href="#;">${tagList}</a>
                          </div>
                          <div id="feed_popup_btn_${postId}" onclick="open_feed_popup(${postId})">${commentCount}개의 댓글 모두 보기</div>
                          <span class="photo__time-ago" id="createdAt">${elapsedText(createdAt)}</span>
                      </div>
                 </div>
          `
            $('#getPost').append(tempHtml)
        }
    });
}

function showFollowButton(userId, postId) {
    $.ajax({
        url: "http://localhost:8080/users/" + userId + "/follow-or-unfollow",
        type: "GET",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            follow_button = `<div class="follow_button" id="feed_follow_state_${postId}" onclick="followUser(${userId})">팔로우</div>`
            unfollow_button = `<div class="unfollow_button" id="feed_follow_state_${postId}" onclick="unfollowUser(${userId})">언팔로우</div>`

            response['followOrUnfollow']

            let button = null;
            if (response['followOrUnfollow'] === 'follow') {
                button = unfollow_button;
            } else if (response['followOrUnfollow'] === 'unfollow') {
                button = follow_button;
            } else {
                console.log('좋아요여부 반환 에러')
            }

            $('#feed_follow_' + postId).append(button)
        }
    })
}

function followUser(userId) {
    var settings = {
        "url": "http://localhost:8080/users/" + userId + "/follow",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response) {
        alert("팔로우 완료!")
        window.location.reload(true)
    })
        .fail(function (response) {
            alert("팔로우가 정상적으로 진행되지 않았습니다.")
        });
}

function unfollowUser(userId) {
    var settings = {
        "url": "http://localhost:8080/users/" + userId + "/unfollow",
        "method": "DELETE",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response) {
        alert("팔로우 취소 완료!")
        window.location.reload(true)
    })
        .fail(function (response) {
            alert("팔로우 취소가 정상적으로 진행되지 않았습니다.")
        });
}

//게시글 좋아요 or 좋아요 안한 상태 버튼 보이게 하기
function showFeedLikeButton(postId) {
    $.ajax({
        url: "http://localhost:8080/posts/" + postId + "/like-or-unlike",
        type: "GET",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            like_button = `<ion-icon id="feed_like_state_${postId}" name="heart" style="color: red; font-size: 20pt;" onclick="unlikePost(${postId})">좋아요 누른 후</ion-icon>`
            unlike_button = `<ion-icon id="feed_like_state_${postId}" name="heart-outline" style="font-size: 20pt;" onclick="likePost(${postId})">>좋아요 누르기 전</ion-icon>`

            response['likeOrUnlike']

            let button = null;
            if (response['likeOrUnlike'] === 'like') {
                button = like_button;
            } else if (response['likeOrUnlike'] === 'unlike') {
                button = unlike_button;
            } else {
                console.log('좋아요여부 반환 에러')
            }

            $('#showFeedLikeButton_' + postId).append(button)
        }
    })
};

//게시글 좋아요
function likePost(postId) {
    $.ajax({
        url: "http://localhost:8080/posts/" + postId + "/like",
        type: "POST",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            window.location.reload(true);
        },
        error: function (error) {
            console.log(error)
        }
    });
}

//게시글 좋아요 취소
function unlikePost(postId) {
    $.ajax({
        url: "http://localhost:8080/posts/" + postId + "/unlike",
        type: "DELETE",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            window.location.reload(true)
        },
        error: function (error) {
            console.log(error)
        }
    });
}

function elapsedText(date) {
    // 초 (밀리초)
    const seconds = 1;
    // 분
    const minute = seconds * 60;
    // 시
    const hour = minute * 60;
    // 일
    const day = hour * 24;

    var today = new Date();
    var elapsedTime = Math.trunc((today.getTime() - date.getTime()) / 1000);

    var elapsedText = "";
    if (elapsedTime < seconds + 10) {
        elapsedText = "방금 전";
    } else if (elapsedTime < minute) {
        elapsedText = elapsedTime + "초 전";
    } else if (elapsedTime < hour) {
        elapsedText = Math.trunc(elapsedTime / minute) + "분 전";
    } else if (elapsedTime < day) {
        elapsedText = Math.trunc(elapsedTime / hour) + "시간 전";
    } else if (elapsedTime < (day * 15)) {
        elapsedText = Math.trunc(elapsedTime / day) + "일 전";
    } else {
        elapsedText = SimpleDateTimeFormat(date, "yyyy.M.d");
    }
    return elapsedText;
}