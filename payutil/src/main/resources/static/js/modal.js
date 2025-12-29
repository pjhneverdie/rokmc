const modal = document.getElementById("modal");
const shareBtn = document.getElementById("share-btn");
const closeBtn = document.getElementById("close-btn");

shareBtn.addEventListener("click", () => {
  modal.classList.add("is-open");
});

closeBtn.addEventListener("click", () => {
  modal.classList.remove("is-open");
});
