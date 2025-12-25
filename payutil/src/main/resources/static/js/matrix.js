const canvas = document.getElementById("matrix");
    const ctx = canvas.getContext("2d");

    // 캔버스 크기 설정
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    // 글자 설정
    const characters = 'payutil'.split('');
    const fontSize = 16;

    // 열 수 줄이기 (1열에 하나씩 떨어지도록 설정)
    const columns = Math.floor(canvas.width / fontSize) / 3; // 열 수를 줄여줌
    const drops = [];

// Drops 초기화 (각 열의 시작 높이를 랜덤으로 설정)
    for (let x = 0; x < columns; x++) {
        drops[x] = Math.floor(Math.random() * canvas.height / fontSize); // 랜덤한 시작 높이
    }

    // 애니메이션 실행
    function drawMatrix() {
        ctx.fillStyle = "rgba(0, 0, 0, 0.05)";
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        ctx.fillStyle = "#00FF00"; // 초록색 글자
        ctx.font = fontSize + "px monospace";

        for (let i = 0; i < drops.length; i++) {
            const text = characters[Math.floor(Math.random() * characters.length)];
            // 열 간격을 더 넓게 설정 (여기서 6으로 바꾸면 간격이 더 넓어짐)
            const x = i * fontSize * 6; // 6으로 변경해서 간격을 넓힘
            const y = drops[i] * fontSize;

            ctx.fillText(text, x, y);

            // 화면 하단에 도달하면 다시 랜덤 높이로 초기화
            if (y > canvas.height && Math.random() > 0.975) {
                drops[i] = 0;
            } else {
                drops[i]++;
            }
        }
    }

    // 매트릭스 애니메이션 반복
    setInterval(drawMatrix, 50);