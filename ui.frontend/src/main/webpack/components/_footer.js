import $ from 'jquery';

(function () {
    let didScroll = false;
    const navbarHeight = $('.header__navigation-bar').outerHeight();

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

        if (scrollTop > navbarHeight) {
            if ($('.scroll-up-button').css('display') !== 'none') return;

            $('.scroll-up-button').css('display', 'flex').hide().fadeIn();
        } else {
            $('.scroll-up-button').fadeOut();
        }
    }

    $('.scroll-up-button').click(function () {
        $('html').animate({ scrollTop: 0 }, 500);
    });
})();
