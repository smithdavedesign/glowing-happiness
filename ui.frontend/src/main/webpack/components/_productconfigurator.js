window.addEventListener('DOMContentLoaded', async () => {
    // Tabs functionality
    function changeTabs(e) {
        const target = e.target;
        const parent = target.parentNode;
        const container = parent.parentNode.parentNode;

        // Remove all current selected tabs
        parent
            .querySelectorAll('[aria-selected="true"]')
            .forEach((tab) => tab.setAttribute('aria-selected', false));

        // Set this tab as selected
        target.setAttribute('aria-selected', true);

        // Hide all tab panels
        container
            .querySelectorAll('[role="tabpanel"]')
            .forEach((panel) => (panel.style.display = 'none'));

        // Show the selected panel
        container.parentNode.querySelector(
            `#${target.getAttribute('aria-controls')}`
        ).style.display = 'flex';
    }

    const tabs = document.querySelectorAll('[role="tab"]');

    // Add a click event handler to each tab
    tabs.forEach((tab) => {
        tab.addEventListener('click', changeTabs);
    });

    // Data fetching
    const productConfigurator = document.querySelector('.cmp-product-configurator');
    const compareButton = document.querySelector('.cmp-product-configurator__add-to-compare-btn');
    const interfaceSelect = productConfigurator.querySelector('#interface');
    const interfaceOptionsContainer = productConfigurator.querySelector('#interface + ul');
    const interfaceData = {
        key: 'interface',
        element: interfaceSelect,
        optionsContainer: interfaceOptionsContainer,
    };

    const formFactorSelect = productConfigurator.querySelector('#form-factor');
    const formFactorOptionsContainer = productConfigurator.querySelector('#form-factor + ul');

    const formFactorData = {
        key: 'formFactor',
        element: formFactorSelect,
        optionsContainer: formFactorOptionsContainer,
    };

    const capacitySelect = productConfigurator.querySelector('#capacity');
    const capacityOptionsContainer = productConfigurator.querySelector('#capacity + ul');
    const capacityData = {
        key: 'capacity',
        element: capacitySelect,
        optionsContainer: capacityOptionsContainer,
    };

    const path = productConfigurator.getAttribute("data-path");
    const request = await fetch(`/bin/solidigm/productConfigurator?path=${path}`);
    const data = await request.json();
    const {
        allFilters: { interface: interfaces, formFactor: formFactors, capacity: capacities },
    } = data;

    function setSelectOptions(optionsContainer, options) {
        optionsContainer.innerHTML = '';
        options.forEach((opt) => {
            optionsContainer.innerHTML += `<li class='cmp-custom-select__option'>${opt}</li>`;
        });
    }

    function handleOpen(target, otherSelects) {
        const validOptions = data.filters
            .filter((combination) => {
                return otherSelects.reduce((acc, curr) => {
                    if (!acc) { return false; }

                    return (
                        combination[curr.key] === curr.element.value || curr.element.value === ''
                    );
                }, true);
            })
            .map((combination) => combination[target.key]);

        target.optionsContainer.querySelectorAll('li').forEach((option) => {
            option.style.display = validOptions.includes(option.textContent) ? 'block' : 'none';
        });
    }

    setSelectOptions(interfaceOptionsContainer, interfaces);
    setSelectOptions(formFactorOptionsContainer, formFactors);
    setSelectOptions(capacityOptionsContainer, capacities);
    interfaceSelect.addEventListener('open', () =>
        handleOpen(interfaceData, [formFactorData, capacityData])
    );
    formFactorSelect.addEventListener('open', () =>
        handleOpen(formFactorData, [interfaceData, capacityData])
    );
    capacitySelect.addEventListener('open', () =>
        handleOpen(capacityData, [interfaceData, formFactorData])
    );

    function updateProductData(product) {
        // By the numbers
        Object.entries(product.attributes).forEach(([key, val]) => {
            var attr = productConfigurator.querySelector(`[data-name=${key}]`);
            if (attr != null) {
                if (attr.attributes['data-authored'] == null) {
                    attr.textContent = val;
                }
            }
        });
        // Performance
        productConfigurator.querySelector('[data-name=random-read]').textContent =
            product.attributes.RandomRead100PctSpanValue;
        productConfigurator.querySelector('[data-name=random-write]').textContent =
            product.attributes.RandomWrite100PctSpanValue;
        productConfigurator.querySelector('[data-name=power-active]').textContent =
            product.attributes.SSDPowerActive;
        productConfigurator.querySelector('[data-name=power-idle]').textContent =
            product.attributes.SSDPowerIdle;
        const regex = /\D*/gm;
        productConfigurator.querySelector(
            '#panel-performance .cmp-product-tabs__side-panel--metric :nth-child(1) .cmp-product-tabs__metric-title'
        ).textContent = product.attributes.SequentialReadMB.replace(regex, '');
        productConfigurator.querySelector(
            '#panel-performance .cmp-product-tabs__side-panel--metric :nth-child(2) .cmp-product-tabs__metric-title'
        ).textContent = product.attributes.SequentialWriteMB.replace(regex, '');

        // Reliability
        productConfigurator.querySelector('[data-name=warranty-period]').textContent =
            product.attributes.WarrantyLengthYears;
        productConfigurator.querySelector('[data-name=endurance-rating]').textContent =
            product.attributes.SSDEnduranceRating;
        productConfigurator.querySelector('[data-name=mean-time-netween-failures]').textContent =
            product.attributes.MTBF;
        productConfigurator.querySelector('[data-name=uncorrectable-bit-error-rate]').textContent =
            product.attributes.UBER;
        productConfigurator.querySelector('[data-name=vibration-operating]').textContent =
            product.attributes.VibrationOperating;
        productConfigurator.querySelector('[data-name=vibration-non-operating]').textContent =
            product.attributes.vibrationNonOperating;
        productConfigurator.querySelector('[data-name=shock]').textContent =
            product.attributes.SSDShock;
        productConfigurator.querySelector('[data-name=operating-temperature-range]').textContent =
            product.attributes.OperatingTemperature;

        // Ordering Specs
        productConfigurator.querySelector('[data-name=non-opal-ordering-code]').textContent =
            product.orderingSpecs.orderingCode;
        productConfigurator.querySelector('[data-name=non-opal-shipping-media]').textContent =
            product.orderingSpecs.shippingMedia;

        // Compliance
        productConfigurator.querySelector('[data-name=eccn]').textContent = product.compliance.eccn;
        productConfigurator.querySelector('[data-name=ccats]').textContent =
            product.compliance.ccats;
        productConfigurator.querySelector('[data-name=ushts]').textContent =
            product.compliance.ushts;
    }

    let selectedProduct;
    function handleSelectChange() {
        const interface = interfaceSelect.value;
        const formFactor = formFactorSelect.value;
        const capacity = capacitySelect.value;

        if (!interface || !formFactor || !capacity) {
            compareButton.setAttribute('disabled', '');
        } else {
            const productResult = data.filters.find((result) => {
                return (
                    result.interface === interface &&
                    result.formFactor === formFactor &&
                    result.capacity === capacity
                );
            });

            selectedProduct = data.results.find((result) => result.path === productResult.result);
            compareButton.removeAttribute('disabled');
            updateProductData(selectedProduct);
        }
    }

    interfaceSelect.addEventListener('change', handleSelectChange);
    formFactorSelect.addEventListener('change', handleSelectChange);
    capacitySelect.addEventListener('change', handleSelectChange);

    compareButton.addEventListener('click', function () {
        window.dispatchEvent(
            new CustomEvent('addProductToCompare', {
                detail: { path: selectedProduct.productPagePath, variation: selectedProduct.path },
            })
        );
    });
});
