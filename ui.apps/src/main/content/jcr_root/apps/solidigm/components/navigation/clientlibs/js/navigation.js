$('.mobile-open-close').click(function () {
    $('.mobile-content').toggleClass('hidden');
    $('.menu-icon').toggleClass('hidden');
    $('.menu-close-icon').toggleClass('hidden');
});

$('.link-item-content').click(function () {
    const subLinksVisible = $('.sub-links').css('display') !== 'none';

    $('.sub-links').toggleClass('hidden');
    $('.link-item-icon').css({
        transform: `rotate(${subLinksVisible ? '0' : '180'}deg) `,
    });
});

$('.search-icon').click(function () {
    $('.desktop-search').toggleClass('hidden');
    $('.search-icon').toggleClass('hidden');
});

$('.language-picker-button').click(function () {
    $('.language-picker-options').toggleClass('hidden');
});

$('.profile').click(function () {
    $('.login-modal-wrapper').removeClass('hidden');
});

$('.login-modal').click(function (e) {
    e.preventDefault();
});

$('.login-close-icon, .login-modal-wrapper').click(function (e) {
    if (!$(e.target).hasClass('login-close-icon') && !$(e.target).hasClass('login-modal-wrapper'))
        return;

    $('.login-modal-wrapper').addClass('hidden');
});

/* Navigation Scroll Logic */
/* Source: https://medium.com/@mariusc23/hide-header-on-scroll-down-show-on-scroll-up-67bbaae9a78c */

let didScroll = false;
let lastScrollTop = 0;
const delta = 5;
const navbarHeight = $('.navigation-bar').outerHeight();

$(window).scroll(function () {
    didScroll = true;
});

setInterval(function () {
    if (didScroll) {
        hasScrolled();

        didScroll = false;
    }
}, 250);

function hasScrolled() {
    const scrollTop = $(this).scrollTop();

    // Make sure they scroll more than delta.
    if (Math.abs(lastScrollTop - scrollTop) <= delta) return;

    // If they scrolled down and are past the navbar, add class .nav-up.
    // This is necessary so you never see what is "behind" the navbar.
    if (scrollTop > lastScrollTop && scrollTop > navbarHeight) {
        // Scroll Down
        $('.navigation-bar').addClass('nav-up');
    } else {
        // Scroll Up
        if (scrollTop + $(window).height() < $(document).height()) {
            $('.navigation-bar').removeClass('nav-up');
        }
    }

    lastScrollTop = scrollTop;
}

/* End Scroll Logic */
