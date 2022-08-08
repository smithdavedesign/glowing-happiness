window.addEventListener("DOMContentLoaded", function(){
    const overlayContainer = document.querySelector(".cmp-product-comparison-overlay");
    const detailsBtns = document.querySelectorAll("[data-details-btn]");
    const closeBtn = document.querySelector(".cmp-product-comparison-overlay__btn-close");

    const compactData = document.querySelector(".cmp-product-comparison-overlay__data--compact");
    const expandedData = document.querySelector(".cmp-product-comparison-overlay__data--expanded");

    detailsBtns.forEach(btn => {
        btn.addEventListener("click", function(){
            compactData.style.display = 'none';
            expandedData.style.display = 'flex';
            overlayContainer.classList.add("cmp-product-comparison-overlay--expanded")
        });
    });

    closeBtn.addEventListener("click", function(){
        compactData.style.display = '';
        expandedData.style.display = 'none';
        overlayContainer.classList.remove("cmp-product-comparison-overlay--expanded")
    })
});