function changeProfile() {
    const accessToken = localStorage.getItem('accessToken');
    const settings = {
        "url": "http://localhost:8080/users/profile",
        "method": "PATCH",
        "timeout": 0,
        "headers": {
            "Authorization": accessToken,
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            "username": $(`#username`).val(),
            "introduction": $(`#introduction`).val(),
            "profileImage": $(`#profileImage`).val()
        }),
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
        alert("변경사항 저장 완료");
    }).fail(function (jqXHR, textStatus, errorThrown) {
        if (jqXHR.status === 401) {
            alert("로그인이 필요합니다.");
        } else if (jqXHR.status === 403) {
            alert("접근 권한이 없습니다.");
        } else {
            alert("서버에 문제가 발생하였습니다.");
        }
    });
}
