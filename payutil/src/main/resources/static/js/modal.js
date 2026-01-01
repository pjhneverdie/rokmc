const modal = document.getElementById("modal");
const openBtn = document.getElementById("model-open-btn");
const closeBtn = document.getElementById("modal-close-btn");

const imageInput = document.getElementById("modal-image-input");
const imagePreview = document.getElementById("modal-image-preview");
const imageUploadText = document.getElementById("modal-image-upload-text");

openBtn.addEventListener("click", () => {
  modal.classList.add("is-open");
});

closeBtn.addEventListener("click", () => {
  imageInput.value = "";
  imagePreview.src = "";
  imagePreview.style.display = "none";
  imageUploadText.style.display = "block";

  modal.classList.remove("is-open");
});

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
