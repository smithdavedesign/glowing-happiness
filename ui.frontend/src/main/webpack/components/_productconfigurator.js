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

    const request = await fetch('/bin/solidigm/productConfigurator');
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
                    if (!acc) return false;

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
        Object.entries(product.byTheNumbers).forEach(([key, val]) => {
            productConfigurator.querySelector(`[data-name=${key}]`).textContent = val;
        });
        // Performance
        productConfigurator.querySelector('[data-name=random-read]').textContent =
            product.performance.randomRead;
        productConfigurator.querySelector('[data-name=random-write]').textContent =
            product.performance.randomWrite;
        productConfigurator.querySelector('[data-name=power-active]').textContent =
            product.performance.powerActive;
        productConfigurator.querySelector('[data-name=power-idle]').textContent =
            product.performance.powerIdle;
        productConfigurator.querySelector(
            '#panel-performance .cmp-product-tabs__side-panel--metric :nth-child(1) .cmp-product-tabs__metric-title'
        ).textContent = product.performance.speeds[0].title;
        productConfigurator.querySelector(
            '#panel-performance .cmp-product-tabs__side-panel--metric :nth-child(1) .cmp-product-tabs__metric-subtitle'
        ).textContent = product.performance.speeds[0].unit;
        productConfigurator.querySelector(
            '#panel-performance .cmp-product-tabs__side-panel--metric :nth-child(1) .cmp-product-tabs__metric-body'
        ).textContent = product.performance.speeds[0].description;
        productConfigurator.querySelector(
            '#panel-performance .cmp-product-tabs__side-panel--metric :nth-child(2) .cmp-product-tabs__metric-title'
        ).textContent = product.performance.speeds[1].title;
        productConfigurator.querySelector(
            '#panel-performance .cmp-product-tabs__side-panel--metric :nth-child(2) .cmp-product-tabs__metric-subtitle'
        ).textContent = product.performance.speeds[1].unit;
        productConfigurator.querySelector(
            '#panel-performance .cmp-product-tabs__side-panel--metric :nth-child(2) .cmp-product-tabs__metric-body'
        ).textContent = product.performance.speeds[1].description;

        // Reliability
        productConfigurator.querySelector('[data-name=warranty-period]').textContent =
            product.reliability.warrantyPeriod;
        productConfigurator.querySelector('[data-name=endurance-rating]').textContent =
            product.reliability.enduranceRating;
        productConfigurator.querySelector('[data-name=mean-time-netween-failures]').textContent =
            product.reliability.meanTimeBetweenFailures;
        productConfigurator.querySelector('[data-name=uncorrectable-bit-error-rate]').textContent =
            product.reliability.uncorrectableBitErrorRate;
        productConfigurator.querySelector('[data-name=vibration-operating]').textContent =
            product.reliability.vibrationOperating;
        productConfigurator.querySelector('[data-name=vibration-non-operating]').textContent =
            product.reliability.vibrationNonOperating;
        productConfigurator.querySelector('[data-name=shock]').textContent =
            product.reliability.shock;
        productConfigurator.querySelector('[data-name=operating-temperature-range]').textContent =
            product.reliability.operatingTemperatureRange;

        // Ordering Specs
        productConfigurator.querySelector('[data-name=non-opal-ordering-code]').textContent =
            product.orderingSpecs.leftTab.orderingCode;
        productConfigurator.querySelector('[data-name=non-opal-shipping-media]').textContent =
            product.orderingSpecs.leftTab.shippingMedia;
        productConfigurator.querySelector('[data-name=opal-ordering-code]').textContent =
            product.orderingSpecs.rightTab.orderingCode;
        productConfigurator.querySelector('[data-name=opal-shipping-media]').textContent =
            product.orderingSpecs.rightTab.shippingMedia;

        // PCN/MDDS
        const pcnmdds = document.querySelector('#panel-pcn-mdds .cmp-product-tabs__key-val');
        pcnmdds.innerHTML = '';
        product.pcnmdds.forEach((entry) => {
            const links = entry.data
                .map((link) => `<a href="${link.url}">${link.text}</a>`)
                .join('');
            pcnmdds.innerHTML += `<dt>${entry.label}</dt> <dd>${links}</dd>`;
        });

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
                detail: { path: selectedProduct.path % 3 },
            })
        );
    });
});
