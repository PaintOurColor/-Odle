let login_chart_userId;

// 페이지가 로딩될 때 실행되는 거
jQuery(document).ready(function ($) {
    getMySimpleProfile();
    getAngryChart();
});

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
        login_chart_userId = response['userId']
        $('#myProfileUsername3').text(response['username'])
        $('#myProfileImage3').attr('src', response['profileImage'] == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : response['profileImage'])
        if (status === 403) { // 권한이 없는 것이니까 로그인으로 보내면 됨
            window.location = "/login.html"
        }
        const userId = response['userId']
        $('#myProfile6').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
        $('#myProfile7').attr('onclick', `window.location.href='./profile.html?userId=${userId}'`)
    });
}

function getAngryChart() {
    var settings = {
        "url": "http://localhost:8080/music/charts/angry",
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('accessToken')
        },
    };

    $.ajax(settings).done(function (response) {
        console.log(response);

        for (let i = 0; i <= 6; i++) {
            const obj = response[i];
            const musicId = obj['musicId']
            const title = obj['title']
            const singer = obj['singer']
            const cover = obj['cover']
            const todayAngryCount = obj['todayEmotionCount']

            const tempHtml = `
                    <div class="chart__musics">
                        <img alt="앨범 커버" src="${cover == null ? 'http://bwptedu.com/assets/image/default-profile.jpg' : cover}" id="angryChartCover">
                        <div class="chart__musicInfo">
                            <span style="font-weight: bold">${title}</span>
                            <span>${singer}</span>
                        </div>
                        <div class="chart__emotion_count">
                            <span>post ${todayAngryCount}</span>
                        </div> 
                    </div>
        `
            $('#getAngryChart').append(tempHtml)
        }
    });
}