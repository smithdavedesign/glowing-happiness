import $ from 'jquery';

(function () {
    $('.partners-list .cmp-custom-select__input').on('change', onFilterChange);

    let productSelector = '',
        countrySelector = '';
    function onFilterChange() {
        const currentValue = this.value;
        const option = [...this.parentElement.querySelectorAll('.cmp-custom-select__options li')]
            .find((item) => {
                return currentValue === item.textContent.trim();
            })
            ?.getAttribute('data-value');

        if (this.name === 'countries') {
            countrySelector = option === '' ? '' : "[data-partner-country='" + option + "']";
        } else if (this.name === 'products') {
            productSelector = option === '' ? '' : "[data-partner-product*='" + option + "']";
        }

        $('.partners-list__partners__table-row').hide();

        const visibleRows = $(
            '.partners-list__partners__table-row' + countrySelector + productSelector
        );
        visibleRows.show();

        if (visibleRows.length === 0) {
            $('.partners-list__no-results').show();
        } else {
            $('.partners-list__no-results').hide();
        }
    }
})();
