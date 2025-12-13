const iamges = ["0.jpeg", "1.jpeg", "2.jpeg"];

const chosenImage = iamges[Math.floor(Math.random() * quotes.length)];

const bgImages = document.createElement("img");

bgImages.src = `/drill1/img/${chosenImage}`;

document.body.appendChild(bgImages);