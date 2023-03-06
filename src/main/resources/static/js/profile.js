
const api_url = "http://localhost:8080/users/"; //공통 Url
const url_userId = document.location.href.split('=')[1]; // 추후 프로필 화면 누를 시 Id 받아오도록 변경
let login_userId;

//페이지 시작 시 호출 펑션들
$(document).ready(function () {
    getMySimpleProfile() //검색바 미니프로필
    get_profile() //상단 프로필
    get_posts() //유저 게시글
    // show_edit_profile_button() // 프로필 편집 or 팔로우 or 팔로우 취소 버튼

    getFollowers()//팔로워 버튼 누르면 나오도록 변경예정
    getFollowings()

});


// 프로필 정보 전체 가져오기(유저 정보+ 게시글, 팔로워, 팔로잉 수)
function get_profile() {
    get_userinfo();
    get_postcount();
    get_follwercount();
    get_follwingcount();
}


// 프로필 유저 정보 가져오기
function get_userinfo() {
    $.ajax({
        url: api_url + url_userId + "/profile",
        type: "GET",
        dataType: "json",
        success: function (response) {
            // 받은 데이터 표시
            const username = response['username'] // 유저 이름
            const email = response['email'] //유저 이메일
            let profileImage = response['profileImage'] //프로필 사진
            let introduction = response['introduction'] //프로필 소개글

            //소개글이 없을 때
            if (introduction == null) {
                introduction = "소개글이 없습니다"
            }

            //프로필 이미지가 없을 때
            profileImage = (profileImage == null) ? "http://bwptedu.com/assets/image/default-profile.jpg" : profileImage;// 기본 프로필 이미지


            $("#profile__username").append(username);
            $("#profile__email").append(email);
            $("#profile__introduction").append(introduction);
            $("#profile__profileImage").append("<img src='" + profileImage + "'>");
        },
    });
}


// 게시글 수 가져오기
function get_postcount() {
    $.ajax({
        url: api_url + url_userId + "/post-count",
        type: "GET",
        dataType: "json",
        success: function (response) {
            const post_count = response.profilePostCount // 게시글 수
            $("#post_count").append(post_count);
        },
    });
}

// 팔로워 수 가져오기
function get_follwercount() {
    $.ajax({
        url: api_url + url_userId + "/follower-count",
        type: "GET",
        dataType: "json",
        success: function (response) {
            const follower_count = response.followerCount // 팔로워 수
            $("#follower_count").append(follower_count);
        },
    });
}

// 팔로잉 수 가져오기
function get_follwingcount() {
    $.ajax({
        url: api_url + url_userId + "/following-count",
        type: "GET",
        dataType: "json",
        success: function (response) {
            const following_count = response.followingCount // 팔로잉 수
            $("#following_count").append(following_count);
        },
    });
}


// 작성한 게시글 목록 가져오기
function get_posts() {
    $.ajax({
        url: api_url + url_userId + "/profile/posts",
        type: "GET",
        dataType: "json",
        success: function (response) {
            $('#profile-posts').empty()

            for (let i = 0; i < response.length; i++) {
                const post_id = response[i].postId;
                const username = response[i].username;
                const music_title = response[i].musicTitle;
                const music_singer = response[i].musicSinger;
                const music_cover = response[i].musicCover;
                const content = response[i].content;
                const oepn_or_end = response[i].openOrEnd;
                const emotion = response[i].emotion;
                const like_count = response[i].likeCount;
                const created_at = new Date(response[i].createdAt);
                const modified_at = response[i].modifiedAt;
                const createdYear = created_at.getFullYear().toString().slice(2);
                const createdMonth = created_at.getMonth() + 1;
                const createdDate = created_at.getDate();

                const temp_post = `
                            <div id="post_popup_btn_${post_id}" class="profile__photo" onclick="open_post_popup(${post_id})">
                            <span class="date">${createdYear}/${createdMonth < 10 ? '0' + createdMonth : createdMonth}/${createdDate < 10 ? '0' + createdDate : createdDate}</span>
                            <a href="#개별게시글링크">
                                <img src="${music_cover}" />
                            </a>
                            <span>${music_title}
                            <br>-${music_singer}</span>
                        </div>
                                                `
                $('#profile-posts').append(temp_post)
            }
        },
    });
}


// 상단 검색바 미니 프로필 정보 가져오기
function getMySimpleProfile() {
    $.ajax({
        url: api_url + "profile/simple",
        type: "GET",
        dataType: "json",
        timeout: 0,
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            login_userId = response['userId'] // 유저 아이디
            const my_username = response['username'] // 유저 이름
            let my_profileImage = response['profileImage'] //프로필 사진
            const tokenExpiration = response['tokenExpiration']
            const expiration_time = new Date(tokenExpiration).getTime();
            timer(expiration_time)

            $("#login_userId").text(login_userId);
            $("#my_username").text(my_username);

            my_profileImage = (my_profileImage == null) ? "http://bwptedu.com/assets/image/default-profile.jpg" : my_profileImage;

            $("#my_profileImage").attr('src', my_profileImage);
            $("#my_profile").attr('onclick', `window.location.href='./profile.html?userId=${login_userId}'`)
            $('#getSimpleProfile').attr('onclick', `window.location.href='./profile.html?userId=${login_userId}'`)
        }
    })
}

