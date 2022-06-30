import Splide from '@splidejs/splide';

(function () {
    const carousels = document.querySelectorAll('.splide');

    carousels.forEach((carousel) => {
        const duration = 1500;

        new Splide(carousel, {
            type: 'loop',
            padding: '12rem',
            focus: 'center',
            pauseOnHover: false,
            pauseOnFocus: false,
            updateOnMove: true,
            arrows: false,
            autoplay: carousel.getAttribute('data-autoplay') === 'true',
            interval: duration,
            speed: 950,
            breakpoints: {
                767: {
                    padding: '1.5rem',
                },
                1023: {
                    padding: '3rem',
                },
            },
        }).mount();

        carousel.style.setProperty('--slide-duration', `${duration}ms`);
    });
})();
