window.addEventListener('DOMContentLoaded', function () {
    const richtextEmbeds = document.querySelectorAll('.cmp-video');

for (const embed of richtextEmbeds) {
        const button = embed.querySelector('button');
        const video = embed.querySelector('.cmp-video__video');
        const preview = embed.querySelector('img');
        const brightcoveVideo = embed.querySelector('video-js');

        button.addEventListener('click', function () {
            preview.style.display = 'none';
            button.style.display = 'none';
            video.style.display = 'block';
            brightcoveVideo?.player?.play();
        });
    }
});
