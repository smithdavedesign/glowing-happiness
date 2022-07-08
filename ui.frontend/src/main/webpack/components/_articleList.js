import Splide from '@splidejs/splide';

(function () {
    const carousels = document.querySelectorAll('.cmp-article-list__articles-container.splide');

    carousels.forEach((carousel) => {
        new Splide(carousel, {
            type: 'slide',
            padding: '0',
            gap: '32px',
            focus: 'center',
            updateOnMove: true,
            arrows: false,
            speed: 950,
            perPage: 3,
            breakpoints: {
                767: {
                    perPage: 1,
                    gap: '20px',
                },
            },
        }).mount();
    });
})();
