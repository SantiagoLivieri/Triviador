//estilo provincia
export function estiloProvincia(feature) {

  const idProvincia = feature.properties.id;

  const boton = document.querySelector(
    `.provincia[data-id="${idProvincia}"]`
  );

  const color = obtenerColorProvincia(boton);

  return crearEstiloProvincia(color);
}

//obtener color de provincia
export function obtenerColorProvincia(boton) {
  let color = "#e0d8b0";

  if (boton) {

    if (boton.classList.contains("provincia-rojo")) {
      color = "#dc2626";
    }

    else if (boton.classList.contains("provincia-azul")) {
      color = "#2563eb";
    }

    else if (boton.classList.contains("provincia-verde")) {
      color = "#16a34a";
    }
  }

  return color;
}

//crear estilo de provincia
export function crearEstiloProvincia(color) {
  return {
    color: "#333",
    weight: 1,
    fillColor: color,
    fillOpacity: 1
  };
}

//al hacer hover agranda
export function hoverProvincia(layer) {

  layer.setStyle({
    weight: 3
  });
}

//cuando dejas de hacer hover la provincia vuelve a la normalidad
export function salirProvincia(layer) {

  layer.setStyle(estiloProvincia(layer.feature));

}

//aparece nombre en hover
export function mostrarInfoProvincia(feature) {

  const panel = document.getElementById("info-provincia");
    
  const boton = document.querySelector(
    `.provincia[data-id="${feature.properties.id}"]`
  );

  if (boton) {
    panel.innerText = boton.dataset.nombre;
  }

  panel.style.display = "block";
}

//desaparece nombre sin hover
export function ocultarInfoProvincia() {

  document.getElementById("info-provincia").style.display = "none";
}