import Splide from '@splidejs/splide';

(function () {
    const carousels = document.querySelectorAll(".cmp-carousel");
    carousels.forEach(carousel => {
        const duration = 1500;

        const slideWidth =
            getComputedStyle(carousel).getPropertyValue(
                "--carousel-slide-width"
            ) || undefined;

        new Splide(carousel, {
            type: "loop",
            padding: "12rem",
            focus: "center",
            pauseOnHover: false,
            pauseOnFocus: false,
            updateOnMove: true,
            arrows: false,
            autoplay: carousel.getAttribute("data-autoplay") === "true",
            interval: duration,
            speed: 950,
            fixedWidth: slideWidth,
            breakpoints: {
                767: {
                    padding: "1.5rem"
                },
                1023: {
                    padding: "3rem",
                    fixedWidth: undefined
                }
            }
        }).mount();

        carousel.classList.add("cmp-carousel--initialized");
        carousel.style.setProperty("--slide-duration", `${duration}ms`);
    });
})();
