window.addEventListener('DOMContentLoaded', async function () {
    async function fetchData() {
        // @TODO update endpoint once real url is defined
        const request = await fetch(`http://localhost:3004/all/`);
        const response = await request.json();

        return response;
    }
    const data = await fetchData();

    const supportSearchResults = document.querySelectorAll('.cmp-support-search-results');
    for (const supportSearchResult of supportSearchResults) {
        const RESULTS_PER_PAGE = 10;

        const resultsContainer = supportSearchResult.querySelector('.cmp-quick-links__container');
        const firstPageBtn = supportSearchResult.querySelector('.cmp-search-results__first-page');

        const lastPageBtn = supportSearchResult.querySelector('.cmp-search-results__last-page');

        const prevPageBtn = supportSearchResult.querySelector('.cmp-search-results__previous-page');

        const nextPageBtn = supportSearchResult.querySelector('.cmp-search-results__next-page');

        const filtersInput = supportSearchResult.querySelector('#support-search-results-filter');

        const viewArticleLabel = supportSearchResult.getAttribute('data-view-article-label');

        let currentPage =
            Number.parseInt(new URLSearchParams(window.location.search).get('page')) || 1;
        let totalPages = null;
        let selectedFilter = new URLSearchParams(window.location.search).get('filter');

        async function fetchResults(page = 1, filter) {
            let totalItems = data.totalItems;
            let items = data.items;
            if (filter) {
                items = data.items.filter((item) => item.models.includes(filter));
                totalItems = items.length;
            }

            const selectedResults = items.slice(
                (page - 1) * RESULTS_PER_PAGE,
                (page - 1) * RESULTS_PER_PAGE + RESULTS_PER_PAGE
            );

            return {
                totalItems,
                items: selectedResults,
            };
        }

        function resultToHtml(result) {
            return `
             <li>
                <a href="${result.url}" class="cmp-quick-links__link cmp-quick-links__link--external" data-label="${viewArticleLabel} ${result.title}">
                    <div class="cmp-quick-links__content">
                        <span class="cmp-quick-links__link-title">${result.title}</span>
                    </div>
                </a>
            </li>
            `;
        }

        function insertAfter(newNode, referenceNode) {
            referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
        }

        function generatePagination(currentPage, totalPages) {
            if (currentPage === 1 || totalPages === 0) {
                firstPageBtn.setAttribute('disabled', '');
                prevPageBtn.setAttribute('disabled', '');
            } else {
                firstPageBtn.removeAttribute('disabled');
                prevPageBtn.removeAttribute('disabled');
            }

            if (currentPage === totalPages || totalPages === 0) {
                lastPageBtn.setAttribute('disabled', '');
                nextPageBtn.setAttribute('disabled', '');
            } else {
                lastPageBtn.removeAttribute('disabled');
                nextPageBtn.removeAttribute('disabled');
            }

            const offset = 2;
            const pages = [currentPage];
            for (let i = currentPage - 1; i > 0 && i > currentPage - offset - 1; i--) {
                pages.unshift(i);
            }

            for (let i = currentPage + 1; i < totalPages + 1 && i < currentPage + offset + 1; i++) {
                pages.push(i);
            }

            const pagesButtons = supportSearchResult.querySelectorAll('.cmp-search-results__page');
            pagesButtons.forEach((pageBtn) => pageBtn.remove());
            pages.reverse().forEach((page) => {
                const liContainer = document.createElement('li');
                liContainer.classList.add('cmp-search-results__page');

                const button = document.createElement('button');
                button.innerText = page;
                if (page === currentPage) {
                    button.setAttribute('aria-current', 'true');
                }

                button.addEventListener('click', function () {
                    navigateToPage(page);
                });

                liContainer.appendChild(button);
                insertAfter(liContainer, prevPageBtn.parentElement);
            });
        }

        async function preparePage(page) {
            const results = await fetchResults(page, selectedFilter);
            totalPages = Math.ceil(results.totalItems / RESULTS_PER_PAGE);
            generatePagination(currentPage, totalPages);

            resultsContainer.innerHTML = '';
            for (const result of results.items) {
                resultsContainer.innerHTML += resultToHtml(result);
            }
        }

        async function navigateToPage(page) {
            var url = new URL(window.location);
            var search_params = url.searchParams;
            search_params.set('page', page);
            if (selectedFilter) {
                search_params.set('filter', selectedFilter);
            } else {
                search_params.delete('filter');
            }
            url.search = search_params.toString();
            const new_url = url.toString();
            window.history.pushState(null, '', new_url);

            preparePage(page);
            currentPage = page;
        }

        await preparePage(currentPage);

        prevPageBtn.addEventListener('click', () => navigateToPage(currentPage - 1));

        nextPageBtn.addEventListener('click', () => navigateToPage(currentPage + 1));

        firstPageBtn.addEventListener('click', () => navigateToPage(1));
        lastPageBtn.addEventListener('click', () => navigateToPage(totalPages));

        filtersInput.addEventListener('change', function (e) {
            selectedFilter = e.target.value;
            navigateToPage(1);
        });
    }
});
