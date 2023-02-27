const api_url = "http://localhost:8080/users/"; //공통 Url

$(document).ready(function () {
    getUserList() //유저 목록 가져오기
});

// 유저 목록 가져오기
function getUserList() {
    $.ajax({
        url: api_url,
        type: "GET",
        contentType: "json",
        success: function (response) {
            $('#user_card_list').empty(); // 유저 틀 초기화

            for (let i = 0; i < response.length; i++) {
            const userId = response[i]['userId'] // 유저 이름
            const username = response[i]['username'] // 유저 이름
            const email = response[i]['email'] //유저 이메일
            let activation = response[i]['activation'] //활성화 여부

            const user_temp = `
            <div id="user_card">
            <span>유저 아이디: </span><span id="user_id">${userId}</span>
            <span>유저 이메일: </span><span id="user_email">${email}</span>
            <span>유저 이름: </span><span id="user_username">${username}</span>
            <span>활성화 여부: </span><span id="user_activation_${userId}">${activation}</span>
            <button id="user_active" onclick = "activateUser(${userId})">활성화</button>
            <button id="user_inactive" onclick = "inactivateUser(${userId})">비활성화</button>
            </div>
            `
            $('#user_card_list').append(user_temp)
        }
        }
    });
}

//유저 활성화
function activateUser(userId) {
    $.ajax({
        url: api_url + userId + "/activation/admin",
        type: "PATCH",
        contentType: "application/json; charset=UTF-8",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        data: JSON.stringify({
            "password": $('#admin_password').val()
        }),
        success: function (response) {
            $('#user_activation_'+ userId).text("ACTIVE")
        },
        error: function (response) {
            const errorMessage = response.responseJSON['errorMessage']
            if(errorMessage){
                alert(errorMessage);
            } else{
                alert("에러: "+ response.status)
            }
        }
    });
}

//유저 비활성화
function inactivateUser(userId) {
    $.ajax({
        url: api_url + userId + "/inactivation/admin",
        type: "PATCH",
        contentType: "application/json; charset=UTF-8",
        headers: {
            "Authorization": localStorage.getItem("accessToken")
        },
        data: JSON.stringify({
            "password": $('#admin_password').val()
        }),
        success: function (response) {
            $('#user_activation_'+ userId).text("INACTIVE")
        },
        error: function (response) {
            const errorMessage = response.responseJSON['errorMessage']
            if(errorMessage){
                alert(errorMessage);
            } else{
                alert("에러: "+ response.status)
            }
        }
    });
}


