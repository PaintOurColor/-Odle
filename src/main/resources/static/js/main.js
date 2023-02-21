AudioScheduledSourceNode.init({
    duration: 800,
    easing: 'slide',
    once: true
});

// 페이지가 로딩 될 때 실행 되는거.
jQuery(document).ready(function($){
    getSimpleProfile();
});
function getSimpleProfile(){
    $.ajax({
        type: "GET",
        url: `http://localhost:8080//${userId}/profile/simple`,
        data: JSON.stringify({

        }),
        contentType: "application/json; charset=UTF-8",
        success: function (response, status, xhr) {
            if (response === 'success') {
                alert('이메일 또는 비밀번호를 확인해주세요.')
                window.location.reload();

            } else {
                alert("로그인 완료");
                document.cookie =
                    'Authorization' + '=' + xhr.getResponseHeader('Authorization') + ';path=/';
                location.href = "index.html";
            }
        }
    })
}

var settings = {
    "url": "localhost:8080/users/1/profile/simple",
    "method": "GET",
    "timeout": 0,
    "headers": {
        "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJhdXRoIjoiVVNFUiIsImV4cCI6MTY3Njg5MTI1MiwiaWF0IjoxNjc2ODg3NjUyfQ.xoVe_jPm6swehKHfenYmGmjSP3JjUfoK_BO9EztDAKo"
    },
};

$.ajax(settings).done(function (response) {
    console.log(response);
});
