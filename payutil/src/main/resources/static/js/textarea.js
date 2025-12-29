const textarea = document.querySelectorAll('.modal-textarea');

textarea.forEach(el => {
  el.addEventListener('input', () => {
    if (el.value.length > el.maxLength) {
      el.value = el.value.slice(0, el.maxLength);
    }
  });
});
