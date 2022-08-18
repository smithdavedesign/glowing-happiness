/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__productconfigurator_js"],{

/***/ "./src/main/webpack/components/_productconfigurator.js":
/*!*************************************************************!*\
  !*** ./src/main/webpack/components/_productconfigurator.js ***!
  \*************************************************************/
/***/ (function() {

eval("window.addEventListener('DOMContentLoaded', () => {\n    function changeTabs(e) {\n        const target = e.target;\n        const parent = target.parentNode;\n        const container = parent.parentNode.parentNode;\n\n        // Remove all current selected tabs\n        parent\n            .querySelectorAll('[aria-selected=\"true\"]')\n            .forEach((t) => t.setAttribute('aria-selected', false));\n\n        // Set this tab as selected\n        target.setAttribute('aria-selected', true);\n\n        // Hide all tab panels\n        container.querySelectorAll('[role=\"tabpanel\"]').forEach((p) => (p.style.display = 'none'));\n\n        // Show the selected panel\n        container.parentNode.querySelector(\n            `#${target.getAttribute('aria-controls')}`\n        ).style.display = 'flex';\n    }\n\n    const tabs = document.querySelectorAll('[role=\"tab\"]');\n\n    // Add a click event handler to each tab\n    tabs.forEach((tab) => {\n        tab.addEventListener('click', changeTabs);\n    });\n});\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_productconfigurator.js?");

/***/ })

}]);