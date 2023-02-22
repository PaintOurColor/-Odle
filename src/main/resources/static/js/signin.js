function signIn() {
    var settings = {
        type: "POST",
        url: "http://localhost:8080/users/login",
        data: JSON.stringify({
            email: $(`#email`).val(),
            password: $(`#password`).val()
        }),
        contentType: "application/json; charset=UTF-8",
    }
    $.ajax(settings).done(function (response, status, xhr) {
        localStorage.setItem('accessToken', xhr.getResponseHeader('Authorization'))
        document.cookie =
            'RefreshToken' + '=' + xhr.getResponseHeader('RefreshToken') + ';path=/';
        alert("로그인 완료");
        window.location = 'index.html'
    }).fail(function (response) {
        console.log(response.responseJSON)
        if (response.responseJSON.httpStatus === 'NOT_FOUND') {
            alert('아이디와 비밀번호를 확인하여주세요');
        } else {
            alert('서버에 문제가 발생하였습니다');
        }
    });
}