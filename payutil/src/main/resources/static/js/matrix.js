const canvas = document.getElementById("matrix");
const ctx = canvas.getContext("2d");

// 캔버스 크기 설정
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

// 글자 설정
const characters = 'payutil'.split('');
const fontSize = 16;

// 열 수
const columns = Math.floor(canvas.width / fontSize) / 3;
const drops = [];

// Drops 초기화 (각 열의 시작 높이를 랜덤으로 설정)
for (let x = 0; x < columns; x++) {
    drops[x] = Math.floor(Math.random() * canvas.height / fontSize);
}

// 애니메이션 실행
function drawMatrix() {
    ctx.fillStyle = "rgba(0, 0, 0, 0.05)";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = "#00FF00"; // 초록색 글자
    ctx.font = fontSize + "px monospace";

    for (let i = 0; i < drops.length; i++) {
        const text = characters[Math.floor(Math.random() * characters.length)];
        
        const x = i * fontSize * 6;
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