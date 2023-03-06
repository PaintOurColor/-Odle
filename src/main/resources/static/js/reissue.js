
let expiration_time;

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ토큰 만료 시간 타이머ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
function timer(time) {
    expiration_time = time
    countdown()
    setInterval(countdown,1000)
}

function countdown() {
    // Get today's date and time
    var now = new Date().getTime();

    // Find the distance between now and the count down date
    var distance = expiration_time - now;

    // Time calculations for days, hours, minutes and seconds
    var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
    var seconds = Math.floor((distance % (1000 * 60)) / 1000);

    // Output the result in an element with id="demo"
    document.getElementById("expiration_time").innerHTML = minutes + "분" + seconds + "초";

    // If the count down is over, write some text 
    if (distance < 0) {
        clearInterval(x);
        document.getElementById("expiration_time").innerHTML = "EXPIRED";
        alert("접속 시간이 만료되었습니다 재로그인하여 주세요.")
    }
}

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ토큰 재발급ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

function reissueToken() {
    $.ajax({
        url: "http://localhost:8080/users/reissue",
        type: "POST",
        dataType: "json",
        headers: {
            "Authorization": localStorage.getItem("accessToken"),
            "RefreshToken": getCookie('RefreshToken')
        },
        success: function (response) {
            const newAccessToken = response['newAccessToken'];
            localStorage.removeItem('accessToken'); // 기존 accessToken 제거
            localStorage.setItem('accessToken', newAccessToken); //새로운 accessToken 추가
            getMySimpleProfile()

            console.log("접속시간 연장 완료");
        },
        error: function (response) {
            const errorMessage = response.responseJSON['errorMessage']
            if (errorMessage) {
                alert(errorMessage);
            } else {
                alert("에러: " + response.status)
            }
        }
    })
}


function getCookie(name) {
    const value = '; ' + document.cookie;
    const parts = value.split('; ' + name + '=');
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
}