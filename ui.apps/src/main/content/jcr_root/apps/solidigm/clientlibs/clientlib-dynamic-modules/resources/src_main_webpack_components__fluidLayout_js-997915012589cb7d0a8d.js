/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__fluidLayout_js"],{

/***/ "./src/main/webpack/components/_fluidLayout.js":
/*!*****************************************************!*\
  !*** ./src/main/webpack/components/_fluidLayout.js ***!
  \*****************************************************/
/***/ (function() {

eval("const DESKTOP_WIDTH = 1024;\nconst MAX_WIDTH = Number.parseInt(getComputedStyle(document.body).getPropertyValue(\"--max-width\").replace(\"px\", \"\"));\n\nwindow.addEventListener(\"resize\", handleResize);\n\nhandleResize();\n\n// Custom css property to handle mobile browser bars.\nconst vh = innerHeight * 0.01;\ndocument.documentElement.style.setProperty(\"--vh\", `${vh}px`);\n\nfunction handleResize() {\n    const innerWidth =\n        window.innerWidth >= MAX_WIDTH ? MAX_WIDTH : window.innerWidth;\n    const innerHeight = window.innerHeight;\n\n    // Only update vh on desktop, to prevent content shifting on mobile due to browser bars.\n    if (innerHeight >= DESKTOP_WIDTH) {\n        const vh = innerHeight * 0.01;\n        document.documentElement.style.setProperty(\"--vh\", `${vh}px`);\n    }\n\n    const vw = innerWidth * 0.01;\n    document.documentElement.style.setProperty(\"--vw\", `${vw}px`);\n}\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_fluidLayout.js?");

/***/ })

}]);