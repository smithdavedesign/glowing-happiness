/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__partnersList_js"],{

/***/ "./src/main/webpack/components/_partnersList.js":
/*!******************************************************!*\
  !*** ./src/main/webpack/components/_partnersList.js ***!
  \******************************************************/
/***/ (function() {

eval("(function () {\n    $(\".partners-list .solidigm-select\").on(\"change\", onFilterChange);\n\n    let productSelector = \"\", countrySelector = \"\";\n    function onFilterChange() {\n        if(this.name === 'countries'){\n            countrySelector = this.value === '' ? '' : \"[data-partner-country='\" + this.value + \"']\" \n        } else if(this.name === 'products'){\n            productSelector = this.value === '' ? '' :  \"[data-partner-product*='\" + this.value + \"']\" \n        }\n\n        $(\".partners-list__partners__table-row\").hide();\n\n        const visibleRows = $(\".partners-list__partners__table-row\" + countrySelector + productSelector);\n        visibleRows.show();\n\n        if (visibleRows.length === 0) {\n            $(\".partners-list__no-results\").show();\n        } else {\n            $(\".partners-list__no-results\").hide();\n        }\n    }\n})();\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_partnersList.js?");

/***/ })

}]);