import {
    estiloProvincia,
    hoverProvincia,
    salirProvincia,
    mostrarInfoProvincia,
    ocultarInfoProvincia
} from "./mapa_funciones.js";

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
    maxZoom: CONFIG_MAPA.zoomMax,
    zoomControl: false
}).setView(CONFIG_MAPA.centro, CONFIG_MAPA.zoomInicial);

L.control.zoom({
    position: 'bottomright'
}).addTo(map);

map.setMaxBounds(CONFIG_MAPA.limites);

//mapa de fondo (después podemos cambiarlo)

L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; OpenStreetMap &copy; CARTO'
}).addTo(map);

//hago que lea el geoJSON
//agregué date.now para que el navegador descargue el json y no use cargas viejas
fetch('/spring/js/ProvinciasArgentinas.json?v=' + Date.now())
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

                layer.on('click', function(e) {

                console.log(feature);
                console.log(feature.properties);

                const idProvincia = feature.properties.id;

                console.log("Provincia clickeada:", idProvincia);

                const boton = document.querySelector(`.provincia[data-id="${idProvincia}"]`);

                if (boton) {
                    boton.click();
                } else {
                console.error("No se encontró botón");
                }
                });
            }

        });

        provinciasLayer.addTo(map);

    });