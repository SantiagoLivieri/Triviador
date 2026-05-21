//cargo y delimito el mapa
const CONFIG_MAPA = {
    centro: [-38.4161, -63.6167],
    zoomInicial: 4,
    zoomMin: 4,
    zoomMax: 7,

    limites: [
        [-55, -75],
        [-20, -50]
    ]
};

const map = L.map('map', {
    minZoom: CONFIG_MAPA.zoomMin,
    maxZoom: CONFIG_MAPA.zoomMax
}).setView(CONFIG_MAPA.centro, CONFIG_MAPA.zoomInicial);

map.setMaxBounds(CONFIG_MAPA.limites);

//mapa de fondo (después podemos cambiarlo)

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap'
}).addTo(map);

//hago que lea el geoJSON

fetch('/spring/js/ProvinciasArgentinas.json')
    .then(response => response.json())
    .then(data => {

        const provinciasLayer = L.geoJSON(data, {

            style: estiloProvincia,

            onEachFeature: function(feature, layer) {

                layer.on('mouseover', function() {

                    hoverProvincia(layer);

                    mostrarInfoProvincia(feature);
                });

                layer.on('mouseout', function() {

                    salirProvincia(layer);

                    ocultarInfoProvincia();
                });
            }

        });

        provinciasLayer.addTo(map);

    });

//funciones
    //estilo de provincias
function estiloProvincia(feature) {

    return {
        color: '#333',
        weight: 1,
        fillColor: '#e0d8b0',
        fillOpacity: 1
    };
}

//FUNCIONES
    //al hacer hover cambia color y agranda
function hoverProvincia(layer) {

    layer.setStyle({
        fillColor: '#d63535',
        weight: 3
    });

    layer.bringToFront();
}

    //cuando dejas de hacer hover la provincia vuelve a la normalidad
function salirProvincia(layer) {

    layer.setStyle(estiloProvincia(layer.feature));

}

    //aparece nombre en hover
function mostrarInfoProvincia(feature) {

    const panel = document.getElementById('info-provincia');

    panel.style.display = 'block';

    panel.innerText = feature.properties.NAME_1;
}

    //desaparece nombre sin hover
function ocultarInfoProvincia() {

    document.getElementById('info-provincia').style.display = 'none';
}