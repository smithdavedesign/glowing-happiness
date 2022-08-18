/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__bioList_js"],{

/***/ "./src/main/webpack/components/_bioList.js":
/*!*************************************************!*\
  !*** ./src/main/webpack/components/_bioList.js ***!
  \*************************************************/
/***/ (function() {

eval("$(\".cmp-bio-list__profile-link\").click(function () {\n    const profileId = $(this).data(\"profile-id\");\n\n    $(\n        `.cmp-bio-list__profile__modal-wrapper[data-profile-id=\"${profileId}\"]`\n    ).css(\"display\", \"flex\");\n    $(\"body\").css(\"overflow\", \"hidden\");\n});\n\n$(\n    \".cmp-bio-list__profile__modal__close, .cmp-bio-list__profile__modal-wrapper\"\n).click(function (event) {\n    const target = event.target;\n    const targetClassName = target.className;\n\n    if (\n        targetClassName === \"cmp-bio-list__profile__modal__close\" ||\n        targetClassName === \"cmp-bio-list__profile__modal-wrapper\" ||\n        $(target).is(\"svg\") ||\n        $(target).is(\"path\")\n    ) {\n        closeModal();\n    }\n});\n\n$(document).keyup(function (event) {\n    if (event.key === \"Escape\") {\n        closeModal();\n    }\n});\n\nfunction closeModal() {\n    $(\".cmp-bio-list__profile__modal-wrapper\").hide();\n    $(\"body\").css(\"overflow\", \"auto\");\n}\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_bioList.js?");

/***/ })

}]);