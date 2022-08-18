/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__taggedArticles_js"],{

/***/ "./src/main/webpack/components/_taggedArticles.js":
/*!********************************************************!*\
  !*** ./src/main/webpack/components/_taggedArticles.js ***!
  \********************************************************/
/***/ (function() {

eval("window.addEventListener('DOMContentLoaded', function () {\n    const taggedArticles = document.querySelectorAll('.cmp-tagged-articles');\n\n    if (taggedArticles) {\n        function generateTaggedArticleCard({\n            teaserImage,\n            articleTitle,\n            articleLink,\n            categoryTag,\n            altText,\n        }) {\n            return `<div class=\"cmp-article-list__article\">\n                        <div class=\"cmp-article-list__article-image media-with-corner\">\n                            <img src=\"${teaserImage}\" alt=\"${altText}\" height=\"225\" width=\"100%\">\n                        </div>\n                        <div class=\"cmp-article-list__article-content\">\n                            <span class=\"cmp-article-list__article-category\">${categoryTag}</span>\n                            <h3 class=\"cmp-article-list__article-title\">\n                                <a href=\"${articleLink}\">\n                                    ${articleTitle}\n                                </a>\n                            </h3>\n                        </div>\n                    </div>`;\n        }\n\n        async function getArticles(servletPath, page = 1) {\n            const request = await fetch(`${servletPath}?page=${page}`);\n            const response = await request.json();\n\n            return response;\n        }\n\n        taggedArticles.forEach(async (cmp) => {\n            const articlesContainer = cmp.querySelector('.cmp-tagged-articles__articles-container');\n            const servletPath = cmp\n                .querySelector('[data-servlet-path]')\n                .getAttribute('data-servlet-path');\n            let page = 1;\n\n            const response = await getArticles(servletPath, page);\n            response.items.forEach((item) => {\n                const generatedHtml = generateTaggedArticleCard(item);\n                articlesContainer.innerHTML += `${generatedHtml}`;\n            });\n\n            const loadMoreBtn = cmp.querySelector('.cmp-tagged-articles__load-more-btn');\n            loadMoreBtn.addEventListener('click', async function () {\n                page++;\n                const response = await getArticles(servletPath, page);\n                response.items.forEach((item) => {\n                    const generatedHtml = generateTaggedArticleCard(item);\n                    articlesContainer.innerHTML += `${generatedHtml}`;\n                });\n            });\n        });\n    }\n});\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_taggedArticles.js?");

/***/ })

}]);