package com.tallerwebi.servicios;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.entidades.CategoriaPregunta;
import com.tallerwebi.entidades.Pregunta;
import com.tallerwebi.entidades.Provincia;
import com.tallerwebi.entidades.Rol;
import com.tallerwebi.entidades.TipoPregunta;
import com.tallerwebi.entidades.Usuario;
import com.tallerwebi.repositorios.RepositorioPregunta;
import com.tallerwebi.repositorios.RepositorioProvincia;
import com.tallerwebi.repositorios.RepositorioRol;
import com.tallerwebi.repositorios.RepositorioUsuario;

@Service
@Transactional
public class ServicioCargaInicialImpl implements ServicioCargaInicial {

  private static final Long CANTIDAD_MINIMA_PREGUNTAS = 10L;

  private static final String MULTIPLE_CHOICE = "MULTIPLE_CHOICE";
  private static final String NUMERICA = "NUMERICA";

  private static final String GEOGRAFIA = "GEOGRAFIA";
  private static final String HISTORIA = "HISTORIA";
  private static final String CULTURA_GENERAL = "CULTURA_GENERAL";
  private static final String DEPORTE = "DEPORTE";

  private static final String BUENOS_AIRES = "Buenos Aires";
  private static final String CABA = "CABA";
  private static final String CATAMARCA = "Catamarca";
  private static final String CHACO = "Chaco";
  private static final String CHUBUT = "Chubut";
  private static final String CORDOBA = "Córdoba";
  private static final String CORRIENTES = "Corrientes";
  private static final String ENTRE_RIOS = "Entre Ríos";
  private static final String FORMOSA = "Formosa";
  private static final String JUJUY = "Jujuy";
  private static final String LA_PAMPA = "La Pampa";
  private static final String LA_RIOJA = "La Rioja";
  private static final String MENDOZA = "Mendoza";
  private static final String MISIONES = "Misiones";
  private static final String NEUQUEN = "Neuquén";
  private static final String RIO_NEGRO = "Río Negro";
  private static final String SALTA = "Salta";
  private static final String SAN_JUAN = "San Juan";
  private static final String SAN_LUIS = "San Luis";
  private static final String SANTA_CRUZ = "Santa Cruz";
  private static final String SANTA_FE = "Santa Fe";
  private static final String SANTIAGO_DEL_ESTERO = "Santiago del Estero";
  private static final String TIERRA_DEL_FUEGO = "Tierra del Fuego";
  private static final String TUCUMAN = "Tucuman";

  private final RepositorioPregunta repositorioPregunta;
  private final RepositorioRol repositorioRol;
  private final RepositorioUsuario repositorioUsuario;
  private final RepositorioProvincia repositorioProvincia;

  @Autowired
  public ServicioCargaInicialImpl(
      RepositorioPregunta repositorioPregunta,
      RepositorioRol repositorioRol,
      RepositorioUsuario repositorioUsuario,
      RepositorioProvincia repositorioProvincia) {
    this.repositorioPregunta = repositorioPregunta;
    this.repositorioRol = repositorioRol;
    this.repositorioUsuario = repositorioUsuario;
    this.repositorioProvincia = repositorioProvincia;
  }

  @Override
  public void cargarDatosIniciales() {
    cargarRolesIniciales();
    cargarUsuarioAdminInicial();
    cargarPreguntasIniciales();
  }

  @Override
  public void cargarRolesIniciales() {
    crearRolSiNoExiste("JUGADOR");
    crearRolSiNoExiste("EDITOR");
    crearRolSiNoExiste("ADMIN");
  }

  private void crearRolSiNoExiste(String descripcion) {
    Rol rolExistente = repositorioRol.buscarPorDescripcion(descripcion);

    if (rolExistente != null) {
      return;
    }

    repositorioRol.guardar(new Rol(descripcion));
  }

