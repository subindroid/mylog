document.addEventListener("DOMContentLoaded", function () {
    const btnEnable = document.querySelector("#btn_enable");
    const btnCancel = document.querySelector("#btn_cancel");
    const profileForm = document.querySelector("#profileForm");

    // '수정하기' 버튼 클릭 시 입력창 활성화
    if (btnEnable) {
        btnEnable.addEventListener("click", function () {
            document.querySelector("#i_nickname").disabled = false;
            document.querySelector("#i_location").disabled = false;
            document.querySelector("#i_bio").disabled = false;
            
            document.querySelector("#tr_btn").style.display = "none";
            document.querySelector("#tr_btn_modify").style.display = "flex";
        });
    }

    // '취소' 버튼 클릭 시 새로고침
    if (btnCancel) {
        btnCancel.addEventListener("click", function () {
            location.reload();
        });
    }
});