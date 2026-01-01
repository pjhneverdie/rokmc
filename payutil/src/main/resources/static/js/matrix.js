const canvas = document.getElementById("matrix");
const ctx = canvas.getContext("2d");
const wrapper = document.getElementById("main-layout-wrapper");

const fontSize = 14;
const spacing = 6;
let columns;
let drops;

function resizeCanvas() {
    canvas.width = wrapper.clientWidth;
    canvas.height = wrapper.clientHeight;

    columns = Math.floor(canvas.width / (fontSize * spacing));
    drops = Array.from({ length: columns }, () => Math.floor(Math.random() * canvas.height / fontSize));
}

resizeCanvas();
window.addEventListener('resize', resizeCanvas);

const characters = '0011010110'.split('');

function drawMatrix() {
    ctx.fillStyle = "rgba(0, 0, 0, 0.05)";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = "#00FF00";
    ctx.font = fontSize + "px monospace";

    for (let i = 0; i < drops.length; i++) {
        const text = characters[Math.floor(Math.random() * characters.length)];
        const x = i * fontSize * spacing; // 간격 적용
        const y = drops[i] * fontSize;

        ctx.fillText(text, x, y);

        if (y > canvas.height && Math.random() > 0.975) {
            drops[i] = 0;
        } else {
            drops[i]++;
        }
    }
}

setInterval(drawMatrix, 50);

