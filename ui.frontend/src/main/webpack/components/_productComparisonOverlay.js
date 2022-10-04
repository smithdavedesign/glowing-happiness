window.addEventListener('DOMContentLoaded', function () {
    const MAX_PRODUCTS_DESKTOP = 3;
    const MAX_PRODUCTS_MOBILE = 2;
    const savedCompareProductsKey = 'compare-products';

    // Updates the product comparison overlay links to point to the comparison page
    // by modifying the productPagePath.
    function updateOverlayURLS(product) {
        const { productPagePath } = product;
        if (productPagePath) {
            const updatedPath = productPagePath.split('/products/')[0] + '/products/compare.html';

            const compareOverlayLinks = document.querySelectorAll(
                ".cmp-product-comparison-overlay a[href='#']"
            );
            compareOverlayLinks.forEach((link) => {
                link.setAttribute('href', updatedPath);
            });
        }
    }

    function getSavedProducts() {
        return JSON.parse(window.localStorage.getItem(savedCompareProductsKey)) || [];
    }

    const isMobile = window.matchMedia('(max-width: 767px)');

    const productOverlay = document.querySelector('.cmp-product-comparison-overlay');
    const products = getSavedProducts();
    products.length > 0 && updateOverlayURLS(products[0]);
    const compactDataContainer = productOverlay.querySelector(
        '.cmp-product-comparison-overlay__data--compact'
    );
    const expandedContainer = productOverlay.querySelector(
        '.cmp-product-comparison-overlay__data--expanded'
    );

    const interfaceLabel = productOverlay.getAttribute('data-interface-label');
    const formFactorLabel = productOverlay.getAttribute('data-form-factor-label');
    const capacityLabel = productOverlay.getAttribute('data-capacity-label');
    const removeLabel = productOverlay.getAttribute('data-remove-label');
    const selectedProductsLabel = productOverlay.getAttribute('data-selected-products-label');

    if (!products.length) {
        productOverlay.style.display = 'none';
    } else {
        renderOverlay(products);
    }

    async function addProduct(product) {
        updateOverlayURLS(product);
        const maxProducts = isMobile.matches ? MAX_PRODUCTS_MOBILE : MAX_PRODUCTS_DESKTOP;

        const savedProducts = getSavedProducts();
        if (savedProducts.length < maxProducts) {
            const request = await fetch(
                `/bin/solidigm/productComparisonOverlay?productPagePath=${product.productPagePath}&variation=${product.variation}`
            );
            const productDetails = await request.json();
            const productToSave = {
                title: productDetails.name,
                image: productDetails.image,
                altText: productDetails.altText,
                interface: productDetails.productInterface,
                formFactor: productDetails.formFactor,
                capacity: productDetails.capacity,
                ...product,
            };

            const products = getSavedProducts();
            products.push(productToSave);
            window.localStorage.setItem(savedCompareProductsKey, JSON.stringify(products));
            productOverlay.dispatchEvent(new Event('change'));
        }
    }

    function removeProduct(title) {
        const products = getSavedProducts();
        window.localStorage.setItem(
            savedCompareProductsKey,
            JSON.stringify(products.filter((product) => product.title !== title))
        );
        productOverlay.dispatchEvent(new Event('change'));
    }

    function removeAllProducts() {
        window.localStorage.removeItem(savedCompareProductsKey);
        productOverlay.dispatchEvent(new Event('change'));
    }

    function renderOverlay(products) {
        const mostRecentlyAddedProduct = products[products.length - 1];
        const compactTitle = compactDataContainer.querySelector(
            '.cmp-product-comparison-overlay__compact-title'
        );
        const compactImage = compactDataContainer.querySelector('img');

        compactTitle.textContent = selectedProductsLabel.replace(/\{0\}/g, products.length);
        compactImage.setAttribute('src', mostRecentlyAddedProduct.image);
        compactImage.setAttribute('alt', mostRecentlyAddedProduct.altText);

        const expandedTitle = expandedContainer.querySelector(
            '.cmp-product-comparison-overlay__expanded-title'
        );
        expandedTitle.textContent = selectedProductsLabel.replace(/\{0\}/g, products.length);
        const expandedProductGrid = expandedContainer.querySelector(
            '.cmp-product-comparison-overlay__expanded-grid'
        );

        expandedContainer
            .querySelectorAll('.cmp-product-comparison-overlay__product')
            .forEach((el) => el.remove());

        products.forEach((product) => {
            const productContainer = document.createElement('div');
            productContainer.classList.add('cmp-product-comparison-overlay__product');
            productContainer.innerHTML = `
                <div>
                    <h3 class="cmp-product-comparison-overlay__product-title">${product.title}</h3>
                    <img class="cmp-product-comparison-overlay__product-image" src="${product.image}" alt="${product.altText}"/>
                    <button class="cmp-product-comparison-overlay__product-remove">${removeLabel}</button>
                </div>
                <dl class="cmp-product-comparison-overlay__product-details">
                    <dt>${interfaceLabel}</dt>
                    <dd>${product.interface}</dd>
                    <dt>${formFactorLabel}</dt>
                    <dd>${product.formFactor}</dd>
                    <dt>${capacityLabel}</dt>
                    <dd>${product.capacity}</dd>
                </dl>
            `;

            expandedProductGrid.insertBefore(productContainer, expandedProductGrid.firstChild);
            productContainer
                .querySelector('.cmp-product-comparison-overlay__product-remove')
                .addEventListener('click', () => removeProduct(product.title));
        });
    }

    productOverlay.addEventListener('change', function () {
        const products = getSavedProducts();
        if (products?.length) {
            productOverlay.style.display = '';
            const maxProducts = isMobile.matches ? MAX_PRODUCTS_MOBILE : MAX_PRODUCTS_DESKTOP;

            renderOverlay(products);
            if (products.length >= maxProducts) {
                window.dispatchEvent(new Event('max-products'));
            }
        } else {
            productOverlay.style.display = 'none';
        }
    });

    const overlayContainer = document.querySelector('.cmp-product-comparison-overlay');
    const detailsBtns = document.querySelectorAll('[data-details-btn]');
    const closeBtn = document.querySelector('.cmp-product-comparison-overlay__btn-close');

    const compactData = document.querySelector('.cmp-product-comparison-overlay__data--compact');
    const expandedData = document.querySelector('.cmp-product-comparison-overlay__data--expanded');

    detailsBtns.forEach((btn) => {
        btn.addEventListener('click', function () {
            compactData.style.display = 'none';
            expandedData.style.display = 'flex';
            overlayContainer.classList.add('cmp-product-comparison-overlay--expanded');
        });
    });

    closeBtn.addEventListener('click', function () {
        compactData.style.display = '';
        expandedData.style.display = 'none';
        overlayContainer.classList.remove('cmp-product-comparison-overlay--expanded');
    });

    const removeAllBtn = document.querySelector(
        '.cmp-product-comparison-overlay__controls-remove-all'
    );
    removeAllBtn.addEventListener('click', removeAllProducts);

    window.addEventListener('removeSavedProduct', function (e) {
        removeProduct(e.detail.title);
    });

    window.addEventListener('addProductToCompare', function (e) {
        addProduct({ productPagePath: e.detail.path, variation: e.detail.variation });
    });
});
