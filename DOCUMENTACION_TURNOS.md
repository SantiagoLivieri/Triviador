Resumen:
Se modificó:
*ControladorJuego
*ServicioJuego
*ServicioJuegoImpl
*juego
*pregunta

Se creó:
+Partida
+RepositorioPartida
+RepositorioPartidaImpl
+TiempoAgotadoException
+TurnoinvalidoException

El objetivo inicial era implementar un temporizador en la pantalla de juego y pregunta, que al llegar a 0 saltee el turno actual y siga al siguiente jugador. Para implementar esto, fue necesario migrar el manejo de turnos desde una logica en el cliente (HttpSession) hacia un estado en el servidor (BBDD). Por lo tanto, se tuvo que crear la clase Partida para controlar esta nueva lógica, y actualizar o eliminar métodos esenciales de otras clases (*).

La razon es porque la memoria temporal del navegador es facilmente manipulable

Se eliminó (-) Se agregó (+) Se modificó (*)

*ControladorJuego:

-private static final String TURNO_ACTUAL = "turnoActual";
+private static final String ATRIBUTO_PARTIDA_ID = "partidaId";

Esto elimina la dependencia de la sesión HTTP para rastrear de quien es el turno. Ahora el controlador usa partidaId como llave maestra. El servicio busca la partida en la base de datos y la partida sabe de quien es el turno. Tambien se agregaron constantes de redireccion para evitar advertencias de código duplicado. (REDIRECT_JUEGO, MENSAJE_RESULTADO).

*Método iniciarPartida:
Antes devolvia un String ("redirect:/juego") y forzaba el turno actual a 0 en la sesión del navegador.
Ahora devuelve un ModelAndView. Llama al servicio para crear la partida en la base de datos y captura el id generado. Redirige a la ruta pasando el id: redirect:/juego?id={partidaId}.
De esta manera, al iniciar el juego, la base de datos crea una sala unica a la cual el controlador vincula los jugadores.

*Método mostrarJuego: Antes traia a todos los jugadores en la base de datos.
Ahora recibe el @RequestParam("id") Long partidaId, trae el objeto Partida partida, y trae el turno actual y solo a los jugadores vinculados a cierta partida por id.


*Método seleccionarProvincia
Aparte de migrar el sistema de sesion HTTP, solo se agrego un bloque try-catch que llama a servicioJuego.procesarJugada(). Su proposito es:
(En etapa 1) 
Si el temporizador llega a 0, lanza una excepcion, aborta la jugada y devuelve al tablero.
Si se selecciona una provincia antes de que el temporizador llegue a 0, cambia la partida a etapa 2, que resetea el reloj del servidor (LocalDateTime.now()) para asi resetear el temporizador.

*Metodo responderProvincia
Se traslado la logica que habia en el metodo a la clase ServicioJuegoImpl, en el metodo procesarRespuestaYPasarTurno();


-Métodos auxiliares: obtenerTurnoActual() y avanzarTurno() se trasladaron a la clase ServicioJuegoImpl.

+tiempoAgotado
Un nuevo @RequestMapping que escucha llamadas POST a /juego/tiempo-agotado.
Cuando el JS llega a 0 en el temporizador, manda una peticion, que se usa para llamar al metodo forzarSaltoPorTiempo(partidaId); quien valida la hora del servidor y le quita el turno al jugador si es verdadero.




*ServicioJuego: utiliza la entidad Partida para coordinar estados de juego consistentes (turnos, tiempos y puntaje) manejados por el servidor.

*inivializarPartida:
Antes eliminaba a todos los jugadores de la base de datos, para crear una partida totalmente nueva. Ahora solo crea 3 nuevos jugadores ingresados e instancia un nuevo objeto Partida a la cual asocia los nuevos jugadores, y tambien inicia en etapa 1, da arranque al timer y devuelve un long (la id).

-obtenerJugadorDelTurno: Se elimino de ServicioJuegoImpl, porque ahora es responsabilidad de la nueva clase Partida el saber de quien es el turno (que se consigue con el nuevo metodo obtenerPartidaPorId();)

+procesarJugada: recibe id de partida, jugador y provincia. Valida que el jugador no se exceda del limite dle temporizador (empleando ChronoUnit.SECONDS). Si es correcto, pasa a etapa 2 (pantalla de pregunta donde se reinicia le timer)

-responderPregunta: se reemplzo por procesarRespuestaYPasarTurno():
Antes verificaba respuesta, pasaba puntos y el controlador se encargaba de los turnos. Ahora directamente el metodo hace todo (evalua respuesta, conquista provincia, suma puntos y llama al avanzarTurno(partida)).

+avanzarTurno(Partida) y +obtenerOpcionesMezcaldas(Pregunta): ya existian en el ControladorJuego, se trasladaron a esta clase ServicioJuegoImpl.

+forzarSaltoPorTiempo(): Busca la partida, verifica con ChronoUnit la diferencia entre el inicio del turno y la hora actual. Si superó los 30 segundos, invoca avanzarTurno(). Este metodo es llamado por el sistema en el momento que el timer de JS llega a 0. A diferencia de la logica en procesarJugada, que se activa con input del jugador.


+Partida.java: Objeto de la partida (entidad)
tiene un (fetch = FetchType.EAGER) para que hibernate siempre traiga la lista de jugadores, sino pueden ocurrir errores.

+RepositorioPartida: la comunicacion con mysql


*juego y *pregunta: se añadio un casi identico script para iniciar el contador segun la hora de cuando comenzo el turno (etapa1 y etapa2) 

Los /*[[...]]*/ sirven para que se pueda abrir tanto de manera local, como en el servidor: "Es una técnica de "Inyección Segura". Si el archivo HTML se abre localmente (sin el servidor de Spring), el navegador lo ignora como un comentario y asigna el valor por defecto (0 o ''), evitando que la página se rompa. Si se ejecuta en el servidor, Spring borra el comentario y estampa el valor real de Java directamente en el código JavaScript"

.toString convierte el LocalDateTime en un formato que js puede leer.

juego.html saca el id del objeto partida (${partida.id}). Pero en pregunta.html, como el controlador envio el id suelto en el modelo, lo saca directamente (${partidaId}).

Math.max(0, ...) evita numeros negativos, que darian errores.

forzarTiempoAgotado(): lo que hace es crear un input oculto con el id de la partida y presiona un boton de submit(), con el objetivo de enviar un POST al servidor, indicando que el timer llego a 0.
