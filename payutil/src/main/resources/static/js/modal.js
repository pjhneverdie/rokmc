const modal = document.getElementById("modal");
const shareBtn = document.getElementById("share-btn");
const closeBtn = document.getElementById("close-btn");
const overlay = document.getElementById('modal-overlay');

shareBtn.addEventListener("click", () => {
  modal.classList.add("is-open");

});

closeBtn.addEventListener("click", () => {


  modal.classList.remove("is-open");
});

