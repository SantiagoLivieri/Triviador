const modalReporte = document.getElementById("modalReporte");
const respuestaPartidaInput = document.getElementById(
  "respuestaPartidaId"
);
const preguntaReportada = document.getElementById(
  "preguntaReportada"
);
const cerrarModalReporte = document.getElementById(
  "cerrarModalReporte"
);
const cancelarReporte = document.getElementById(
  "cancelarReporte"
);

function abrirModalReporte(boton) {
  if (
    !modalReporte ||
    !respuestaPartidaInput ||
    !preguntaReportada
  ) {
    return;
  }

  respuestaPartidaInput.value =
    boton.dataset.respuestaId;

  preguntaReportada.textContent =
    boton.dataset.pregunta;

  modalReporte.hidden = false;
}

function cerrarReporte() {
  if (!modalReporte) {
    return;
  }

  modalReporte.hidden = true;
}

document
  .querySelectorAll(".btn-reportar-pregunta")
  .forEach(function (boton) {
    boton.addEventListener("click", function () {
      abrirModalReporte(boton);
    });
  });

if (cerrarModalReporte) {
  cerrarModalReporte.addEventListener(
    "click",
    cerrarReporte
  );
}

if (cancelarReporte) {
  cancelarReporte.addEventListener(
    "click",
    cerrarReporte
  );
}

if (modalReporte) {
  modalReporte.addEventListener(
    "click",
    function (evento) {
      if (evento.target === modalReporte) {
        cerrarReporte();
      }
    }
  );
}