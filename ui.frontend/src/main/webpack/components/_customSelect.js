/**
 * Custom Select form control.
 *
 * To take into consideration accessibility the starting point for this code can be
 * found here: https://24ways.org/2019/making-a-better-custom-select-element/
 */

window.addEventListener('DOMContentLoaded', function () {
    const SELECT_STATES = {
        INITIAL: 'initial',
        OPENED: 'opened',
        CLOSED: 'closed',
    };

    const SELECT_DROPDOWN_STATES = {
        OPENED: 'opened',
        CLOSED: 'closed',
    };

    const MOVE_DIRECTIONS = {
        FORWARD: 'forward',
        BACK: 'back',
    };

    const csSelector = '.cmp-custom-select';
    const csActiveOptionClass = 'cmp-custom-select__option--selected';
    const expandedClass = 'cmp-custom-select__options--visible';
    const customSelects = document.querySelectorAll(csSelector);
    customSelects.forEach((csElement) => {
        const csInput = csElement.querySelector('input');
        const csList = csElement.querySelector('ul');
        const csOptions = csList.querySelectorAll('li');
        const csStatus = document.querySelector('.cmp-custom-select__status');
        const optionsList = Array.from(csOptions);

        let csState = SELECT_STATES.INITIAL;
        // inform assistive tech (screen readers) of the names & roles of the elements in our group
        csElement.setAttribute('role', 'combobox');
        csElement.setAttribute('aria-haspopup', 'listbox');
        csElement.setAttribute('aria-owns', 'custom-select-list'); // container owns the list...
        csInput.setAttribute('aria-autocomplete', 'both');
        csInput.setAttribute('aria-controls', 'custom-select-list'); // ...but the input controls it
        csList.setAttribute('role', 'listbox');
        csOptions.forEach(function (option) {
            option.setAttribute('role', 'option');
            option.setAttribute('tabindex', '-1'); // make li elements keyboard focusable by script only
        });
        // set up a message to keep screen reader users informed of what the custom input is for/doing
        csStatus.textContent = csOptions.length + ' options available. Arrow down to browse.';

        csElement.addEventListener('click', function (e) {
            const currentFocus = findFocus();
            switch (csState) {
                case SELECT_STATES.INITIAL:
                    toggleList(SELECT_DROPDOWN_STATES.OPENED);
                    setState(SELECT_STATES.OPENED);
                    break;
                case SELECT_STATES.OPENED:
                    if (currentFocus === csInput) {
                        toggleList(SELECT_DROPDOWN_STATES.CLOSED);
                        setState(SELECT_STATES.INITIAL);
                    } else if (currentFocus.tagName === 'LI') {
                        makeChoice(currentFocus);
                        toggleList(SELECT_DROPDOWN_STATES.CLOSED);
                        setState(SELECT_STATES.CLOSED);
                    }
                    break;
                case SELECT_STATES.CLOSED:
                    toggleList(SELECT_DROPDOWN_STATES.OPENED);
                    setState(SELECT_STATES.OPENED);
                    break;
            }
        });

        csElement.addEventListener('keydown', function (e) {
            // Prevent the page from scrolling when the Select is selected and the
            // up/down arrow keys are pressed
            if (['ArrowUp', 'ArrowDown'].indexOf(e.code) > -1) {
                e.preventDefault();
            }
            doKeyAction(e.key);
        });

        // handle click outside
        document.addEventListener('click', function (e) {
            if (!e.target.closest(csSelector)) {
                toggleList(SELECT_DROPDOWN_STATES.CLOSED);
                setState(SELECT_STATES.INITIAL);
            }
        });

        function toggleList(whichWay) {
            if (whichWay === SELECT_DROPDOWN_STATES.OPENED) {
                csList.classList.add(expandedClass);
                csElement.setAttribute('aria-expanded', 'true');
            } else if (whichWay === SELECT_DROPDOWN_STATES.CLOSED) {
                csList.classList.remove(expandedClass);
                csElement.setAttribute('aria-expanded', 'false');
            }
        }

        function findFocus() {
            const focusPoint = document.activeElement;
            return focusPoint;
        }

        function moveFocus(from, to) {
            const options = optionsList.filter(function (option) {
                if (option.style.display === '') {
                    return true;
                }
            });

            if (to === 'input') {
                csInput.focus();
            }

            switch (from) {
                case csInput:
                    if (to === MOVE_DIRECTIONS.FORWARD) {
                        options[0].focus();
                    } else if (to === MOVE_DIRECTIONS.BACK) {
                        options[options.length - 1].focus();
                    }
                    break;
                case csOptions[0]:
                    if (to === MOVE_DIRECTIONS.FORWARD) {
                        options[1].focus();
                    } else if (to === MOVE_DIRECTIONS.BACK) {
                        csInput.focus();
                    }
                    break;
                case csOptions[csOptions.length - 1]:
                    if (to === MOVE_DIRECTIONS.FORWARD) {
                        options[0].focus();
                    } else if (to === MOVE_DIRECTIONS.BACK) {
                        options[options.length - 2].focus();
                    }
                    break;
                default:
                    const currentItem = findFocus();
                    const whichOne = options.indexOf(currentItem);
                    if (to === MOVE_DIRECTIONS.FORWARD) {
                        const nextOne = options[whichOne + 1];
                        nextOne.focus();
                    } else if (to === MOVE_DIRECTIONS.BACK && whichOne > 0) {
                        const previousOne = options[whichOne - 1];
                        previousOne.focus();
                    } else {
                        csInput.focus();
                    }
                    break;
            }
        }

        function makeChoice(selectedOption) {
            csInput.value = selectedOption.getAttribute('data-value') || selectedOption.textContent;

            optionsList.forEach((option) => option.classList.remove(csActiveOptionClass));

            selectedOption.classList.add(csActiveOptionClass);
            moveFocus(document.activeElement, 'input');
        }

        function setState(newState) {
            switch (newState) {
                case SELECT_STATES.INITIAL:
                    csState = SELECT_STATES.INITIAL;
                    break;
                case SELECT_STATES.OPENED:
                    csState = SELECT_STATES.OPENED;
                    break;
                case SELECT_STATES.CLOSED:
                    csState = SELECT_STATES.CLOSED;
            }
        }

        function doKeyAction(whichKey) {
            const currentFocus = findFocus();
            switch (whichKey) {
                case 'Enter':
                    if (csState === SELECT_STATES.INITIAL) {
                        toggleList(SELECT_DROPDOWN_STATES.OPENED);
                        setState(SELECT_STATES.OPENED);
                    } else if (csState === SELECT_STATES.OPENED && currentFocus.tagName === 'LI') {
                        makeChoice(currentFocus);
                        toggleList(SELECT_DROPDOWN_STATES.CLOSED);
                        setState(SELECT_STATES.CLOSED);
                    } else if (csState === SELECT_STATES.OPENED && currentFocus === csInput) {
                        toggleList(SELECT_DROPDOWN_STATES.CLOSED);
                        setState(SELECT_STATES.CLOSED);
                    } else {
                        toggleList(SELECT_DROPDOWN_STATES.OPENED);
                    }
                    break;

                case 'Escape':
                    if (csState === SELECT_STATES.OPENED) {
                        toggleList(SELECT_DROPDOWN_STATES.CLOSED);
                        setState(SELECT_STATES.INITIAL);
                    }
                    break;

                case 'ArrowDown':
                    if (csState === SELECT_STATES.INITIAL || csState === SELECT_STATES.CLOSED) {
                        toggleList(SELECT_DROPDOWN_STATES.OPENED);
                        moveFocus(csInput, MOVE_DIRECTIONS.FORWARD);
                        setState(SELECT_STATES.OPENED);
                    } else {
                        toggleList(SELECT_DROPDOWN_STATES.OPENED);
                        moveFocus(currentFocus, MOVE_DIRECTIONS.FORWARD);
                    }
                    break;
                case 'ArrowUp':
                    if (csState === SELECT_STATES.INITIAL || csState === SELECT_STATES.CLOSED) {
                        toggleList(SELECT_DROPDOWN_STATES.OPENED);
                        moveFocus(csInput, MOVE_DIRECTIONS.BACK);
                        setState(SELECT_STATES.OPENED);
                    } else {
                        moveFocus(currentFocus, MOVE_DIRECTIONS.BACK);
                    }
                    break;
                default:
                    break;
            }
        }
    });
});
