document.addEventListener("DOMContentLoaded", function () {
    const btnEnable = document.querySelector("#btn_enable");
    const btnRemove = document.querySelector("#btn_remove");
    const btnCancel = document.querySelector("#btn_cancel");
    const listBtns = document.querySelectorAll(".btn-list");

    // 목록으로 이동
    listBtns.forEach(btn => {
        btn.addEventListener("click", function () {
            location.href = "/diary/listDiary";
        });
    });

    // 수정 모드 전환
    if (btnEnable) {
        btnEnable.addEventListener("click", function () {
            document.querySelector("#i_title").disabled = false;
            document.querySelector("#i_content").disabled = false;
            document.querySelector("#tr_btn").style.display = "none";
            document.querySelector("#tr_btn_modify").style.display = "flex";
        });
    }

    // 취소 시 새로고침
    if (btnCancel) {
        btnCancel.addEventListener("click", function () {
            location.reload();
        });
    }

    // 동적 CSRF Form 생성을 이용한 삭제 처리
    if (btnRemove) {
        btnRemove.addEventListener("click", function () {
            if (confirm("정말 삭제하시겠습니까?")) {
                const url = this.getAttribute("data-url");
                const diaryId = this.getAttribute("data-id");

                const form = document.createElement("form");
                form.setAttribute("method", "post");
                form.setAttribute("action", url);

                const inputNo = document.createElement("input");
                inputNo.setAttribute("type", "hidden");
                inputNo.setAttribute("name", "diaryNo");
                inputNo.setAttribute("value", diaryId);
                form.appendChild(inputNo);

                // Meta Tag에서 CSRF 토큰 추출 후 삽입
                const csrfMeta = document.querySelector("meta[name='_csrf']");
                if (csrfMeta) {
                    const inputCsrf = document.createElement("input");
                    inputCsrf.setAttribute("type", "hidden");
                    inputCsrf.setAttribute("name", "_csrf");
                    inputCsrf.setAttribute("value", csrfMeta.getAttribute("content"));
                    form.appendChild(inputCsrf);
                }

                document.body.appendChild(form);
                form.submit();
            }
        });
    }
});