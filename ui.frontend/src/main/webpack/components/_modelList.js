(function () {
    // @TODO get products from the API
    const products = [];

    // Important: the filter name should be the same as the corresponding
    // property name of the product, name of the select element and data-attribute name of the product element.
    const filters = [
        {
            name: 'interfaces',
            selected: '',
            dropdownElement: $('#model-list__filters__interface-filter'),
        },
        {
            name: 'formFactors',
            selected: '',
            dropdownElement: $('#model-list__filters__form-factor-filter'),
        },
        {
            name: 'capacities',
            selected: '',
            dropdownElement: $('#model-list__filters__capacity-filter'),
        },
    ];

    function setFilterSelected(filterName, value) {
        filters.find((filter) => filter.name === filterName).selected = value;
    }

    $('.model-list__filters__filter').on('change', onFilterChange);

    populateDropdowns();

    function onFilterChange() {
        setFilterSelected(this.name, this.value);

        populateDropdowns();

        updateProducts();
    }

    function populateDropdowns() {
        for (const filter of filters) {
            // The dropdown options are dynamically fetched from the products.
            // The machingProducts are all products that match all OTHER filters (so not the current one).
            const matchingProducts = getMatchingProducts(filter);

            // From these products, we get the values for the specific filter.
            const filterOptions = getFilterOptions(matchingProducts, filter.name);

            populateDropdown(filter.dropdownElement, filterOptions);
        }
    }

    function getMatchingProducts(filter) {
        return products.filter((product) => {
            const otherFilters = filters.filter((f) => f.name !== filter.name);

            for (const otherFilter of otherFilters) {
                if (!matchesFilter(product[otherFilter.name], otherFilter)) return false;
            }

            return true;
        });
    }

    function matchesFilter(productValue, filter) {
        return filter.selected === '' || productValue.includes(filter.selected);
    }

    function getFilterOptions(products, propertyName) {
        const filterOptions = [];

        for (const product of products) {
            filterOptions.push(...product[propertyName]);
        }

        return [...new Set(filterOptions)];
    }

    function populateDropdown(dropdownElement, filters) {
        dropdownElement.find('option').not(':first, :selected').remove();

        const selectedValue = dropdownElement.find(':selected').text();

        for (const filter of filters) {
            if (filter === selectedValue) continue;

            dropdownElement.append($('<option />').val(filter).text(filter));
        }
    }

    function updateProducts() {
        $('.model-list__products__product').each(function (index, element) {
            if (productElementMatchesAllFilters(element)) {
                $(element).show();
            } else {
                $(element).hide();
            }
        });
    }

    function productElementMatchesAllFilters(productElement) {
        for (const filter of filters) {
            const dataString = $(productElement).data(filter.name.toLowerCase());
            const productValue = dataString.split(',');

            if (!matchesFilter(productValue, filter)) return false;
        }

        return true;
    }
})();
