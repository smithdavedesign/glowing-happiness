window.addEventListener("DOMContentLoaded", function () {
    const LIMIT = 100;
    const scrollToTopButton = document.querySelector(".scroll-up-button");

    function handleScroll() {
        if (window.scrollY > LIMIT) {
            scrollToTopButton.classList.add("scroll-up-button--visible");
        } else {
            scrollToTopButton.classList.remove("scroll-up-button--visible");
        }
    }

    document.addEventListener("scroll", handleScroll, { passive: true });
    scrollToTopButton.addEventListener("click", function () {
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    handleScroll();
});
