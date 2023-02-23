const url_userId = document.location.href.split('=')[1];

// 페이지가 로딩될 때 실행되는 거
jQuery(document).ready(function ($) {
    getMySimpleProfile();
    getPost();
    follow();
});

// 상단바에 보이는 내 프로필
// 이 함수 복사하시던가 feed.js 파일 스크립트에 추가하셔서 사용하세요!
function getMySimpleProfile() {
    var settings = {
        "url": "http://localhost:8080/users/profile/simple",
        "method": "GET",
        "timeout": 0,
        "headers": {
            "RefreshToken": getCookie('RefreshToken'),
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response, status) {
        $('#myProfileUsername').text(response['username'])
        $('#myProfileImage').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        if (status === 403) { // 권한이 없는 것이니까 로그인으로 보내면 됨
            window.location = "/login.html"
        }
        const userId = response['userId']
        // 프로필 이거 꼭 적어주시기!
        $('#myProfile1').attr('onclick', `window.location.href='/src/main/resources/templates/profile.html?userId=${userId}'`)
        $('#myProfile2').attr('onclick', `window.location.href='/src/main/resources/templates/profile.html?userId=${userId}'`)
        $('#myProfile3').attr('onclick', `window.location.href='/src/main/resources/templates/profile.html?userId=${userId}'`)
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
        console.log(response);

        for (let i = 0; i < response.length; i++) {
            const obj = response[i];
            const postId = obj['id']
            const userId = obj['userId']
            const username = obj['username']
            const profileImage = obj['profileImage']
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

            showPostLikeButton(postId, likeCount)
            $('#post_popup').empty()
            const tempHtml = `
                  <div class="photo">
                      <header class="photo__header">
                          <a onclick="window.location.href='/src/main/resources/templates/profile.html?userId=${userId}'" role="link" tabindex="0">
                              <img alt="프로필 사진" src="${profileImage == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : profileImage}" id="profileImage" class="photo__avatar"/>
                          </a>
                          <div class="photo__user-info">
                              <a onclick="window.location.href='/src/main/resources/templates/profile.html?userId=${userId}'" id="getProfile"  role="link" tabindex="0">
                                  <span class="photo__author" id="profileUsername">${username}</span>
                              </a>
                          </div>
                          <div class="arr2">
                            <div class="arr3">
                                <span class="_ac6e _ac6g _ac6h">•</span>
                            </div>
                                <div style="height: 100%;">
                                     <p onclick="follow">팔로우</p>
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
                              <span class="photo__action">
                                  <i class="fa fa-heart-o fa-lg"></i>
                              </span>
                              <span class="photo__action">
                                      <i class="fa fa-comment-o fa-lg"></i>
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
                          <div id="post_popup_btn_${postId}" onclick="open_post_popup(${postId})">${commentCount}개의 댓글 모두 보기</div>
                          <span class="photo__time-ago" id="createdAt">${elapsedText(createdAt)}</span>
                          <div class="photo__add-comment-container">
                              <textarea name="comment" placeholder="댓글을 입력..."></textarea>
                              <i class="fa fa-ellipsis-h"></i>
                          </div>
                      </div>
                 </div>
          `
            $('#getPost').append(tempHtml)
        }
    });
}

function follow() {
    var settings = {
        "url": "localhost:8080/users/" + url_userId + "follow",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
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