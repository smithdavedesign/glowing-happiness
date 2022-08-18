"use strict";
/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__carousel_js"],{

/***/ "./src/main/webpack/components/_carousel.js":
/*!**************************************************!*\
  !*** ./src/main/webpack/components/_carousel.js ***!
  \**************************************************/
/***/ (function(__unused_webpack_module, __webpack_exports__, __webpack_require__) {

eval("__webpack_require__.r(__webpack_exports__);\n/* harmony import */ var _splidejs_splide__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @splidejs/splide */ \"./node_modules/@splidejs/splide/dist/js/splide.esm.js\");\n\n\n(function () {\n    const carousels = document.querySelectorAll(\".cmp-carousel\");\n    carousels.forEach(carousel => {\n        const duration = 1500;\n\n        const slideWidth =\n            getComputedStyle(carousel).getPropertyValue(\n                \"--carousel-slide-width\"\n            ) || undefined;\n\n        new _splidejs_splide__WEBPACK_IMPORTED_MODULE_0__[\"default\"](carousel, {\n            type: \"loop\",\n            padding: \"12rem\",\n            focus: \"center\",\n            pauseOnHover: false,\n            pauseOnFocus: false,\n            updateOnMove: true,\n            arrows: false,\n            autoplay: carousel.getAttribute(\"data-autoplay\") === \"true\",\n            interval: duration,\n            speed: 950,\n            fixedWidth: slideWidth,\n            breakpoints: {\n                767: {\n                    padding: \"1.5rem\"\n                },\n                1023: {\n                    padding: \"3rem\",\n                    fixedWidth: undefined\n                }\n            }\n        }).mount();\n\n        carousel.classList.add(\"cmp-carousel--initialized\");\n        carousel.style.setProperty(\"--slide-duration\", `${duration}ms`);\n    });\n})();\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_carousel.js?");

/***/ })

}]);