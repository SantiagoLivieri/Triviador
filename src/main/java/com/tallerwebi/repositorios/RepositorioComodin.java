package com.tallerwebi.repositorios;

import com.tallerwebi.entidades.Comodin;
import java.util.List;

public interface RepositorioComodin {
  Comodin buscarPorNombre(String nombre);

  List<Comodin> buscarTodos();
}
