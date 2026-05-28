package com.tallerwebi.punta_a_punta;

import java.io.IOException;

public class ReiniciarDB {

  public static void limpiarBaseDeDatos() {
    try {
      String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
      String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
      String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "triviador";
      String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
      String dbPassword = System.getenv("DB_PASSWORD") != null
        ? System.getenv("DB_PASSWORD")
        : "root";

      String sqlCommands =
        "DELETE FROM Usuario;\n" +
        "ALTER TABLE Usuario AUTO_INCREMENT = 1;\n" +
        "INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);";

      @SuppressWarnings("unused")
      String comando = String.format(
        "docker exec tallerwebi-mysql mysql -h %s -P %s -u %s -p%s %s -e \"%s\"",
        dbHost,
        dbPort,
        dbUser,
        dbPassword,
        dbName,
        sqlCommands
      );

      String os = System.getProperty("os.name").toLowerCase();
      Process process;

      if (os.contains("win")) {
        // Si es Windows, usamos cmd.exe para ejecutar el script de limpieza
        process = Runtime.getRuntime().exec("cmd.exe /c tu_script_de_limpieza.bat");
        // O si es un archivo .sh y tenés Git Bash instalado:
        // process = Runtime.getRuntime().exec("C:\\Program Files\\Git\\bin\\bash.exe
        // tu_script.sh");
      } else {
        // Si es Linux/Mac, mantiene el comportamiento original
        process =
          Runtime
            .getRuntime()
            .exec(new String[] { "/bin/bash", "-c", "./tu_script_de_limpieza.sh" });
      }

      int exitCode = process.waitFor();

      if (exitCode == 0) {
        System.out.println("Base de datos limpiada exitosamente");
      } else {
        System.err.println("Error al limpiar la base de datos. Exit code: " + exitCode);
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Error ejecutando script de limpieza: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
