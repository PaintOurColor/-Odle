const a = document.location.href.split('=')[1];
const word = decodeURI(decodeURIComponent(a)); // 한글로 나오게 해줌

let login_search_userId;

// 페이지가 로딩될 때 실행되는 거
jQuery(document).ready(function ($) {
    getMySimpleProfile1();
    getTagInfo();
    getPostSearchCount();
    getPostSearchList();
});

// 상단바 프로필 보기
function getMySimpleProfile1() {
    var settings = {
        "url": "http://localhost:8080/users/profile/simple",
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response, status) {
        login_search_userId = response['userId']
        $('#myProfileUsername9').text(response['username'])
        $('#myProfileImage9').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        if (status === 403) { // 권한이 없는 것이니까 로그인으로 보내면 됨
            window.location = "/login.html"
        }
        const userId = response['userId']
        const tokenExpiration = response['tokenExpiration']
        const expiration_time = new Date(tokenExpiration).getTime();
        timer(expiration_time)
        $('#myProfile18').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile19').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile20').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
    });
}

// 태그 이름
function getTagInfo() {
    $('.post_search__tagName').append("#" + word)
}

// 검색된 게시글 개수
function getPostSearchCount() {
    var settings = {
        "url": `http://localhost:8080/posts/search/${word}/post-count`,
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response) {
        const postCount = response['profilePostCount']
        $('#post_search_count').append(postCount)
    }).error(function () {
        $('#post_search_count').append("0")
    });
}

// 검색창 클릭
function getPostSearchList() {
    const settings = {
        "url": `http://localhost:8080/posts/search/${word}`,
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response) {
        $('#post_search-posts').empty()
        for (let i = 0; i < response.length; i++) {
            const obj = response[i]
            const postId = obj['postId']
            const title = obj['title']
            const singer = obj['singer']
            const cover = obj['cover']

            const tempHtml = `
                            <div id="post_search_popup_btn_${postId}" class="post_search__photo" onclick="open_post_search_popup(${postId})">
                            <a href="#개별게시글링크">
                                <img src="${cover}" />
                            </a>
                            <span>${title}
                            <br>-${singer}</span>
                        </div>
            `
            $('#post_search-posts').append(tempHtml)
        }
    }).error(function () {
        alert("검색된 게시물이 없습니다.")
    });
}