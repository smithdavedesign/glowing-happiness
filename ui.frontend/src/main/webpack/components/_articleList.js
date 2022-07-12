import Splide from '@splidejs/splide';

window.addEventListener("DOMContentLoaded", function(){
    const carousels = document.querySelectorAll(".cmp-article-list .splide");
    
    carousels.forEach(carousel => {
        const duration = 1500;
    
        new Splide(carousel, {
            type: "slide",
            padding: "0",
            gap: "32px",
            focus: "center",
            updateOnMove: true,
            arrows: false,
            interval: duration,
            speed: 950,
            perPage: 3,
            perMove: 3,
            drag: false,
            isNavigation: true,
            breakpoints: {
                1023: {
                    perPage: 2,
                    gap: "20px",
                    padding: "20px",
                    isNavigation: false,
                    drag: true
                },
                767: {
                    perPage: 1,
                }
            }
        }).mount();
    
        carousel.style.setProperty("--slide-duration", `${duration}ms`);
    });
})
