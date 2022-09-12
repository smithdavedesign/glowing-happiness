window.addEventListener("DOMContentLoaded", function () {
    const richtextEmbeds = document.querySelectorAll(".cmp-video");

    for (const embed of richtextEmbeds) {
        const button = embed.querySelector("button");
        const iframe = embed.querySelector("iframe");
        const preview = embed.querySelector("img");

        button.addEventListener("click", function () {
            preview.style.display = "none";
            button.style.display = "none";
            iframe.style.display = "block";
        });
    }
});
