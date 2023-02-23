function postCreate() {

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/posts",
        headers: {
            "Authorization": localStorage.getItem('accessToken')
        },
        data: JSON.stringify({
            content: $(`#content`).val(),
            tagCreateRequest: $(`#tag`).val(),
            openOrEnd: $(`input[name='btnradio']:checked`).val(),
            emotion: $(`input[name='emotion']:checked`).val(),
            // melonId: $(``),
            // title: $(``),
            // singer: $(``),
            // cover: $(``)
            melonId: 1,
            title: "abc",
            singer: "abc",
            cover: "abc"
        }),
        contentType: "application/json; charset=UTF-8",
        success: function (response) {
            if (response['statusCode'] === 201) {
                alert("게시글 작성 완료");
                location.href = "profile.html";
            }
        }
    })
}



function show () {
    document.querySelector(".background2").className = "background2 show";
}

function close () {
    document.querySelector(".background2").className = "background2";
}

document.querySelector("#show").addEventListener('click', show);
document.querySelector("#close").addEventListener('click', close);