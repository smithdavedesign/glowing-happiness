(async function () {
    const modelLists = document.querySelectorAll('.model-list');
    for (const modelList of modelLists) {
        async function getData() {
            const path = modelList.getAttribute("data-path");
            const request = await fetch(`/bin/solidigm/modelList?path=${path}`);
            const response = await request.json();

            return response;
        }

        function setSelectOptions(optionsContainer, options) {
            optionsContainer.innerHTML = '';
            options.forEach((opt) => {
                optionsContainer.innerHTML += `<li class='cmp-custom-select__option'>${opt}</li>`;
            });
        }

        const productsContainer = modelList.querySelector('.model-list__products');
        const productTemplate = modelList.querySelector('.model-list__template').innerHTML;
        const featuredProducts = modelList.querySelectorAll(
            '.model-list__products > .model-list__products__product'
        );
        const interfaceSelect = modelList.querySelector('#interface');
        const interfaceOptionsContainer = modelList.querySelector('#interface + ul');
        const interfaceData = {
            key: 'interface',
            element: interfaceSelect,
            optionsContainer: interfaceOptionsContainer,
            anyLabel: modelList.getAttribute('data-interface-any-label'),
        };

        const formFactorSelect = modelList.querySelector('#form-factor');
        const formFactorOptionsContainer = modelList.querySelector('#form-factor + ul');

        const formFactorData = {
            key: 'formFactor',
            element: formFactorSelect,
            optionsContainer: formFactorOptionsContainer,
            anyLabel: modelList.getAttribute('data-form-factor-any-label'),
        };

        const capacitySelect = modelList.querySelector('#capacity');
        const capacityOptionsContainer = modelList.querySelector('#capacity + ul');
        const capacityData = {
            key: 'capacity',
            element: capacitySelect,
            optionsContainer: capacityOptionsContainer,
            anyLabel: modelList.getAttribute('data-capacity-any-label'),
        };

        const data = await getData();
        const {
            allFilters: { interface: interfaces, formFactor: formFactors, capacity: capacities },
        } = data;

        setSelectOptions(interfaceOptionsContainer, interfaces);
        setSelectOptions(formFactorOptionsContainer, formFactors);
        setSelectOptions(capacityOptionsContainer, capacities);

        function handleSelectChange(e) {
            const interface = interfaceSelect.value || interfaceData.anyLabel;
            const formFactor =
                formFactorSelect.value || formFactorData.anyLabel;
            const capacity = capacitySelect.value || capacityData.anyLabel;

            productsContainer.innerHTML = "";

            if (
                interface === interfaceData.anyLabel &&
                formFactor === formFactorData.anyLabel &&
                capacity === capacityData.anyLabel &&
                !!featuredProducts.length
            ) {
                featuredProducts.forEach(featuredProduct => {
                    productsContainer.appendChild(featuredProduct);
                });
            } else {
                const selectedCombination = data.filters.find(combination => {
                    return (
                        combination.interface === interface &&
                        combination.formFactor === formFactor &&
                        combination.capacity === capacity
                    );
                });

                if (selectedCombination && selectedCombination.products) {
                    const products = data.products.filter(product =>
                        selectedCombination.products.includes(product.title)
                    );

                    products.forEach(product => {
                        const productDOM = new DOMParser().parseFromString(
                            productTemplate,
                            "text/html"
                        );
                        const productImage = productDOM.querySelector("img");
                        productImage.setAttribute("src", product.image);
                        productImage.setAttribute("alt", product.imageAlt);

                        const productTitle = productDOM.querySelector(
                            ".cmp-product-cards__item-title"
                        );
                        productTitle.textContent = product.title;

                        const productDescription = productDOM.querySelector(
                            ".cmp-product-cards__item-description"
                        );
                        productDescription.textContent = product.description;

                        const productInterface = productDOM.querySelector(
                            ".cmp-product-cards__interface"
                        );
                        productInterface.textContent = product.interface;

                        const productFormFactor = productDOM.querySelector(
                            ".cmp-product-cards__form-factor"
                        );
                        productFormFactor.textContent = product.formFactor;

                        const productCapacity = productDOM.querySelector(
                            ".cmp-product-cards__capacity"
                        );
                        productCapacity.textContent = product.capacity;

                        const productConfigureBtn = productDOM.querySelector(
                            ".cmp-product-cards__configure-btn"
                        );
                        productConfigureBtn.setAttribute(
                            "href",
                            product.configureURL
                        );

                        const productDetailsBtn = productDOM.querySelector(
                            ".cmp-product-cards__details-btn"
                        );
                        productDetailsBtn.setAttribute(
                            "href",
                            product.detailsURL
                        );

                        productsContainer.appendChild(
                            productDOM.querySelector(".cmp-product-cards__item")
                        );
                    });
                }
            }
        }

        interfaceSelect.addEventListener('change', handleSelectChange);
        formFactorSelect.addEventListener('change', handleSelectChange);
        capacitySelect.addEventListener('change', handleSelectChange);

        function handleOpen(target, otherSelects) {
            const validOptions = data.filters
                .filter((combination) => {
                    return otherSelects.reduce((acc, curr) => {
                        if (!acc) return false;

                        return combination[curr.key] === (curr.element.value || curr.anyLabel);
                    }, true);
                })
                .map((combination) => combination[target.key]);

            target.optionsContainer.querySelectorAll('li').forEach((option) => {
                option.style.display = validOptions.includes(option.textContent) ? 'block' : 'none';
            });
        }

        interfaceSelect.addEventListener('open', () =>
            handleOpen(interfaceData, [formFactorData, capacityData])
        );
        formFactorSelect.addEventListener('open', () =>
            handleOpen(formFactorData, [interfaceData, capacityData])
        );
        capacitySelect.addEventListener('open', () =>
            handleOpen(capacityData, [interfaceData, formFactorData])
        );
    }
})();
