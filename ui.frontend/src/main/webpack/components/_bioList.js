import $ from 'jquery';

$('.cmp-bio-list__profile-link').click(function () {
    const profileId = $(this).data('profile-id');

    $(`.cmp-bio-list__profile__modal-wrapper[data-profile-id="${profileId}"]`).css(
        'display',
        'flex'
    );
    $('body').css('overflow', 'hidden');
});

$('.cmp-bio-list__profile__modal__close, .cmp-bio-list__profile__modal-wrapper').click(function (
    event
) {
    const target = event.target;
    const targetClassName = target.className;

    if (
        targetClassName === 'cmp-bio-list__profile__modal__close' ||
        targetClassName === 'cmp-bio-list__profile__modal-wrapper' ||
        $(target).is('svg') ||
        $(target).is('path')
    ) {
        closeModal();
    }
});

$(document).keyup(function (event) {
    if (event.key === 'Escape') {
        closeModal();
    }
});

function closeModal() {
    $('.cmp-bio-list__profile__modal-wrapper').hide();
    $('body').css('overflow', 'auto');
}
