/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__footer_js"],{

/***/ "./src/main/webpack/components/_footer.js":
/*!************************************************!*\
  !*** ./src/main/webpack/components/_footer.js ***!
  \************************************************/
/***/ (function() {

eval("(function () {\n    let didScroll = false;\n    const navbarHeight = $(\".header__navigation-bar\").outerHeight();\n\n    $(window).scroll(function () {\n        didScroll = true;\n    });\n\n    setInterval(function () {\n        if (didScroll) {\n            hasScrolled();\n\n            didScroll = false;\n        }\n    }, 250);\n\n    function hasScrolled() {\n        const scrollTop = $(this).scrollTop();\n\n\n\n        if (scrollTop > navbarHeight) {\n            if ($(\".scroll-up-button\").css(\"display\") !== \"none\") return;\n\n            $(\".scroll-up-button\").css(\"display\", \"flex\").hide().fadeIn();\n        } else {\n            $(\".scroll-up-button\").fadeOut();\n        }\n    }\n\n    $(\".scroll-up-button\").click(function () {\n        $(\"html\").animate({ scrollTop: 0 }, 500);\n    });\n})();\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_footer.js?");

/***/ })

}]);