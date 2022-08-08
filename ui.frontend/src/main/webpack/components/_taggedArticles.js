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

        async function getArticles(servletPath, page = 1) {
            const request = await fetch(`${servletPath}?page=${page}`);
            const response = await request.json();

            return response;
        }

        taggedArticles.forEach(async (cmp) => {
            const articlesContainer = cmp.querySelector('.cmp-tagged-articles__articles-container');
            const servletPath = cmp
                .querySelector('[data-servlet-path]')
                .getAttribute('data-servlet-path');
            let page = 1;

            const response = await getArticles(servletPath, page);
            response.items.forEach((item) => {
                const generatedHtml = generateTaggedArticleCard(item);
                articlesContainer.innerHTML += `${generatedHtml}`;
            });

            const loadMoreBtn = cmp.querySelector('.cmp-tagged-articles__load-more-btn');
            loadMoreBtn.addEventListener('click', async function () {
                page++;
                const response = await getArticles(servletPath, page);
                response.items.forEach((item) => {
                    const generatedHtml = generateTaggedArticleCard(item);
                    articlesContainer.innerHTML += `${generatedHtml}`;
                });
            });
        });
    }
});
