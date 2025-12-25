// 캔버스 설정
const canvas = document.getElementById("matrix");
const ctx = canvas.getContext("2d");

// 화면 크기 맞추기
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

// 문자 배열 (비슷한 느낌의 문자)
const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*()*&^%';
const fontSize = 16; // 글자 크기
const columns = canvas.width / fontSize; // 한 열에 들어가는 문자 수
const drops = []; // 각 열마다 떨어지는 문자

// drops 배열 초기화
for (let i = 0; i < columns; i++) {
    drops[i] = Math.random() * canvas.height;
}

// 매트릭스 효과
function drawMatrix() {
    ctx.fillStyle = "rgba(0, 0, 0, 0.05)"; // 배경을 투명하게 덮어 글자 효과를 남긴다
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = "#0F0"; // 글자 색상 (초록색)
    ctx.font = fontSize + "px monospace"; // 폰트 설정

    // 각 열마다 글자를 떨어뜨림
    for (let i = 0; i < drops.length; i++) {
        const text = characters[Math.floor(Math.random() * characters.length)];
        ctx.fillText(text, i * fontSize, drops[i] * fontSize);

        // 글자가 떨어지다가 화면을 넘어가면 다시 위로 초기화
        if (drops[i] * fontSize > canvas.height && Math.random() > 0.975) {
            drops[i] = 0;
        }

        // 떨어지는 속도 조정
        drops[i]++;
    }
}

// 애니메이션
setInterval(drawMatrix, 33); // 약 30프레임 per second
