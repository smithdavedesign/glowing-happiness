window.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('[data-modal-target]').forEach((btn) => {
        btn.addEventListener('click', function () {
            const modal = document.getElementById(btn.getAttribute('data-modal-target'));
            const closeBtn = modal.querySelector('.cmp-modal__close-btn');
            const iframe = modal.querySelector('iframe');
            const brightcoveVideo = modal.querySelector("video-js");

            if (iframe && !iframe.getAttribute('src')) {
                iframe.setAttribute('src', iframe.getAttribute('data-src'));
            }
            brightcoveVideo?.player?.play();

            modal.showModal();
            document.body.overflow = 'hidden';
            closeBtn.focus();

            function closeModal() {
                if (iframe) {
                    // Resets iframe
                    const currentSrc = iframe.getAttribute('src');
                    iframe.setAttribute('data-src', currentSrc);
                    iframe.setAttribute('src', '');
                }
                brightcoveVideo?.player?.pause();
                modal.close();
                document.body.overflow = 'visible';
            }

            // Closes modal when clicked outside
            function onClick(event) {
                if (event.target === modal) {
                    modal.close();
                }
            }

            modal.addEventListener('click', onClick, { once: true });
            modal.addEventListener('close', closeModal, { once: true });
            closeBtn.addEventListener('click', onClick, { once: true });
        });
    });
});
