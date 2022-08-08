const DESKTOP_WIDTH = 1024;
const MAX_WIDTH = 1920;

window.addEventListener("resize", handleResize);

handleResize();

// Custom css property to handle mobile browser bars.
const vh = innerHeight * 0.01;
document.documentElement.style.setProperty("--vh", `${vh}px`);

function handleResize() {
    const innerWidth =
        window.innerWidth >= MAX_WIDTH ? MAX_WIDTH : window.innerWidth;
    const innerHeight = window.innerHeight;

    // Only update vh on desktop, to prevent content shifting on mobile due to browser bars.
    if (innerHeight >= DESKTOP_WIDTH) {
        const vh = innerHeight * 0.01;
        document.documentElement.style.setProperty("--vh", `${vh}px`);
    }

    const vw = innerWidth * 0.01;
    document.documentElement.style.setProperty("--vw", `${vw}px`);
}
