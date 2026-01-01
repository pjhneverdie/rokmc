Kakao.init("39e38609a0cbb50d554b80c1d616e2d5");

document.getElementById("modal-share-btn").addEventListener("click", function () {
    console.log("dasasd");
    
    Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
            title: "shareTitle",
            description: "shareDesc",
            imageUrl: "https://ogqmarket.img.sooplive.co.kr/sticker/17b83b9eb3204e7/main.png",
            link: {
                mobileWebUrl: "https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/",
                webUrl: "https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/",
            }
        },
        buttons: [
            {
                title: '웹으로 이동',
                link: {
                    mobileWebUrl: "https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/",
                    webUrl: "https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/",
                }
            }
        ]
    });
});
