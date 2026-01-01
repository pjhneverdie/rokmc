
document.getElementById("modal-share-btn").addEventListener("click", function () {
    Kakao.Share.sendDefault({
        objectType: 'feed',
        content: {
            title: '딸기 치즈 케익',
            description: '#케익 #딸기 #삼평동 #카페 #분위기 #소개팅',
            imageUrl: 'http://k.kakaocdn.net/dn/bLPLfX/dJMcacayNt1/iWQpxLOqbqcyg2hxzKCEE1/kakaolink40_original.png',
            link: {
                // [내 애플리케이션] > [플랫폼] 에서 등록한 사이트 도메인과 일치해야 함
                mobileWebUrl: 'https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/',
                webUrl: 'https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/',
            },
        },
        buttons: [
            {
                title: '웹으로 보기',
                link: {
                    mobileWebUrl: 'https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/',
                    webUrl: 'https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/',
                },
            },
        ],
    });
});
