/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__productGallery_js"],{

/***/ "./src/main/webpack/components/_productGallery.js":
/*!********************************************************!*\
  !*** ./src/main/webpack/components/_productGallery.js ***!
  \********************************************************/
/***/ (function() {

eval("(() => {\n    let showMoreIsClicked = false;\n\n    $(window).resize(function () {\n        if (showMoreIsClicked) return;\n\n        calculateThumbnails();\n    });\n\n    calculateThumbnails();\n\n    $(\".product-gallery__thumbnails__image\").click(function (event) {\n        const thumbnailSrc = $(this).attr(\"src\");\n        const thumbnailAlt = $(this).attr(\"alt\");\n\n        $(\".product-gallery__product-image\").attr(\"src\", thumbnailSrc);\n        $(\".product-gallery__product-image\").attr(\"alt\", thumbnailAlt);\n\n        $(\".product-gallery__thumbnails__image\").removeClass(\n            \"product-gallery__thumbnails__image--selected\"\n        );\n        $(this).addClass(\"product-gallery__thumbnails__image--selected\");\n    });\n\n    $(\".product-gallery__thumbnails__show-more-tile\").click(function (event) {\n        $(\".product-gallery__thumbnails__image-wrapper\").show();\n        $(\".product-gallery__thumbnails__show-more-tile\").hide();\n\n        showMoreIsClicked = true;\n    });\n\n    function calculateThumbnails() {\n        // Reset values to account for resize updates.\n        $(\".product-gallery__thumbnails__image-wrapper\").show();\n        $(\".product-gallery__thumbnails__show-more-tile\").hide();\n\n        const isTablet = $(document).width() >= 768;\n        const maxTotalThumbnails = isTablet ? 7 : 5;\n\n        const firstThumbnail = $(\".product-gallery__thumbnails__image\")[0];\n        $(firstThumbnail).addClass(\n            \"product-gallery__thumbnails__image--selected\"\n        );\n\n        const totalThumbnails = $(\n            \".product-gallery__thumbnails__image-wrapper\"\n        ).length;\n        const showMoreTextElement = $(\n            \".product-gallery__thumbnails__show-more-tile__text\"\n        );\n\n        showMoreTextElement.text(\n            `+${totalThumbnails - (maxTotalThumbnails - 1)}`\n        );\n\n        if (totalThumbnails >= maxTotalThumbnails) {\n            $(\".product-gallery__thumbnails__image-wrapper\").each(function (\n                index,\n                element\n            ) {\n                if ($(element).data(\"index\") > maxTotalThumbnails) {\n                    $(element).hide();\n                }\n\n                if (\n                    totalThumbnails > maxTotalThumbnails &&\n                    $(element).data(\"index\") === maxTotalThumbnails\n                ) {\n                    const showMoreElement = $(element).children(\n                        \".product-gallery__thumbnails__show-more-tile\"\n                    );\n\n                    showMoreElement.css(\"display\", \"flex\");\n                }\n            });\n        }\n    }\n})();\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_productGallery.js?");

/***/ })

}]);