package com.tallerwebi.servicios;

import com.tallerwebi.entidades.Usuario;
import java.util.List;

public interface ServicioRanking {
  List<Usuario> obtenerTop10General();
  Long calcularPuestoUsuario(Usuario usuario);
}
