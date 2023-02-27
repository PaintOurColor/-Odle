function emailCheck() {
    $.ajax({
        url: "http://localhost:8080/users/check-email",
        type: "POST",
        data: JSON.stringify({
            email: $(`#email`).val()
        }),
        contentType: "application/json; charset=UTF-8",
        success: function () {
            alert("사용 가능한 이메일입니다.")
        },
        error: function (response) {
            if (response.responseJSON.errorMessage === '중복된 이메일이 존재합니다.') {
                alert("중복된 이메일이 존재합니다.")
            } else {
                alert("잘못된 형식입니다.")
            }
        }
    })
}

function signUp() {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/users/signup",
        data: JSON.stringify({
            username: $(`#username`).val(),
            email: $(`#email`).val(),
            password: $(`#password`).val()
        }),
        contentType: "application/json; charset=UTF-8",
        success: function () {
            alert("회원가입 완료")
            location.href="signin.html"
        },
        error: function () {
            alert("잘못 입력하셨습니다.")
        }
    })
}