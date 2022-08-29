import $ from 'jquery';

(function () {
    $('.partners-list .solidigm-select').on('change', onFilterChange);

    let productSelector = '',
        countrySelector = '';
    function onFilterChange() {
        if (this.name === 'countries') {
            countrySelector =
                this.value === '' ? '' : "[data-partner-country='" + this.value + "']";
        } else if (this.name === 'products') {
            productSelector =
                this.value === '' ? '' : "[data-partner-product*='" + this.value + "']";
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
