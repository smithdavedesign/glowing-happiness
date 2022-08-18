/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkaem_maven_archetype"] = self["webpackChunkaem_maven_archetype"] || []).push([["src_main_webpack_components__modal_js"],{

/***/ "./src/main/webpack/components/_modal.js":
/*!***********************************************!*\
  !*** ./src/main/webpack/components/_modal.js ***!
  \***********************************************/
/***/ (function() {

eval("window.addEventListener('DOMContentLoaded', function () {\n    document.querySelectorAll('[data-modal-target]').forEach((btn) => {\n        btn.addEventListener('click', function () {\n            const modal = document.getElementById(btn.getAttribute('data-modal-target'));\n            const closeBtn = modal.querySelector('.cmp-modal__close-btn');\n            const iframe = modal.querySelector('iframe');\n\n            if (!iframe.getAttribute('src')) {\n                iframe.setAttribute('src', iframe.getAttribute('data-src'));\n            }\n\n            modal.showModal();\n            document.body.overflow = 'hidden';\n            closeBtn.focus();\n\n            function closeModal() {\n                if (iframe) {\n                    // Resets iframe\n                    const currentSrc = iframe.getAttribute('src');\n                    iframe.setAttribute('data-src', currentSrc);\n                    iframe.setAttribute('src', '');\n                }\n                modal.close();\n                document.body.overflow = 'visible';\n            }\n\n            // Closes modal when clicked outside\n            function onClick(event) {\n                if (event.target === modal) {\n                    modal.close();\n                }\n            }\n\n            modal.addEventListener('click', onClick, { once: true });\n            modal.addEventListener('close', closeModal, { once: true });\n            closeBtn.addEventListener('click', onClick, { once: true });\n        });\n    });\n});\n\n\n//# sourceURL=webpack://aem-maven-archetype/./src/main/webpack/components/_modal.js?");

/***/ })

}]);