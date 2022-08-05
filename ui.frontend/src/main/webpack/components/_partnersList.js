(function () {
    const partners = getPartners();

    let selectedCountry = "";
    let selectedProduct = "";

    $(".partners-list__filters__filter").on("change", onFilterChange);

    populateDropdowns(partners);

    function onFilterChange() {
        if (this.name === "countries") {
            selectedCountry = this.value;
        } else {
            selectedProduct = this.value;
        }

        const countryQuery = selectedCountry
            ? `[data-partner-country="${selectedCountry}"]`
            : "";
        const productQuery = selectedProduct
            ? `[data-partner-product="${selectedProduct}"]`
            : "";

        $(".partners-list__partners__table-row").hide();

        const visibleRows = $(
            `.partners-list__partners__table-row${countryQuery}${productQuery}`
        );
        visibleRows.show();

        if (visibleRows.length === 0) {
            $(".partners-list__no-results").show();
        } else {
            $(".partners-list__no-results").hide();
        }
    }

    function populateDropdowns(partners) {
        const countryFilters = getCountryFilters(partners);
        const countryDropdown = $("#partners-list__filters__country-filter");

        const productFilters = getProductFilters(partners);
        const productDropdown = $("#partners-list__filters__product-filter");

        populateDropdown(countryDropdown, countryFilters);
        populateDropdown(productDropdown, productFilters);
    }

    function getCountryFilters(partners) {
        const countries = partners.map(partner => partner.country);

        return countries.filter((country, index, self) => {
            return self.findIndex(c => c.name === country.name) === index;
        });
    }

    function getProductFilters(partners) {
        const products = partners.map(partner => partner.product);

        return products.filter((product, index, self) => {
            return self.findIndex(p => p.name === product.name) === index;
        });
    }

    function populateDropdown(dropdown, filters) {
        for (const filter of filters) {
            dropdown.append($("<option />").val(filter.id).text(filter.name));
        }
    }

    // Dummy function because it's hard to fetch the data from a local file.
    function getPartners() {
        return [
            {
                name: "ASI",
                country: {
                    id: "1",
                    name: "Canada"
                },
                link: "https://solidigm.com",
                product: {
                    id: "1",
                    name: "Product 1"
                },
                additionalInfo: ""
            },
            {
                name: "Dicker Data",
                country: {
                    id: "2",
                    name: "Australia"
                },
                link: "https://solidigm.com",
                product: {
                    id: "1",
                    name: "Product 1"
                },
                additionalInfo: ""
            },
            {
                name: "Ingram Micro",
                country: {
                    id: "3",
                    name: "Belgium"
                },
                link: "https://solidigm.com",
                product: {
                    id: "1",
                    name: "Product 1"
                },
                additionalInfo: "Country of Inc"
            },
            {
                name: "Ingram Micro",
                country: {
                    id: "1",
                    name: "Canada"
                },
                link: "https://solidigm.com",
                product: {
                    id: "2",
                    name: "Product 2"
                },
                additionalInfo: ""
            },
            {
                name: "Synnex Corp",
                country: {
                    id: "1",
                    name: "Canada"
                },
                link: "https://solidigm.com",
                product: {
                    id: "2",
                    name: "Product 2"
                },
                additionalInfo: ""
            },
            {
                name: "ASBIS",
                country: {
                    id: "4",
                    name: "Cyprus"
                },
                link: "https://solidigm.com",
                product: {
                    id: "2",
                    name: "Product 2"
                },
                additionalInfo: ""
            },
            {
                name: "Logicom",
                country: {
                    id: "4",
                    name: "Cyprus"
                },
                link: "https://solidigm.com",
                product: {
                    id: "2",
                    name: "Product 2"
                },
                additionalInfo: ""
            }
        ];
    }
})();
