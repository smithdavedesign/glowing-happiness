document.addEventListener('DOMContentLoaded', function () {
    const dynamicTiles = document.querySelectorAll('.cmp-tilelist--dynamic');
    dynamicTiles.forEach(async (dynamicTile) => {
        async function getTilesData(path) {
            const request = await fetch(`/bin/solidigm/dynamicTilelist?path=${path}`);
            const response = await request.json();

            return response;
        }

        function generateTile(baseTemplate, data) {
            const template = baseTemplate.cloneNode(true);

            template.querySelector('.cmp-tilelist__icon').classList.add(`icon--${data.icon}`);
            template.querySelector('.cmp-tilelist__title').textContent = data.title;
            template.querySelector('.cmp-tilelist__description').textContent = data.description;
            const link = template.querySelector('.solidigm-btn');
            if (data.link) {
                link.setAttribute('href', data.link.url);
                link.setAttribute('target', data.link.target);
                data.link.linkType.length > 0 && link.classList.add(data.link.linkType);
                link.querySelector('span').textContent = data.link.label;
            } else {
                link.remove();
            }

            const container = document.createElement('li');
            container.classList.add('cmp-tilelist__tile');
            container.append(template);

            return container;
        }

        const path = dynamicTile.getAttribute('data-path');
        const template = dynamicTile.querySelector('template').content;
        const tilesContainer = dynamicTile.querySelector('.cmp-tilelist__grid');

        const data = await getTilesData(path);
        data.forEach((tileInfo) => {
            tilesContainer.appendChild(generateTile(template, tileInfo));
        });
    });
});
