/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__productComparisonOverlay_js"],{

/***/ "./src/main/webpack/components/_productComparisonOverlay.js":
/*!******************************************************************!*\
  !*** ./src/main/webpack/components/_productComparisonOverlay.js ***!
  \******************************************************************/
/***/ (function() {

eval("window.addEventListener(\"DOMContentLoaded\", function(){\n    const overlayContainer = document.querySelector(\".cmp-product-comparison-overlay\");\n    const detailsBtns = document.querySelectorAll(\"[data-details-btn]\");\n    const closeBtn = document.querySelector(\".cmp-product-comparison-overlay__btn-close\");\n\n    const compactData = document.querySelector(\".cmp-product-comparison-overlay__data--compact\");\n    const expandedData = document.querySelector(\".cmp-product-comparison-overlay__data--expanded\");\n\n    detailsBtns.forEach(btn => {\n        btn.addEventListener(\"click\", function(){\n            compactData.style.display = 'none';\n            expandedData.style.display = 'flex';\n            overlayContainer.classList.add(\"cmp-product-comparison-overlay--expanded\")\n        });\n    });\n\n    closeBtn.addEventListener(\"click\", function(){\n        compactData.style.display = '';\n        expandedData.style.display = 'none';\n        overlayContainer.classList.remove(\"cmp-product-comparison-overlay--expanded\")\n    })\n});\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_productComparisonOverlay.js?");

/***/ })

}]);