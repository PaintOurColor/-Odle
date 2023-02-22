
const api_url = "http://localhost:8080/users/"; //공통 Url
const url_userId = 1; // 추후 프로필 화면 누를 시 Id 받아오도록 변경

//페이지 시작 시 호출 펑션들
$(document).ready(function () {
    get_my_simple_profile() //검색바 미니프로필
    get_profile() //상단 프로필
    get_posts() //유저 게시글
    // show_edit_profile_button() // 프로필 편집 or 팔로우 or 팔로우 취소 버튼
});


// 프로필 정보 전체 가져오기(유저 정보+ 게시글, 팔로워, 팔로잉 수)
function get_profile() {
    get_userinfo();
    get_postcount();
    get_follwercount();
    get_follwingcount();
}

// function show_profile_button() {
//     show_edit_profile_button();
//     //팔로우 버튼 펑션 추가 필요
//     //팔로우 취소 버튼 펑션 추가 필요
// }


// 프로필 유저 정보 가져오기
function get_userinfo() {
    $.ajax({
        url: api_url + url_userId + "/profile",
        type: "GET",
        dataType: "json",
        success: function (response) {
            console.log(response)
            // 받은 데이터 표시
            const username = response['username'] // 유저 이름
            const email = response['email'] //유저 이메일
            let profileImage = response['profileImage'] //프로필 사진
            let introduction = response['introduction'] //프로필 소개글

            //소개글이 없을 때
            if (introduction == null) {
                introduction = "소개글이 없습니다"
            }

            // //프로필 이미지가 없을 때
            // if (profileImage == null) {
            //     profileImage = "../static/images/avatar.jpg" // 기본 프로필 이미지
            // }

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
                const createdMonth = created_at.getMonth()+1;
                const createdDate = created_at.getDate();
                

                console.log(typeof created_at)
                

                const temp_post = `
                            <div id="post_popup_btn_${post_id}" class="profile__photo" onclick="open_post_popup(${post_id})">
                            <span class="date">${createdYear}/${createdMonth<10?'0'+createdMonth:createdMonth}/${createdDate<10?'0'+createdDate:createdDate}</span>
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
function get_my_simple_profile() {
    $.ajax({
        url: api_url + "profile/simple",
        type: "GET",
        dataType: "json",
        timeout: 0,
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        success: function (response) {
            console.log('외않되?' + response);//콘솔에 받아오는 데이터 확인

            const my_userId = response['userId'] // 유저 아이디
            const my_username = response['username'] // 유저 이름
            let my_profileImage = response['profileImage'] //프로필 사진


            console.log(my_userId, my_username, my_profileImage)

            $("#my_userId").text(my_userId);
            $("#my_username").text(my_username);

            //프로필 이미지가 없을 때
            if (my_profileImage == null) {
                my_profileImage = "../static/images/avatar.jpg" // 기본 프로필 이미지
            }

            $("#my_profileImage").append("<img src='" + my_profileImage + "'>");

            // 함수 성공 시 프로필 버튼 노출

            show_edit_profile_button()
        }
    })
}

function show_profile_button() {
    console.log("내아이디: " + my_userId)
    console.log("url 아이디: " + url_userId)

    if (my_userId == url_userId) {
        show_edit_profile_button();
    }
    else {



    }
}

function show_edit_profile_button() {
    $("#profile_button").append("<a href='edit-profile.html'>프로필 편집</a>");
}

function show_follow_button() {
    $("#profile_button").append("<a href='edit-profile.html'>팔로우</a>");
}

function show_follow_button() {
    $("#profile_button").append("<a href='edit-profile.html'>팔로우 취소</a>");
}