const linkInput = document.getElementById("link-input");

const modal = document.getElementById("modal");
const openButton = document.getElementById("model-open-button");
const closeButton = document.getElementById("modal-close-button");
const shareButton = document.getElementById("modal-share-button");

const imageInput = document.getElementById("modal-image-input");
const imagePreview = document.getElementById("modal-image-preview");
const imageUploadText = document.getElementById("modal-image-upload-text");

let imageFile = null;

window.onload = function () {
  imageInput.addEventListener("change", () => {
    imageFile = imageInput.files[0];

    if (!imageFile) return;

    if (!imageFile.type.startsWith("image/")) {
      alert("이미지 파일만 업로드할 수 있습니다.");
      imageInput.value = "";
      return;
    }

    const options = {
      maxSizeMB: 5,
      maxWidthOrHeight: 1024,
      useWebWorker: true,
    };

    imageCompression(imageFile, options)
      .then((compressedFile) => {
        imageFile = compressedFile;

        const reader = new FileReader();

        reader.onload = (e) => {
          imagePreview.src = e.target.result;
          imagePreview.style.display = "block";
          imageUploadText.style.display = "none";
        };

        reader.readAsDataURL(imageFile);
      });
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
  imageFile = null;
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
  const kakaopayUrl = linkInput.value.trim();

  if (!imageFile) {
    alert("이미지를 업로드하세요.");
    return;
  }

  Kakao.Share.uploadImage({
    file: [imageFile]
  })
    .then(response => {
      const uploadedImageUrl = response.infos.original.url;

      const data = {
        contentTitle,
        contentDescription,
        contentImageUrl: uploadedImageUrl,
        buttonTitle,
        kakaopayUrl
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
            response.json().then(response => {
              console.log(response);

              Kakao.Share.sendDefault({
                objectType: 'feed',
                content: {
                  title: response.value["contentTitle"],
                  description: response.value["contentDescription"],
                  imageUrl: response.value["contentImageUrl"],
                  link: {
                    mobileWebUrl: window.location.origin,
                    webUrl: window.location.origin,
                  },
                },
                buttons: [
                  {
                    title: response.value["buttonTitle"],
                    link: {
                      mobileWebUrl: response.value["redirectUrl"],
                      webUrl: response.value["redirectUrl"],
                    },
                  },
                ],
              });

            });
          } else {
            response.json().then(response => {
              alert(response.message);
            });
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
