window.addEventListener('DOMContentLoaded', async function () {
    const productComparisonElements = document.querySelectorAll('.cmp-product-comparison');

    for (const productComparison of productComparisonElements) {
        const sectionTitle = productComparison.querySelector('.cmp-product-comparison__title');
        const isMobile = window.matchMedia('(max-width: 767px)');
        const template = productComparison
            .querySelector('template')
            .content.querySelector('.cmp-product-comparison__product');
        let products;
        const grid = productComparison.querySelector('.cmp-product-comparison__grid');
        const savedProducts = JSON.parse(window.localStorage.getItem('compare-products')) || [];

        function updateSectionTitle() {
            const sectionTitleTemplate = sectionTitle.getAttribute('data-section-title-template');
            const maxProducts = isMobile.matches ? 2 : 3;
            const comparingProducts = Math.min(products.length, maxProducts);

            sectionTitle.textContent = sectionTitleTemplate.replace('{0}', comparingProducts);
        }

        function createProductNode(product, variant) {
            const placeholderText = '---';
            const clone = template.cloneNode(true);
            const title = clone.querySelector('.cmp-product-comparison__product-title');
            const image = clone.querySelector('.cmp-product-comparison__product-image');
            const select = clone.querySelector('.cmp-custom-select');
            const selectInput = select.querySelector('.cmp-custom-select__input');
            const optionsContainer = clone.querySelector('.cmp-custom-select__options');
            const removeButton = clone.querySelector('.cmp-product-comparison__remove-btn');

            const dataNodes = clone.querySelectorAll('dd');

            title.textContent = product.title;
            image.setAttribute('src', product.image);
            image.setAttribute('alt', product.altText);

            dataNodes.forEach((dataNode, i) => {
                let text = variant.specsList?.[i] || placeholderText;
                text === '' ? placeholderText : text;

                dataNode.textContent = text;
            });

            optionsContainer.innerHTML = '';
            product.variants.forEach((opt) => {
                optionsContainer.innerHTML += `<li class='cmp-custom-select__option ${
                    opt.variationID === variant.variationID && 'cmp-custom-select__option--selected'
                }'>${opt.variationID}</li>`;
            });

            selectInput.addEventListener('change', function (e) {
                renderProductData(product.title, e.target.value);
            });

            select.querySelector('label').textContent += ' ' + product.title;
            const sluggedTitle = product.title.replace(/\s/g, '');
            selectInput.setAttribute('name', `${selectInput.getAttribute('name')}-${sluggedTitle}`);
            selectInput.setAttribute('id', `${selectInput.getAttribute('id')}-${sluggedTitle}`);

            removeButton.addEventListener('click', function () {
                products = products.filter((savedProduct) => savedProduct.title !== product.title);
                renderProductData();
                window.dispatchEvent(
                    new CustomEvent('removeSavedProduct', {
                        detail: { title: product.title },
                    })
                );
                updateSectionTitle();
            });
            return clone;
        }

        function renderProductData(updatedProductTitle, updatedVariationID) {
            grid.innerHTML = '';
            products.forEach((product) => {
                if (product.title === updatedProductTitle) {
                    grid.append(
                        createProductNode(
                            product,
                            product.variants.find(
                                (variant) => variant.variationID === updatedVariationID
                            )
                        )
                    );
                } else {
                    grid.append(createProductNode(product, product.variants[0]));
                }
            });
            window.dispatchEvent(new Event('initCustomSelects'));
        }

        async function getProducts(path) {
            const request = await fetch(`/bin/solidigm/productComparison?path=${path}`);
            const response = await request.json();

            return response;
        }

        products = await Promise.all(savedProducts.map((product) => getProducts(product.path)));
        updateSectionTitle();
        renderProductData();
    }
});
