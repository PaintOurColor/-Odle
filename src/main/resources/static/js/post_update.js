jQuery(document).ready(function ($) {
    getMySimpleProfile();
    getPost();
});

let post_url_userId;
const url_postId = document.location.href.split('=')[1];

function getPost() {
    $.ajax({
        url: "http://localhost:8080/posts/" + url_postId,
        type: "GET",
        dataType: "json",
        success: function (response) {
            $('#update').empty()
            const post_music_id = response.musicId;
            const music_title = response.musicTitle;
            const music_singer = response.musicSinger;
            const music_cover = response.musicCover;
            const content = response.content;
            //const open_end = response.openOrEnd;
            //const emotion = response.emotion;
            const tag_list = response.tagList;
            post_url_userId = response.userId;

            const temp_post = `
  <div class="d-grid gap-2">
    <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
      <input type="radio" class="btn-check" name="btnradio" value="OPEN" id="btnradio1" autocomplete="off"
             checked>
      <label class="btn btn-outline-primary" for="btnradio1">여는 노래</label>

      <input type="radio" class="btn-check" name="btnradio" value="END" id="btnradio2" autocomplete="off">
      <label class="btn btn-outline-primary" for="btnradio2">닫는 노래</label>
    </div>
  </div>
  <div id="music-info" class="music-info">
    <div>
      <div id="post-melon-id">${post_music_id}</div>
      <div id="post-title">${music_title}</div>
      <div id="post-singer">${music_singer}</div>
      <img id="post-cover"
           src="${music_cover}">
    </div>
  </div>

  <div class="input-group">
    <span class="input-group-text">게시글작성</span>
    <textarea aria-label="With textarea" class="form-control h-25" id="content" rows="10">${content}</textarea>
  </div>
  <div class="input-group mb-3">
    <span class="input-group-text"># 태그입력</span>
    <input aria-describedby="basic-addon1" aria-label="Username" class="form-control" id="tag" placeholder=""
           type="text" value="${tag_list}">
  </div>
  <div class="group" role="emotion" aria-label="emotion group">
    <input type="radio" class="btn-check" name="emotion" value="ANGRY" id="angry" autocomplete="off">
    <label class="btn btn-outline-primary" for="angry">😡</label>

    <input type="radio" class="btn-check" name="emotion" value="SAD" id="sad" autocomplete="off">
    <label class="btn btn-outline-primary" for="sad">😭</label>

    <input type="radio" class="btn-check" name="emotion" value="SCREAM" id="scream" autocomplete="off">
    <label class="btn btn-outline-primary" for="scream">😱</label>

    <input type="radio" class="btn-check" name="emotion" value="SHY" id="shy" autocomplete="off" checked>
    <label class="btn btn-outline-primary" for="shy">😳</label>

    <input type="radio" class="btn-check" name="emotion" value="HAPPY" id="happy" autocomplete="off">
    <label class="btn btn-outline-primary" for="happy">😆</label>

    <input type="radio" class="btn-check" name="emotion" value="LOVE" id="love" autocomplete="off">
    <label class="btn btn-outline-primary" for="love">😍</label>

    <input type="radio" class="btn-check" name="emotion" value="FLEX" id="flex" autocomplete="off">
    <label class="btn btn-outline-primary" for="flex">😎</label>
  </div>`
            $('#update').append(temp_post)
        }
    });
}

function postUpdate() {
    $.ajax({
        type: "PATCH",
        url: "http://localhost:8080/posts/" + url_postId,
        headers: {
            "Authorization": localStorage.getItem('accessToken')
        },
        data: JSON.stringify({
            content: $(`#content`).val(),
            tagList: $(`#tag`).val(),
            openOrEnd: $(`input[name='btnradio']:checked`).val(),
            emotion: $(`input[name='emotion']:checked`).val(),
            melonId: $(`#post-melon-id`).text(),
            title: $(`#post-title`).text(),
            singer: $(`#post-singer`).text(),
            cover: $(`#post-cover`).attr('src')
        }),
        contentType: "application/json; charset=UTF-8",
        success: function (response) {
            alert("게시글 수정 완료");
            location.href = './profile.html?userId=' + post_url_userId;
        }
    })
}

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
        $('#myProfileUsername').text(response['username'])
        $('#myProfileImage').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        if (status === 403) { // 권한이 없는 것이니까 로그인으로 보내면 됨
            window.location = "/login.html"
        }
        post_url_userId = response['userId']
        // 프로필 이거 꼭 적어주시기!
        const tokenExpiration = response['tokenExpiration']
        const expiration_time = new Date(tokenExpiration).getTime();
        timer(expiration_time)
        $('#myProfile_post_pic').attr('onclick', `window.location.href='./profile.html?userId=${post_url_userId}'`)
        $('#myProfile_post').attr('onclick', `window.location.href='./profile.html?userId=${post_url_userId}'`)
    });
}