  @Override
  public void cargarUsuarioAdminInicial() {
    Usuario adminExistente = repositorioUsuario.buscarUsuarioPorEmail("admin@triviador.com");

    if (adminExistente != null) {
      return;
    }

    Rol rolAdmin = repositorioRol.buscarPorDescripcion("ADMIN");

    Usuario admin = new Usuario();
    admin.setEmail("admin@triviador.com");
    admin.setPassword("admin123");
    admin.setNombre("Administrador");
    admin.setNombreJugador("Admin");
    admin.setRol(rolAdmin);

    repositorioUsuario.crearUsuario(admin);
  }

  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  @Override
  public void cargarPreguntasIniciales() {
    if (repositorioPregunta.contar() >= CANTIDAD_MINIMA_PREGUNTAS) {
      return;
    }

    final List<Provincia> todasLasProvincias = repositorioProvincia.buscarTodas();
    final Map<String, Provincia> mapaProvincias = todasLasProvincias
        .stream()
        .collect(Collectors.toMap(Provincia::getNombre, provincia -> provincia));

    String[][] todasLasPreguntas = {
        // --- BUENOS AIRES ---
        {
            "¿En qué río tuvo lugar la defensa soberana conocida como la Batalla de la Vuelta de Obligado?",
            "Río Paraná",
            "Río de la Plata",
            "Río Salado",
            "Río Uruguay",
            BUENOS_AIRES,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué destacado autor escribió el poema y obra cumbre de la literatura gauchesca 'Martín Fierro'?",
            "José Hernández",
            "Jorge Luis Borges",
            "Domingo F. Sarmiento",
            "Julio Cortázar",
            BUENOS_AIRES,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿De qué ciudad es originario el ex tenista, campeón del US Open, Juan Martín del Potro?",
            "Tandil",
            "Mar del Plata",
            "La Plata",
            "Bahía Blanca",
            BUENOS_AIRES,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- CABA ---
        {
            "¿En qué año se llevó a cabo la fundación definitiva de la ciudad por orden de Juan de Garay?",
            "1580",
            "1536",
            "1810",
            "1492",
            CABA,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué prestigioso teatro de ópera mundialmente reconocido está ubicado sobre la Avenida 9 de Julio?",
            "Teatro Colón",
            "Teatro San Martín",
            "Teatro Cervantes",
            "Teatro Gran Rex",
            CABA,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿En qué barrio porteño se encuentra ubicado el estadio conocido popularmente como 'La Bombonera'?",
            "La Boca",
            "Núñez",
            "Liniers",
            "Boedo",
            CABA,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- CATAMARCA ---
        {
            "¿Qué importante fraile y orador pronunció el famoso Sermón de la Constitución en 1853?",
            "Fray Mamerto Esquiú",
            "Cura Brochero",
            "Fray Luis Beltrán",
            "San Francisco Solano",
            CATAMARCA,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué prenda de abrigo tradicional, tejida a mano, tiene su Fiesta Nacional cada mes de julio?",
            "El Poncho",
            "La Ruana",
            "El Manto",
            "El Chal",
            CATAMARCA,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Por qué inmensas montañas de arena pasaba el Rally Dakar destacando su gran dificultad?",
            "Dunas de Fiambalá",
            "Dunas del Nihuil",
            "Médanos Blancos",
            "Dunas de Tatón",
            CATAMARCA,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- CHACO ---
        {
            "¿Qué trágico hecho represivo sufrieron los pueblos originarios qom y moqoit en el año 1924?",
            "Masacre de Napalpí",
            "Semana Trágica",
            "Patagonia Rebelde",
            "Campaña del Desierto",
            CHACO,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué disciplina artística interviene las calles de la capital mediante una Bienal Internacional?",
            "Escultura",
            "Muralismo",
            "Fotografía",
            "Pintura al óleo",
            CHACO,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué equipo de fútbol es el máximo y clásico rival del Club Atlético Sarmiento?",
            "Chaco For Ever",
            "Textil Mandiyú",
            "Boca Unidos",
            "Crucero del Norte",
            CHACO,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- CHUBUT ---
        {
            "¿De qué origen eran los inmigrantes colonos que fundaron ciudades como Trelew y Gaiman?",
            "Galeses",
            "Alemanes",
            "Suizos",
            "Rusos",
            CHUBUT,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué mamífero marino es la principal atracción turística de la Península Valdés?",
            "Ballena Franca Austral",
            "Orca",
            "Lobo Marino",
            "Delfín Rosado",
            CHUBUT,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué boxeador nacido en Trelew, apodado 'El Huracán', fue un histórico campeón mundial supermosca?",
            "Omar Narváez",
            "Sergio Martínez",
            "Marcos Maidana",
            "Jorge Castro",
            CHUBUT,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- CORDOBA ---
        {
            "¿Qué movimiento impulsado por estudiantes en 1918 transformó los estatutos universitarios de toda América?",
            "Reforma Universitaria",
            "Cordobazo",
            "Grito de Alcorta",
            "Noche de los Lápices",
            CORDOBA,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué ritmo musical popular e identitario fue popularizado inicialmente por Leonor Marzano?",
            "Cuarteto",
            "Chamamé",
            "Cumbia Villera",
            "Chacarera",
            CORDOBA,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué basquetbolista de la Generación Dorada, ex campeón de la NBA, nació en Las Varillas?",
            "Fabricio Oberto",
            "Emanuel Ginóbili",
            "Luis Scola",
            "Carlos Delfino",
            CORDOBA,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- CORRIENTES ---
        {
            "¿En qué histórica localización ribereña nació el general José de San Martín?",
            "Yapeyú",
            "San Lorenzo",
            "Paso de los Libres",
            "Ituzaingó",
            CORRIENTES,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué figura mítica del litoral es venerada cada 8 de enero con santuarios y banderas rojas?",
            "Gauchito Gil",
            "Difunta Correa",
            "San La Muerte",
            "Virgen de Itatí",
            CORRIENTES,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué jugador de fútbol, apodado 'El Pepe', es el goleador histórico del club Lanús?",
            "José Sand",
            "José Sosa",
            "José Chatruc",
            "José Luis Calderón",
            CORRIENTES,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- ENTRE RIOS ---
        {
            "¿Qué militar e influyente caudillo mandó a construir la suntuosa residencia llamada Palacio San José?",
            "Justo José de Urquiza",
            "Francisco Ramírez",
            "Juan Manuel de Rosas",
            "Facundo Quiroga",
            ENTRE_RIOS,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿En qué ciudad se realiza anualmente el Carnaval del País, el evento a cielo abierto más grande de Argentina?",
            "Gualeguaychú",
            "Paraná",
            "Concordia",
            "Colón",
            ENTRE_RIOS,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué aguerrido defensor central de la Selección Argentina, campeón del mundo en 2022, nació en Gualeguay?",
            "Lisandro Martínez",
            "Cristian Romero",
            "Nicolás Otamendi",
            "Marcos Acuña",
            ENTRE_RIOS,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- FORMOSA ---
        {
            "En 1955 dejó de ser formalmente un 'Territorio Nacional'. ¿Qué presidente firmó su provincialización?",
            "Juan Domingo Perón",
            "Arturo Frondizi",
            "Julio Argentino Roca",
            "Hipólito Yrigoyen",
            FORMOSA,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué peculiar instrumento de cuerda única, fabricado tradicionalmente con una lata, es típico del pueblo qom?",
            "Nvike",
            "Siku",
            "Charango",
            "Erke",
            FORMOSA,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué equipo de básquet representa habitualmente a la región en la primera división de la Liga Nacional?",
            "La Unión",
            "Regatas",
            "San Martín",
            "Quimsa",
            FORMOSA,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- JUJUY ---
        {
            "¿Qué prócer lideró el gran éxodo de población y ordenó quemar las tierras en 1812 para dejar sin recursos al enemigo?",
            "Manuel Belgrano",
            "José de San Martín",
            "Martín Miguel de Güemes",
            "Juan José Castelli",
            JUJUY,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "Durante el tradicional Carnaval de la región, la celebración oficial inicia cuando se desentierra a...",
            "El Diablo (Pujllay)",
            "La Pachamama",
            "El Ekeko",
            "El Coquena",
            JUJUY,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué habilidoso ex futbolista y emblema de River Plate, apodado 'El Burrito', nació en Ledesma?",
            "Ariel Ortega",
            "Marcelo Gallardo",
            "Pablo Aimar",
            "Javier Saviola",
            JUJUY,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- LA PAMPA ---
        {
            "La controversial operación militar del siglo XIX que ocupó estos territorios fue liderada por...",
            "Julio Argentino Roca",
            "Juan Manuel de Rosas",
            "Bartolomé Mitre",
            "Domingo F. Sarmiento",
            LA_PAMPA,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué destacado cardiólogo, inventor del bypass coronario, ejerció la medicina rural durante 12 años en Jacinto Arauz?",
            "René Favaloro",
            "Bernardo Houssay",
            "Luis Federico Leloir",
            "Ramón Carrillo",
            LA_PAMPA,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué inteligente mediocampista campeón del mundo en Qatar 2022 es oriundo de Santa Rosa?",
            "Alexis Mac Allister",
            "Leandro Paredes",
            "Rodrigo De Paul",
            "Enzo Fernández",
            LA_PAMPA,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- LA RIOJA ---
        {
            "¿Cómo apodaban popularmente al temido y célebre caudillo federal Facundo Quiroga?",
            "El Tigre de los Llanos",
            "El Restaurador de las Leyes",
            "El Supremo Entrerriano",
            "El Chacho",
            LA_RIOJA,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué festividad de Año Nuevo escenifica en las calles el encuentro pacífico entre españoles y diaguitas?",
            "El Tinkunaco",
            "La Chaya",
            "El Desentierro",
            "La Pachamama",
            LA_RIOJA,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué ex jugador y múltiple director técnico campeón con River Plate, apodado 'El Pelado', nació aquí?",
            "Ramón Díaz",
            "Matías Almeyda",
            "Leonardo Astrada",
            "Marcelo Gallardo",
            LA_RIOJA,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- MENDOZA ---
        {
            "¿Cómo se llamó el campamento militar donde San Martín instruyó al ejército antes de cruzar la cordillera?",
            "El Plumerillo",
            "Yapeyú",
            "San Lorenzo",
            "Cancha Rayada",
            MENDOZA,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿En qué famoso y gran anfiteatro se celebra el imponente acto central de la Vendimia?",
            "Frank Romero Day",
            "Próspero Molina",
            "Mario Alberto Kempes",
            "Cocomarola",
            MENDOZA,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué mediocampista histórico de la Selección Argentina, figura en el Mundial 2014, comenzó su carrera en Godoy Cruz?",
            "Enzo Pérez",
            "Javier Mascherano",
            "Lucas Biglia",
            "Fernando Gago",
            MENDOZA,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- MISIONES ---
        {
            "¿A qué orden religiosa pertenecían las misiones que dieron nombre a la región durante el siglo XVII?",
            "Jesuitas",
            "Franciscanos",
            "Dominicos",
            "Agustinos",
            MISIONES,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué destacado escritor uruguayo se instaló en la selva local y se inspiró en ella para escribir 'Cuentos de la selva'?",
            "Horacio Quiroga",
            "Eduardo Galeano",
            "Mario Benedetti",
            "Juan Carlos Onetti",
            MISIONES,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué histórico arquero, subcampeón del mundo en Brasil 2014, nació en la ciudad de Bernardo de Irigoyen?",
            "Sergio Romero",
            "Franco Armani",
            "Emiliano Martínez",
            "Willy Caballero",
            MISIONES,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- NEUQUEN ---
        {
            "¿Qué importante formación geológica rica en petróleo y gas no convencional transformó la matriz energética nacional?",
            "Vaca Muerta",
            "Cuenca Austral",
            "Golfo San Jorge",
            "Cerro Vanguardia",
            NEUQUEN,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué fósil de dinosaurio gigante, uno de los carnívoros más grandes del mundo, fue descubierto en este territorio?",
            "Giganotosaurus",
            "Tyrannosaurus rex",
            "Argentinosaurus",
            "Triceratops",
            NEUQUEN,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué lateral izquierdo de la selección nacional, campeón del mundo y apodado 'El Huevo', nació en Zapala?",
            "Marcos Acuña",
            "Nahuel Molina",
            "Gonzalo Montiel",
            "Nicolás Tagliafico",
            NEUQUEN,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- RIO NEGRO ---
        {
            "¿Qué presidente propuso en la década de 1980 mudar la Capital Federal a la ciudad de Viedma?",
            "Raúl Alfonsín",
            "Carlos Menem",
            "Néstor Kirchner",
            "Fernando de la Rúa",
            RIO_NEGRO,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué fruta es el símbolo económico por excelencia del Alto Valle y se exporta a nivel mundial?",
            "La Manzana",
            "El Limón",
            "La Uva",
            "El Arándano",
            RIO_NEGRO,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué famoso centro de deportes invernales e instalaciones de esquí está ubicado en San Carlos de Bariloche?",
            "Cerro Catedral",
            "Las Leñas",
            "Cerro Castor",
            "Chapelco",
            RIO_NEGRO,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- SALTA ---
        {
            "¿Con qué apodo temible eran conocidos los gauchos de caballería que frenaron los avances realistas en el norte?",
            "Los Infernales",
            "Los Colorados",
            "Los Blandengues",
            "Los Patricios",
            SALTA,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué histórico conjunto folclórico, fundado en 1948, vistió como marca registrada el poncho tinto característico?",
            "Los Chalchaleros",
            "Los Nocheros",
            "Los Fronterizos",
            "Los Tucu Tucu",
            SALTA,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué destacado piloto motociclista hizo historia al ganar dos ediciones del exigente Rally Dakar?",
            "Kevin Benavides",
            "Marcos Patronelli",
            "Orlando Terranova",
            "Lucio Álvarez",
            SALTA,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- SAN JUAN ---
        {
            "¿Qué prócer de la educación y expresidente de la nación redactó su famosa obra sociológica 'Facundo'?",
            "Domingo Faustino Sarmiento",
            "Nicolás Avellaneda",
            "Juan Bautista Alberdi",
            "Bartolomé Mitre",
            SAN_JUAN,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿A qué figura o elemento vital de la naturaleza se le rinde homenaje en su fiesta nacional más importante?",
            "Al Sol",
            "A la Pachamama",
            "A la Vendimia",
            "Al Viento Zonda",
            SAN_JUAN,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿En qué deporte sobre ruedas los clubes de la región son potencia internacional acumulando campeonatos mundiales?",
            "Hockey sobre patines",
            "Patinaje artístico",
            "Ciclismo de pista",
            "Skateboarding",
            SAN_JUAN,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- SAN LUIS ---
        {
            "¿Qué heroico granadero, nacido en la región, acompañó a Cabral para salvar la vida de San Martín en San Lorenzo?",
            "Juan Bautista Baigorria",
            "Antonio Ruiz (Falucho)",
            "Pedro Ríos",
            "Fray Luis Beltrán",
            SAN_LUIS,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿A qué localidad serrana famosa por su asombroso clima y purificación del aire se la conoce como 'el tercer microclima del mundo'?",
            "Villa de Merlo",
            "Potrero de los Funes",
            "El Trapiche",
            "La Carolina",
            SAN_LUIS,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿En torno a qué hermoso embalse se trazó un exigente circuito automovilístico de nivel internacional?",
            "Potrero de los Funes",
            "Lago San Roque",
            "Embalse Río Tercero",
            "Dique Los Molinos",
            SAN_LUIS,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- SANTA CRUZ ---
        {
            "¿Bajo qué nombre popular e histórico se conoció a la feroz huelga obrera reprimida por el ejército en 1921?",
            "La Patagonia Rebelde",
            "La Semana Trágica",
            "El Cordobazo",
            "El Grito de Alcorta",
            SANTA_CRUZ,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué asombroso patrimonio prehistorico alberga miles de impresiones rupestres en negativo de extremidades humanas?",
            "Cueva de las Manos",
            "Cerro Colorado",
            "Ruinas de Quilmes",
            "Piedra Pintada",
            SANTA_CRUZ,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué ex boxeador y campeón mundial, recordado por su épica victoria frente a John David Jackson, nació en Caleta Olivia?",
            "Jorge 'Locomotora' Castro",
            "Sergio 'Maravilla' Martínez",
            "Carlos Monzón",
            "Marcos 'Chino' Maidana",
            SANTA_CRUZ,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- SANTA FE ---
        {
            "¿A orillas de qué río se enarboló e izó por primera vez la bandera creada por Manuel Belgrano en 1812?",
            "Río Paraná",
            "Río de la Plata",
            "Río Uruguay",
            "Río Salado",
            SANTA_FE,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué emblemática banda musical tropical popularizó a nivel masivo la canción 'El Bombón Asesino'?",
            "Los Palmeras",
            "Ráfaga",
            "Amar Azul",
            "La Nueva Luna",
            SANTA_FE,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué talentoso futbolista y múltiple ganador del Balón de Oro se formó en las infantiles del club Newell's Old Boys?",
            "Lionel Messi",
            "Ángel Di María",
            "Gabriel Batistuta",
            "Maxi Rodríguez",
            SANTA_FE,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- SANTIAGO DEL ESTERO ---
        {
            "Por ser el primer asentamiento poblacional español constante en el actual país, recibe tradicionalmente el título de...",
            "Madre de Ciudades",
            "La Docta",
            "La Cuna de la Independencia",
            "El Jardín de la República",
            SANTIAGO_DEL_ESTERO,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué instrumento de percusión, tallado a mano en troncos ahuecados, es el rey indiscutido de su música folclórica?",
            "El bombo legüero",
            "El cajón peruano",
            "Las maracas",
            "El bongó",
            SANTIAGO_DEL_ESTERO,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué moderno circuito internacional recibe año a año a la espectacular categoría mundial de MotoGP?",
            "Termas de Río Hondo",
            "Oscar y Juan Gálvez",
            "Villicum",
            "Cabalén",
            SANTIAGO_DEL_ESTERO,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- TIERRA DEL FUEGO ---
        {
            "Hasta el año 1947, este inhóspito territorio albergó un temido establecimiento penitenciario conocido como...",
            "El Presidio del Fin del Mundo",
            "La Cárcel de Caseros",
            "El Penal de Magdalena",
            "La Isla del Diablo",
            TIERRA_DEL_FUEGO,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Qué pueblo originario nómade, experto en sobrevivir navegando en canoas por los gélidos canales, habitaba la zona?",
            "Yámanas",
            "Mapuches",
            "Guaraníes",
            "Diaguitas",
            TIERRA_DEL_FUEGO,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué centro invernal es el principal polo del esquí provincial y se promociona a nivel global como 'el más austral del mundo'?",
            "Cerro Castor",
            "Cerro Catedral",
            "Las Leñas",
            "Chapelco",
            TIERRA_DEL_FUEGO,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
        // --- TUCUMAN ---
        {
            "¿En la casa de qué importante familia de la época se sesionó el Congreso que declaró la Independencia de 1816?",
            "Francisca Bazán de Laguna",
            "Familia Anchorena",
            "Estancia El Plumerillo",
            "Posta de Yatasto",
            TUCUMAN,
            MULTIPLE_CHOICE,
            HISTORIA,
        },
        {
            "¿Por qué halagador título o apodo es tradicionalmente conocida esta pequeña pero fértil provincia norteña?",
            "El Jardín de la República",
            "Madre de Ciudades",
            "Tierra del Sol",
            "La Docta",
            TUCUMAN,
            MULTIPLE_CHOICE,
            CULTURA_GENERAL,
        },
        {
            "¿Qué histórico club de fútbol viste a rayas celestes y blancas y hace de local en el estadio 'Monumental José Fierro'?",
            "Atlético Tucumán",
            "San Martín de Tucumán",
            "Central Norte",
            "Gimnasia y Tiro",
            TUCUMAN,
            MULTIPLE_CHOICE,
            DEPORTE,
        },
    };

    for (String[] datosPreguntas : todasLasPreguntas) {
      Provincia provincia = mapaProvincias.get(datosPreguntas[5]);
      TipoPregunta tipo = TipoPregunta.valueOf(datosPreguntas[6]);
      CategoriaPregunta categoria = CategoriaPregunta.valueOf(datosPreguntas[7]);

      if (tipo == TipoPregunta.NUMERICA) {
        repositorioPregunta.guardar(
            crearPreguntaNumerica(datosPreguntas[0], datosPreguntas[1], tipo, categoria, provincia));
      } else {
        repositorioPregunta.guardar(
            crearPregunta(
                datosPreguntas[0],
                datosPreguntas[1],
                datosPreguntas[2],
                datosPreguntas[3],
                datosPreguntas[4],
                tipo,
                categoria,
                provincia));
      }
    }
  }

  private Pregunta crearPregunta(
      String enunciado,
      String respuestaCorrecta,
      String opcionIncorrectaUno,
      String opcionIncorrectaDos,
      String opcionIncorrectaTres,
      TipoPregunta tipo,
      CategoriaPregunta categoria,
      Provincia provinciaFiltrada) {
    return new Pregunta(
        enunciado,
        respuestaCorrecta,
        opcionIncorrectaUno,
        opcionIncorrectaDos,
        opcionIncorrectaTres,
        tipo,
        categoria,
        provinciaFiltrada);
  }

  private Pregunta crearPreguntaNumerica(
      String enunciado,
      String respuestaCorrecta,
      TipoPregunta tipo,
      CategoriaPregunta cat,
      Provincia prov) {
    return new Pregunta(enunciado, respuestaCorrecta, tipo, cat, prov);
  }
}
