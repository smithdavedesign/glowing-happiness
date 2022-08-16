import Splide from '@splidejs/splide';

(function () {
    const carousels = document.querySelectorAll('.cmp-carousel');
    carousels.forEach((carousel) => {
        const duration = 7500;

        const slideWidth =
            getComputedStyle(carousel).getPropertyValue('--carousel-slide-width') || undefined;

        const slider = new Splide(carousel, {
            type: 'loop',
            autoplay: carousel.getAttribute('data-autoplay') === 'true',
            padding: '12rem',
            focus: 'center',
            pauseOnHover: false,
            pauseOnFocus: false,
            updateOnMove: true,
            arrows: false,
            interval: duration,
            speed: 950,
            fixedWidth: slideWidth,
            breakpoints: {
                767: {
                    padding: '1.5rem',
                },
                1023: {
                    padding: '3rem',
                    fixedWidth: undefined,
                },
            },
        });

        slider.on('mounted', function(){
            setTimeout(() => {
                carousel.classList.add('cmp-carousel--initialized');
            }, 1)
            const buttons = carousel.querySelectorAll("li[role=presentation]");
            buttons.forEach((btn, i) => {
                btn.addEventListener("click", function(e){
                    e.stopPropagation();
                    e.preventDefault();
    
                    slider.go(i)
                });
            })
        })
        slider.mount()
        carousel.style.setProperty('--slide-duration', `${duration}ms`);
    });
})();
