window.addEventListener('DOMContentLoaded', () => {
    function changeTabs(e) {
        const target = e.target;
        const parent = target.parentNode;
        const container = parent.parentNode.parentNode;

        // Remove all current selected tabs
        parent
            .querySelectorAll('[aria-selected="true"]')
            .forEach((t) => t.setAttribute('aria-selected', false));

        // Set this tab as selected
        target.setAttribute('aria-selected', true);

        // Hide all tab panels
        container.querySelectorAll('[role="tabpanel"]').forEach((p) => (p.style.display = 'none'));

        // Show the selected panel
        container.parentNode.querySelector(
            `#${target.getAttribute('aria-controls')}`
        ).style.display = 'flex';
    }

    const tabs = document.querySelectorAll('[role="tab"]');

    // Add a click event handler to each tab
    tabs.forEach((tab) => {
        tab.addEventListener('click', changeTabs);
    });
});
