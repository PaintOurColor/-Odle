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
        }
    })
}