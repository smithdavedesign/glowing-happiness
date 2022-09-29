import $ from 'jquery';

$('.header__mobile-open-close').click(function () {
    $('.header__mobile-content').toggleClass('hidden');
    $('.header__menu-icon').toggleClass('hidden');
    $('.header__menu-close-icon').toggleClass('hidden');
});

$('.header__search-btn').click(function () {
    $('.header__desktop-search').toggleClass('hidden');
    $('.header__search-btn').toggleClass('hidden');
    $('.header__search-input').focus();
});

let removeModalEscapeListener = null;
function closeLoginModal() {
    $('.header__login-modal-wrapper').addClass('hidden');
    removeModalEscapeListener && removeModalEscapeListener();
}
$('.header__profile-btn').click(function () {
    $('.header__login-modal-wrapper').removeClass('hidden');
    $('.header__email-input').focus();

    function modalEscapeHandler(e) {
        if (e.key === 'Escape') {
            closeLoginModal();
        }
    }
    document.addEventListener('keyup', modalEscapeHandler);
    removeModalEscapeListener = () => document.removeEventListener('keyup', modalEscapeHandler);
});

$('.header__login-close-icon').click(() => {
    closeLoginModal();
});

$('.header__login-modal-wrapper').click(function (e) {
    if (!$(e.target).hasClass('header__login-modal-wrapper')) return;
    closeLoginModal();
});

$('.header__login-modal').click(function (e) {
    e.preventDefault();
});

/* Navigation Scroll Logic */
/* Source: https://medium.com/@mariusc23/hide-header__on-scroll-down-show-on-scroll-up-67bbaae9a78c */

let didScroll = false;
let lastScrollTop = 0;
const delta = 5;
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

    // Make sure they scroll more than delta.
    if (Math.abs(lastScrollTop - scrollTop) <= delta) return;

    // If they scrolled down and are past the navbar, add class .nav-up.
    // This is necessary so you never see what is "behind" the navbar.
    if (scrollTop > lastScrollTop && scrollTop > navbarHeight) {
        // Scroll Down
        $('.header__navigation-bar').addClass('header--nav-up');
    } else {
        // Scroll Up
        if (scrollTop + $(window).height() < $(document).height()) {
            $('.header__navigation-bar').removeClass('header--nav-up');
        }
    }

    lastScrollTop = scrollTop;
}

/* End Scroll Logic */

document.addEventListener('DOMContentLoaded', function () {
    const activeClass = 'header__link-item--has-submenu-active';

    let clearEscKeyEvent = null;
    let clearClickOutside = null;
    let activeElement = null;

    function clearActiveElement() {
        activeElement && activeElement.parentElement.classList.remove(activeClass);
        clearEscKeyEvent && clearEscKeyEvent();
        clearClickOutside && clearClickOutside();
        activeElement = null;
    }

    // Hides the submenu when clicking outside of it
    function handleClickOutside(e) {
        if (!activeElement) return;

        const path = e.composedPath();
        for (const element of path) {
            if (element.classList && element.classList.contains('header__dropdown-container')) {
                return;
            }
        }
        clearActiveElement();
    }

    function handleEscKeyPress(e) {
        if (activeElement && e.key === 'Escape') {
            clearActiveElement();
        }
    }

    function openSubmenu(submenu) {
        document
            .querySelectorAll('.header__link-item--has-submenu')
            .forEach((el) => el.classList.remove());

        submenu.parentElement.classList.add(activeClass);

        document.addEventListener('keyup', handleEscKeyPress);
        document.addEventListener('click', handleClickOutside);

        clearEscKeyEvent = () => document.removeEventListener('keyup', handleEscKeyPress);
        clearClickOutside = () => document.removeEventListener('click', handleClickOutside);
        activeElement = submenu;
    }

    const menusWithSubmenus = document.querySelectorAll('.header__link-item--has-submenu');

    menusWithSubmenus.forEach((menuWithSubmenu) => {
        const trigger = menuWithSubmenu.querySelector('.header__link-item-trigger');

        trigger.addEventListener('click', function (e) {
            e.stopPropagation();
            const submenu = menuWithSubmenu.querySelector('.header__dropdown-container');

            const isDifferentMenu = submenu.isEqualNode(activeElement) === false;
            clearActiveElement();
            if (isDifferentMenu) {
                openSubmenu(submenu);
            }
        });
    });

    document.querySelectorAll('.header__log-out-btn').forEach((logOutBtn) => {
        logOutBtn.addEventListener('click', async function () {
            try {
                await fetch('/bin/solidigm/signOut');
            } catch (error) {
                console.log(error);
            }
            window.location.replace('/');
        });
    });

    async function isSignedIn() {
        const request = await fetch('/bin/solidigm/isSignedIn');
        const response = await request.json();

        return response;
    }

    isSignedIn().then((response) => {
        const userIcons = document.querySelectorAll('.header__user-settings');
        const header = document.querySelector('.header__navigation-bar');
        if (response.isSignedIn) {
            header.classList.add('header__navigation-bar--signed-in');
        } else {
            header.classList.add('header__navigation-bar--not-signed-in');
        }

        for (const userIcon of userIcons) {
            userIcon.classList.add('header__user-settings--initialized');
        }
    });
});
