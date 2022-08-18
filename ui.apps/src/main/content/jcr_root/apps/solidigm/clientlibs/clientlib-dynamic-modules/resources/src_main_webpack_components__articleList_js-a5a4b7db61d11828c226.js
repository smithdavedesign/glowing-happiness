"use strict";
/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__articleList_js"],{

/***/ "./src/main/webpack/components/_articleList.js":
/*!*****************************************************!*\
  !*** ./src/main/webpack/components/_articleList.js ***!
  \*****************************************************/
/***/ (function(__unused_webpack_module, __webpack_exports__, __webpack_require__) {

eval("__webpack_require__.r(__webpack_exports__);\n/* harmony import */ var _splidejs_splide__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @splidejs/splide */ \"./node_modules/@splidejs/splide/dist/js/splide.esm.js\");\n\n\nwindow.addEventListener(\"DOMContentLoaded\", function(){\n    const carousels = document.querySelectorAll(\".cmp-article-list .splide\");\n    \n    carousels.forEach(carousel => {\n        const duration = 1500;\n    \n        new _splidejs_splide__WEBPACK_IMPORTED_MODULE_0__[\"default\"](carousel, {\n            type: \"slide\",\n            padding: \"0\",\n            gap: \"32px\",\n            focus: \"center\",\n            updateOnMove: true,\n            arrows: false,\n            interval: duration,\n            speed: 950,\n            perPage: 3,\n            perMove: 3,\n            drag: false,\n            isNavigation: true,\n            breakpoints: {\n                1023: {\n                    perPage: 2,\n                    gap: \"20px\",\n                    padding: \"20px\",\n                    isNavigation: false,\n                    drag: true\n                },\n                767: {\n                    perPage: 1,\n                }\n            }\n        }).mount();\n    \n        carousel.style.setProperty(\"--slide-duration\", `${duration}ms`);\n    });\n})\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_articleList.js?");

/***/ })

}]);