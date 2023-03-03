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
            alert(response.responseJSON.errorMessage)
        }
    })
}

function emailCode() {
    $.ajax({
        url: "http://localhost:8080/users/email",
        type: "POST",
        data: JSON.stringify({
            email: $(`#email`).val()
        }),
        contentType: "application/json; charset=UTF-8",
        success: function () {
            alert("메일이 전송되었습니다. 작성해주신 이메일에서 확인 부탁드립니다.")
        },
        error: function (response) {
            alert(response.responseJSON.errorMessage)
        }
    })
}

function emailCodeCheck() {
    $.ajax({
        url: "http://localhost:8080/users/verify-code",
        type: "POST",
        data: JSON.stringify({
            email: $(`#email`).val(),
            code: $(`#code`).val()
        }),
        contentType: "application/json; charset=UTF-8",
        success: function (response) {
            alert(response.message)
            // 인증 성공 시 이메일과 인증 코드 수정 못하게
            $("#email").attr("readonly",true);
            $("#code").attr("readonly",true);
        },
        error: function (response) {
            alert(response.responseJSON.errorMessage)
        }
    })
}

function usernameCheck() {
    $.ajax({
        url: "http://localhost:8080/users/check-username",
        type: "POST",
        data: JSON.stringify({
            username: $(`#username`).val()
        }),
        contentType: "application/json; charset=UTF-8",
        success: function () {
            alert("사용 가능한 닉네임입니다.")
        },
        error: function (response) {
            alert(response.responseJSON.errorMessage)
        }
    })
}

function signUp() {
    // if ($("#email").attr("readonly") !== undefined && $("#code").attr("readonly") !== undefined){
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
        error: function (response) {
            alert(response.responseJSON.errorMessage)
        }
    })
    // } else {
    //     alert("모든 인증을 완료해주세요")
    // }
}