//ㅡㅡㅡㅡㅡㅡㅡㅡ프로필편집/팔로우/팔로우취소 버튼 보여주기ㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
function show_button(followerList) {

    const isFollower = followerList.some(follower => follower.followerId === login_userId);
    let style_edit_profile = 'display:none';
    let style_follow = 'display:none';
    let style_unfollow = 'display:none';

    if (login_userId === Number(url_userId)) {

        style_edit_profile = '';
    } else {
        style_follow = isFollower ? 'display:none' : '';
        style_unfollow = isFollower ? '' : 'display:none';
    }

    const button_temp = `<a href='edit-profile.html' style="${style_edit_profile}">프로필 편집</a>
                        <a style="${style_follow}" onclick="followUser(${url_userId})">팔로우</a>
                        <a style="${style_unfollow}" onclick="unfollowUser(${url_userId})">팔로우 취소</a>`

    $("#profile_button").append(button_temp)
}


// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ팔로우/팔로우 취소하기ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
function followUser(followUserId) {
    $.ajax({
        url: "http://localhost:8080/users/" + followUserId + "/follow",
        type: "POST",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            alert("팔로우 완료!")
            window.location.reload()
        },
        error: function (error) {
            alert("팔로우가 정상적으로 진행되지 않았습니다.")
        }
    });
}

function unfollowUser(UnfollowUserId) {
    $.ajax({
        url: "http://localhost:8080/users/" + UnfollowUserId + "/unfollow",
        type: "DELETE",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            alert("팔로우 취소 완료!")
            window.location.reload()
        },
        error: function (error) {
            alert("팔로우 취소가 정상적으로 진행되지 않았습니다.")
        }
    });
}

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ팔로우/팔로워 리스트 가져오기ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

function getFollowers() {
    $.ajax({
        url: api_url + url_userId + "/follower",
        type: "GET",
        dataType: "json",
        timeout: 0,
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            followerList = response;
            show_button(followerList)
        }
    })
}

function getFollowings() {
    $.ajax({
        url: api_url + url_userId + "/following",
        type: "GET",
        dataType: "json",
        timeout: 0,
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            followingList = response;

        }
    })
}

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ팔로워 리스트 모달창ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

// Get the modal
var follower_modal = document.getElementById('follower_modal');

// When the user clicks on the button, open the modal 
function open_follower_popup() {
    follower_modal.style.display = "block";
    showFollowerList();
}

function showFollowerList() {
    $('#follower_list_container').empty();

    for (let i = 0; i < followerList.length; i++) {
        const followerId = followerList[i]['followerId'];
        const follwerName = followerList[i]['followerName'];
        let follwerProfileImage = followerList[i]['followerProfileImage'];

        follwerProfileImage = (follwerProfileImage == null) ? "http://bwptedu.com/assets/image/default-profile.jpg" : follwerProfileImage;

        let follwerTemp = `
                    <div class="follow_profile">
                    <p id="follwer_userId_${followerId}" style="display: none">${followerId}</p>
                    <p id="follower_profileImage">
                    <img src="${follwerProfileImage}"></p>
                        <span id="follower_username">${follwerName}</span>
                        <p class="follow_button" onclick="followUser(${followerId})">팔로우 하기</p>
                    </div>   
                    `
        $('#follower_list_container').append(follwerTemp);
    }
}

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ팔로잉 리스트 모달창ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

// Get the modal
var following_modal = document.getElementById('following_modal');

// When the user clicks on the button, open the modal 
function open_following_popup() {
    following_modal.style.display = "block";
    showFollowingList();
}

function showFollowingList() {
    $('#following_list_container').empty();

    for (let i = 0; i < followingList.length; i++) {
        const followingId = followingList[i]['followingId'];
        const follwingName = followingList[i]['followingName'];
        let follwingProfileImage = followingList[i]['followingProfileImage'];

        follwingProfileImage = (follwingProfileImage == null) ? "http://bwptedu.com/assets/image/default-profile.jpg" : follwingProfileImage;

        let follwingTemp = `
                    <div class="follow_profile">
                    <p id="following_userId_${followingId}" style="display: none">${followingId}</p>
                    <p id="following_profileImage">
                    <img src="${follwingProfileImage}"></p>
                        <span id="following_username">${follwingName}</span>
                        <p class="follow_button" onclick="unfollowUser(${followingId})">팔로우 취소</p>
                    </div>   
                    `
        $('#following_list_container').append(follwingTemp);
    }
}
