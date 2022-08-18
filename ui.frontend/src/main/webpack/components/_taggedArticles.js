window.addEventListener('DOMContentLoaded', function () {
    const taggedArticles = document.querySelectorAll('.cmp-tagged-articles');
    if (taggedArticles) {
        function generateTaggedArticleCard({
            teaserImage,
            articleTitle,
            articleLink,
            categoryTag,
            altText,
        }) {
            return `<div class="cmp-article-list__article">
                        <div class="cmp-article-list__article-image media-with-corner">
                            <img src="${teaserImage}" alt="${altText}" height="225" width="100%">
                        </div>
                        <div class="cmp-article-list__article-content">
                            <span class="cmp-article-list__article-category">${categoryTag}</span>
                            <h3 class="cmp-article-list__article-title">
                                <a href="${articleLink}">
                                    ${articleTitle}
                                </a>
                            </h3>
                        </div>
                    </div>`;
        }

        async function getArticles(servletPath, page = 1, selectorTag = '') {
            const isDesktop = matchMedia('(min-width: 1024px)').matches;
            const perPage = isDesktop ? 9 : 6;
            const request = await fetch(
                `/bin/solidigm/taggedArticles?rootPath=${servletPath}&resultsPerPage=${perPage}&page=${page}&tag=${selectorTag}`
            );
            const response = await request.json();

            return response;
        }

        let page = 1;
        async function getCurrentPage(servletPath, articlesContainer, selectorTag, loadMoreBtn) {
            loadMoreBtn.setAttribute('disabled', 'true');
            const response = await getArticles(servletPath, page, selectorTag);
            page++;
            response.results.forEach((item) => {
                const generatedHtml = generateTaggedArticleCard(item);
                articlesContainer.innerHTML += generatedHtml;
            });
            loadMoreBtn.removeAttribute('disabled');

            if (page > response.totalPages) {
                loadMoreBtn.style.display = 'none';
            }
        }

        taggedArticles.forEach(async (cmp) => {
            let loadMoreBtn = cmp.querySelector('.cmp-tagged-articles__load-more-btn');
            const articlesContainer = cmp.querySelector('.cmp-tagged-articles__articles-container');
            const servletPath = cmp
                .querySelector('[data-servlet-path]')
                .getAttribute('data-servlet-path');
            const selectorTag =
                cmp.querySelector('[data-selector-tag]')?.getAttribute('data-selector-tag') || '';

            const select = cmp.querySelector('.cmp-tagged-articles__select');
            select?.addEventListener('change', function (e) {
                if (e.target.value) {
                    initArticles(e.target.value);
                }
            });

            async function initArticles(selectorTag) {
                async function getPage() {
                    await getCurrentPage(servletPath, articlesContainer, selectorTag, loadMoreBtn);
                }

                loadMoreBtn.replaceWith(loadMoreBtn.cloneNode(true));
                loadMoreBtn = cmp.querySelector('.cmp-tagged-articles__load-more-btn');

                loadMoreBtn.style.display = 'block';
                articlesContainer.innerHTML = '';
                page = 1;

                await getCurrentPage(servletPath, articlesContainer, selectorTag, loadMoreBtn);
                loadMoreBtn.addEventListener('click', getPage, true);
            }
            initArticles(selectorTag);
        });
    }
});
