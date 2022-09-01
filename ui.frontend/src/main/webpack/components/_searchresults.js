window.addEventListener('DOMContentLoaded', async function () {
    const searchResults = document.querySelectorAll('.cmp-search-results');
    for (const searchResult of searchResults) {
        const RESULTS_PER_PAGE = Number.parseInt(searchResult.querySelector("[data-results-per-page]")?.getAttribute("data-results-per-page") ) || 10;

        const resultsContainer = searchResult.querySelector(
            '.cmp-search-results__results-container'
        );
        const totalResultsContainer = searchResult.querySelector('.cmp-search-results__total p');
        const firstPageBtn = searchResult.querySelector('.cmp-search-results__first-page');
        const lastPageBtn = searchResult.querySelector('.cmp-search-results__last-page');
        const prevPageBtn = searchResult.querySelector('.cmp-search-results__previous-page');
        const nextPageBtn = searchResult.querySelector('.cmp-search-results__next-page');
        let currentPage =
            Number.parseInt(new URLSearchParams(window.location.search).get('page')) || 1;
        let totalPages = null;

        // @TODO: update this function once the endpoint is ready
        async function fetchResults(page = 1, filter) {
            return new Promise((resolve, reject) => {
                setTimeout(() => {
                    resolve({
                        totalItems: 102,
                        items: Array.from(new Array(page === 11 ? 2 : 10)).map((_) => {
                            return {
                                title: `This is a result that does not have an image or could be a press release ${page}`,
                                url: 'https://wwww.example.com',
                                description:
                                    'Meta page description: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris auctor diam non ipsum gravida, nec.',
                                tags: [
                                    {
                                        text: 'Product Series',
                                        url: 'https://tag1.com',
                                    },
                                    {
                                        text: 'Product Detail',
                                        url: 'https://tag2.com',
                                    },
                                ],
                                image: {
                                    url: 'https://picsum.photos/500/500',
                                    altText: 'Some Alt text',
                                },
                            };
                        }),
                    });
                }, 1);
            });
        }

        function resultToHtml(result) {
            const tagsHtml = result.tags
                .map(
                    (tag) => `
                    <li>
                        <a href="${tag.url}">${tag.text}</a>
                    </li>
                `
                )
                .join('');

            return `
            <li class="cmp-search-results__result">
            <div class="cmp-search-results__result-img">
                <img src="${result.image.url}" alt="${result.image.altText}"/>
            </div>
            <div>
                <h2 class="cmp-search-results__result-title">${result.title}</h2>
                <a href="${result.url}">${result.url}</a>
                <p class="cmp-search-results__result-description">
                    ${result.description}
                </p>
                <div class="cmp-search-results__tags">
                    <span>Tags:</span>
                    <ul>
                        ${tagsHtml}
                    </ul>
                </div>
            </div>
        </li>
            `;
        }

        function updateTotalIndicator(resultsInCurrentPage, totalResults) {
            const template = totalResultsContainer.getAttribute('data-results-template');

            totalResultsContainer.innerHTML = template
                .replace('${resultsPerPage}', resultsInCurrentPage)
                .replace('${totalResults}', totalResults);
        }

        function insertAfter(newNode, referenceNode) {
            referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
        }

        function generatePagination(currentPage, totalPages) {
            if (currentPage === 1) {
                firstPageBtn.setAttribute('disabled', '');
                prevPageBtn.setAttribute('disabled', '');
            } else {
                firstPageBtn.removeAttribute('disabled');
                prevPageBtn.removeAttribute('disabled');
            }

            if (currentPage === totalPages) {
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

            const pagesButtons = searchResult.querySelectorAll('.cmp-search-results__page');
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
            const results = await fetchResults(page);
            totalPages = Math.ceil(results.totalItems / RESULTS_PER_PAGE);
            updateTotalIndicator(results.items.length, results.totalItems);
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
    }
});
