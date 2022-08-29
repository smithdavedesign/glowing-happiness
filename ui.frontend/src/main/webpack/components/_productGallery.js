import $ from 'jquery';

(() => {
    let showMoreIsClicked = false;

    $(window).resize(function () {
        if (showMoreIsClicked) return;

        calculateThumbnails();
    });

    calculateThumbnails();

    $('.product-gallery__thumbnails__image').click(function (event) {
        const thumbnailSrc = $(this).attr('src');
        const thumbnailAlt = $(this).attr('alt');

        $('.product-gallery__product-image').attr('src', thumbnailSrc);
        $('.product-gallery__product-image').attr('alt', thumbnailAlt);

        $('.product-gallery__thumbnails__image').removeClass(
            'product-gallery__thumbnails__image--selected'
        );
        $(this).addClass('product-gallery__thumbnails__image--selected');
    });

    $('.product-gallery__thumbnails__show-more-tile').click(function (event) {
        $('.product-gallery__thumbnails__image-wrapper').show();
        $('.product-gallery__thumbnails__show-more-tile').hide();

        showMoreIsClicked = true;
    });

    function calculateThumbnails() {
        // Reset values to account for resize updates.
        $('.product-gallery__thumbnails__image-wrapper').show();
        $('.product-gallery__thumbnails__show-more-tile').hide();

        const isTablet = $(document).width() >= 768;
        const maxTotalThumbnails = isTablet ? 7 : 5;

        const firstThumbnail = $('.product-gallery__thumbnails__image')[0];
        $(firstThumbnail).addClass('product-gallery__thumbnails__image--selected');

        const totalThumbnails = $('.product-gallery__thumbnails__image-wrapper').length;
        const showMoreTextElement = $('.product-gallery__thumbnails__show-more-tile__text');

        showMoreTextElement.text(`+${totalThumbnails - (maxTotalThumbnails - 1)}`);

        if (totalThumbnails >= maxTotalThumbnails) {
            $('.product-gallery__thumbnails__image-wrapper').each(function (index, element) {
                if ($(element).data('index') > maxTotalThumbnails) {
                    $(element).hide();
                }

                if (
                    totalThumbnails > maxTotalThumbnails &&
                    $(element).data('index') === maxTotalThumbnails
                ) {
                    const showMoreElement = $(element).children(
                        '.product-gallery__thumbnails__show-more-tile'
                    );

                    showMoreElement.css('display', 'flex');
                }
            });
        }
    }
})();
