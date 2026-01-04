const linkInput = document.getElementById("link-input");

const modal = document.getElementById("modal");
const openButton = document.getElementById("model-open-button");
const closeButton = document.getElementById("modal-close-button");
const shareButton = document.getElementById("modal-share-button");

const imageInput = document.getElementById("modal-image-input");
const imagePreview = document.getElementById("modal-image-preview");
const imageUploadText = document.getElementById("modal-image-upload-text");

window.onload = function () {
  imageInput.addEventListener("change", () => {
    const file = imageInput.files[0];

    if (!file) return;

    if (!file.type.startsWith("image/")) {
      alert("이미지 파일만 업로드할 수 있습니다.");
      imageInput.value = "";
      return;
    }

    const reader = new FileReader();

    reader.onload = (e) => {
      imagePreview.src = e.target.result;
      imagePreview.style.display = "block";
      imageUploadText.style.display = "none";
    };

    reader.readAsDataURL(file);
  });
};


openButton.addEventListener("click", () => {
  const value = linkInput.value.trim();

  const regex = /^https:\/\/qr\.kakaopay\.com\/.+$/;

  if (regex.test(value)) {
    modal.classList.add("is-open");
  } else {
    alert("올바른 카카오페이 QR 링크를 입력하세요.");

    linkInput.value = "";
  }
});

closeButton.addEventListener("click", () => {
  imageInput.value = "";
  imagePreview.src = "";
  imagePreview.style.display = "none";
  imageUploadText.style.display = "block";

  modal.classList.remove("is-open");
});

shareButton.addEventListener("click", () => {
  const contentTitle = document.getElementById('modal-title-textarea').value.trim();
  const contentDescription = document.getElementById('modal-despcription-textarea').value.trim();
  const buttonTitle = document.getElementById('modal-button-textarea').value.trim();
  const kakaopayURL = linkInput.value.trim();

  const file = imageInput.files[0];

  if (!file) {
    alert("이미지를 업로드하세요.");
    return;
  }

  Kakao.Share.uploadImage({
    file: file
  })
    .then(function (response) {
      const uploadedImageUrl = response.infos.original.url;

      const data = {
        contentTitle,
        contentDescription,
        contentImageURL: uploadedImageUrl,
        buttonTitle,
        kakaopayURL
      };

      fetch(`${window.location.origin}/feed/share`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      })
        .then(response => {
          if (response.ok) {
            alert("공유가 성공적으로 완료되었습니다!");
          } else {
            alert(response.body.message);
          }
        })
        .catch(e => {
          alert("알 수 없는 오류가 발생했습니다. 개발자에게 문의하세요. pjhneverdie@gmail.com");
        });
    })
    .catch(function (e) {
      alert("이미지 업로드에 실패했습니다. 이미지가 용량이 너무 크지 않은지 확인하세요.");
    });
});
