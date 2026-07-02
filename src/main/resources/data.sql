/*
-- =============== PROVINCIAS ========================= --
-- --- BUENOS AIRES (provincia_id: 1) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (1, 'Buenos Aires', 0);

-- --- CABA (provincia_id: 2) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (2, 'CABA', 0);

-- --- CATAMARCA (provincia_id: 3) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (3, 'Catamarca', 0);

-- --- CHACO (provincia_id: 4) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (4, 'Chaco', 0);

-- --- CHUBUT (provincia_id: 5) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (5, 'Chubut', 0);

-- --- CÓRDOBA (provincia_id: 6) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (6, 'Córdoba', 0);

-- --- CORRIENTES (provincia_id: 7) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (7, 'Corrientes', 0);

-- --- ENTRE RÍOS (provincia_id: 8) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (8, 'Entre Ríos', 0);

-- --- FORMOSA (provincia_id: 9) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (9, 'Formosa', 0);

-- --- JUJUY (provincia_id: 10) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (10, 'Jujuy', 0);

-- --- LA PAMPA (provincia_id: 11) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (11, 'La Pampa', 0);

-- --- LA RIOJA (provincia_id: 12) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (12, 'La Rioja', 0);

-- --- MENDOZA (provincia_id: 13) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (13, 'Mendoza', 0);

-- --- MISIONES (provincia_id: 14) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (14, 'Misiones', 0);

-- --- NEUQUÉN (provincia_id: 15) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (15, 'Neuquén', 0);

-- --- RÍO NEGRO (provincia_id: 16) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (16, 'Río Negro', 0);

-- --- SALTA (provincia_id: 17) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (17, 'Salta', 0);

-- --- SAN JUAN (provincia_id: 18) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (18, 'San Juan', 0);

-- --- SAN LUIS (provincia_id: 19) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (19, 'San Luis', 0);

-- --- SANTA CRUZ (provincia_id: 20) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (20, 'Santa Cruz', 0);

-- --- SANTA FE (provincia_id: 21) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (21, 'Santa Fe', 0);

-- --- SANTIAGO DEL ESTERO (provincia_id: 22) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (22, 'Santiago del Estero', 0);

-- --- TIERRA DEL FUEGO (provincia_id: 23) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (23, 'Tierra del Fuego', 0);

-- --- TUCUMÁN (provincia_id: 24) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (24, 'Tucumán', 0);

-- ====================== PREGUNTAS ======================= --

-- --- BUENOS AIRES (provincia_id: 1) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (1, '¿En qué río tuvo lugar la defensa soberana conocida como la Batalla de la Vuelta de Obligado?', 'Río Paraná', 'Río de la Plata', 'Río Salado', 'Río Uruguay', 'MULTIPLE_CHOICE', 'HISTORIA', 1);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (2, '¿Qué destacado autor escribió el poema y obra cumbre de la literatura gauchesca ''Martín Fierro''?', 'José Hernández', 'Jorge Luis Borges', 'Domingo F. Sarmiento', 'Julio Cortázar', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 1);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (3, '¿De qué ciudad es originario el ex tenista, campeón del US Open, Juan Martín del Potro?', 'Tandil', 'Mar del Plata', 'La Plata', 'Bahía Blanca', 'MULTIPLE_CHOICE', 'DEPORTE', 1);

-- --- CABA (provincia_id: 2) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (4, '¿En qué año se llevó a cabo la fundación definitiva de la ciudad por orden de Juan de Garay?', '1580', '1536', '1810', '1492', 'MULTIPLE_CHOICE', 'HISTORIA', 2);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (5, '¿Qué prestigioso teatro de ópera mundialmente reconocido está ubicado sobre la Avenida 9 de Julio?', 'Teatro Colón', 'Teatro San Martín', 'Teatro Cervantes', 'Teatro Gran Rex', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 2);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (6, '¿En qué barrio porteño se encuentra ubicado el estadio conocido popularmente como ''La Bombonera''?', 'La Boca', 'Núñez', 'Liniers', 'Boedo', 'MULTIPLE_CHOICE', 'DEPORTE', 2);

-- --- CATAMARCA (provincia_id: 3) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (7, '¿Qué importante fraile y orador pronunció el famoso Sermón de la Constitución en 1853?', 'Fray Mamerto Esquiú', 'Cura Brochero', 'Fray Luis Beltrán', 'San Francisco Solano', 'MULTIPLE_CHOICE', 'HISTORIA', 3);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (8, '¿Qué prenda de abrigo tradicional, tejida a mano, tiene su Fiesta Nacional cada mes de julio?', 'El Poncho', 'La Ruana', 'El Manto', 'El Chal', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 3);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (9, '¿Por qué inmensas montañas de arena pasaba el Rally Dakar destacando su gran dificultad?', 'Dunas de Fiambalá', 'Dunas del Nihuil', 'Médanos Blancos', 'Dunas de Tatón', 'MULTIPLE_CHOICE', 'DEPORTE', 3);

-- --- CHACO (provincia_id: 4) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (10, '¿Qué trágico hecho represivo sufrieron los pueblos originarios qom y moqoit en el año 1924?', 'Masacre de Napalpí', 'Semana Trágica', 'Patagonia Rebelde', 'Campaña del Desierto', 'MULTIPLE_CHOICE', 'HISTORIA', 4);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (11, '¿Qué disciplina artística interviene las calles de la capital mediante una Bienal Internacional?', 'Escultura', 'Muralismo', 'Fotografía', 'Pintura al óleo', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 4);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (12, '¿Qué equipo de fútbol es el máximo y clásico rival del Club Atlético Sarmiento?', 'Chaco For Ever', 'Textil Mandiyú', 'Boca Unidos', 'Crucero del Norte', 'MULTIPLE_CHOICE', 'DEPORTE', 4);

-- --- CHUBUT (provincia_id: 5) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (13, '¿De qué origen eran los inmigrantes colonos que fundaron ciudades como Trelew y Gaiman?', 'Galeses', 'Alemanes', 'Suizos', 'Rusos', 'MULTIPLE_CHOICE', 'HISTORIA', 5);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (14, '¿Qué mamífero marino es la principal atracción turística de la Península Valdés?', 'Ballena Franca Austral', 'Orca', 'Lobo Marino', 'Delfín Rosado', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 5);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (15, '¿Qué boxeador nacido en Trelew, apodado ''El Huracán'', fue un histórico campeón mundial supermosca?', 'Omar Narváez', 'Sergio Martínez', 'Marcos Maidana', 'Jorge Castro', 'MULTIPLE_CHOICE', 'DEPORTE', 5);

-- --- CÓRDOBA (provincia_id: 6) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (16, '¿Qué movimiento impulsado por estudiantes en 1918 transformó los estatutos universitarios de toda América?', 'Reforma Universitaria', 'Cordobazo', 'Grito de Alcorta', 'Noche de los Lápices', 'MULTIPLE_CHOICE', 'HISTORIA', 6);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (17, '¿Qué ritmo musical popular e identitario fue popularizado inicialmente por Leonor Marzano?', 'Cuarteto', 'Chamamé', 'Cumbia Villera', 'Chacarera', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 6);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (18, '¿Qué basquetbolista de la Generación Dorada, ex campeón de la NBA, nació en Las Varillas?', 'Fabricio Oberto', 'Emanuel Ginóbili', 'Luis Scola', 'Carlos Delfino', 'MULTIPLE_CHOICE', 'DEPORTE', 6);

-- --- CORRIENTES (provincia_id: 7) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (19, '¿En qué histórica localización ribereña nació el general José de San Martín?', 'Yapeyú', 'San Lorenzo', 'Paso de los Libres', 'Ituzaingó', 'MULTIPLE_CHOICE', 'HISTORIA', 7);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (20, '¿Qué figura mítica del litoral es venerada cada 8 de enero con santuarios y banderas rojas?', 'Gauchito Gil', 'Difunta Correa', 'San La Muerte', 'Virgen de Itatí', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 7);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (21, '¿Qué jugador de fútbol, apodado ''El Pepe'', es el goleador histórico del club Lanús?', 'José Sand', 'José Sosa', 'José Chatruc', 'José Luis Calderón', 'MULTIPLE_CHOICE', 'DEPORTE', 7);

-- --- ENTRE RÍOS (provincia_id: 8) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (22, '¿Qué militar e influyente caudillo mandó a construir la suntuosa residencia llamada Palacio San José?', 'Justo José de Urquiza', 'Francisco Ramírez', 'Juan Manuel de Rosas', 'Facundo Quiroga', 'MULTIPLE_CHOICE', 'HISTORIA', 8);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (23, '¿En qué ciudad se realiza anualmente el Carnaval del País, el evento a cielo abierto más grande de Argentina?', 'Gualeguaychú', 'Paraná', 'Concordia', 'Colón', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 8);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (24, '¿Qué aguerrido defensor central de la Selección Argentina, campeón del mundo en 2022, nació en Gualeguay?', 'Lisandro Martínez', 'Cristian Romero', 'Nicolás Otamendi', 'Marcos Acuña', 'MULTIPLE_CHOICE', 'DEPORTE', 8);

-- --- FORMOSA (provincia_id: 9) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (25, 'En 1955 dejó de ser formalmente un ''Territorio Nacional''. ¿Qué presidente firmó su provincialización?', 'Juan Domingo Perón', 'Arturo Frondizi', 'Julio Argentino Roca', 'Hipólito Yrigoyen', 'MULTIPLE_CHOICE', 'HISTORIA', 9);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (26, '¿Qué peculiar instrumento de cuerda única, fabricado tradicionalmente con una lata, es típico del pueblo qom?', 'Nvike', 'Siku', 'Charango', 'Erke', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 9);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (27, '¿Qué equipo de básquet representa habitualmente a la región en la primera división de la Liga Nacional?', 'La Unión', 'Regatas', 'San Martín', 'Quimsa', 'MULTIPLE_CHOICE', 'DEPORTE', 9);

-- --- JUJUY (provincia_id: 10) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (28, '¿Qué prócer lideró el gran éxodo de población y ordenó quemar las tierras en 1812 para dejar sin recursos al enemigo?', 'Manuel Belgrano', 'José de San Martín', 'Martín Miguel de Güemes', 'Juan José Castelli', 'MULTIPLE_CHOICE', 'HISTORIA', 10);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (29, 'Durante el tradicional Carnaval de la región, la celebración oficial inicia cuando se desentierra a...', 'El Diablo (Pujllay)', 'La Pachamama', 'El Ekeko', 'El Coquena', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 10);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (30, '¿Qué habilidoso ex futbolista y emblema de River Plate, apodado ''El Burrito'', nació en Ledesma?', 'Ariel Ortega', 'Marcelo Gallardo', 'Pablo Aimar', 'Javier Saviola', 'MULTIPLE_CHOICE', 'DEPORTE', 10);

-- --- LA PAMPA (provincia_id: 11) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (31, 'La controversial operación militar del siglo XIX que ocupó estos territorios fue liderada por...', 'Julio Argentino Roca', 'Juan Manuel de Rosas', 'Bartolomé Mitre', 'Domingo F. Sarmiento', 'MULTIPLE_CHOICE', 'HISTORIA', 11);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (32, '¿Qué destacado cardiólogo, inventor del bypass coronario, ejerció la medicina rural durante 12 años en Jacinto Arauz?', 'René Favaloro', 'Bernardo Houssay', 'Luis Federico Leloir', 'Ramón Carrillo', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 11);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (33, '¿Qué inteligente mediocampista campeón del mundo en Qatar 2022 es oriundo de Santa Rosa?', 'Alexis Mac Allister', 'Leandro Paredes', 'Rodrigo De Paul', 'Enzo Fernández', 'MULTIPLE_CHOICE', 'DEPORTE', 11);

-- --- LA RIOJA (provincia_id: 12) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (34, '¿Cómo apodaban popularmente al temido y célebre caudillo federal Facundo Quiroga?', 'El Tigre de los Llanos', 'El Restaurador de las Leyes', 'El Supremo Entrerriano', 'El Chacho', 'MULTIPLE_CHOICE', 'HISTORIA', 12);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (35, '¿Qué festividad de Año Nuevo escenifica en las calles el encuentro pacífico entre españoles y diaguitas?', 'El Tinkunaco', 'La Chaya', 'El Desentierro', 'La Pachamama', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 12);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (36, '¿Qué ex jugador y múltiple director técnico campeón con River Plate, apodado ''El Pelado'', nació aquí?', 'Ramón Díaz', 'Matías Almeyda', 'Leonardo Astrada', 'Marcelo Gallardo', 'MULTIPLE_CHOICE', 'DEPORTE', 12);

-- --- MENDOZA (provincia_id: 13) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (37, '¿Cómo se llamó el campamento militar donde San Martín instruyó al ejército antes de cruzar la cordillera?', 'El Plumerillo', 'Yapeyú', 'San Lorenzo', 'Cancha Rayada', 'MULTIPLE_CHOICE', 'HISTORIA', 13);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (38, '¿En qué famoso y gran anfiteatro se celebra el imponente acto central de la Vendimia?', 'Frank Romero Day', 'Próspero Molina', 'Mario Alberto Kempes', 'Cocomarola', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 13);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (39, '¿Qué mediocampista histórico de la Selección Argentina, figura en el Mundial 2014, comenzó su carrera en Godoy Cruz?', 'Enzo Pérez', 'Javier Mascherano', 'Lucas Biglia', 'Fernando Gago', 'MULTIPLE_CHOICE', 'DEPORTE', 13);

-- --- MISIONES (provincia_id: 14) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (40, '¿A qué orden religiosa pertenecían las misiones que dieron nombre a la región durante el siglo XVII?', 'Jesuitas', 'Franciscanos', 'Dominicos', 'Agustinos', 'MULTIPLE_CHOICE', 'HISTORIA', 14);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (41, '¿Qué destacado escritor uruguayo se instaló en la selva local y se inspiró en ella para escribir ''Cuentos de la selva''?', 'Horacio Quiroga', 'Eduardo Galeano', 'Mario Benedetti', 'Juan Carlos Onetti', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 14);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (42, '¿Qué histórico arquero, subcampeón del mundo en Brasil 2014, nació en la ciudad de Bernardo de Irigoyen?', 'Sergio Romero', 'Franco Armani', 'Emiliano Martínez', 'Willy Caballero', 'MULTIPLE_CHOICE', 'DEPORTE', 14);

-- --- NEUQUÉN (provincia_id: 15) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (43, '¿Qué importante formación geológica rica en petróleo y gas no convencional transformó la matriz energética nacional?', 'Vaca Muerta', 'Cuenca Austral', 'Golfo San Jorge', 'Cerro Vanguardia', 'MULTIPLE_CHOICE', 'HISTORIA', 15);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (44, '¿Qué fósil de dinosaurio gigante, uno de los carnívoros más grandes del mundo, fue descubierto en este territorio?', 'Giganotosaurus', 'Tyrannosaurus rex', 'Argentinosaurus', 'Triceratops', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 15);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (45, '¿Qué lateral izquierdo de la selección nacional, campeón del mundo y apodado ''El Huevo'', nació en Zapala?', 'Marcos Acuña', 'Nahuel Molina', 'Gonzalo Montiel', 'Nicolás Tagliafico', 'MULTIPLE_CHOICE', 'DEPORTE', 15);

-- --- RÍO NEGRO (provincia_id: 16) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (46, '¿Qué presidente propuso en la década de 1980 mudar la Capital Federal a la ciudad de Viedma?', 'Raúl Alfonsín', 'Carlos Menem', 'Néstor Kirchner', 'Fernando de la Rúa', 'MULTIPLE_CHOICE', 'HISTORIA', 16);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (47, '¿Qué fruta es el símbolo económico por excelencia del Alto Valle y se exporta a nivel mundial?', 'La Manzana', 'El Limón', 'La Uva', 'El Arándano', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 16);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (48, '¿Qué famoso centro de deportes invernales e instalaciones de esquí está ubicado en San Carlos de Bariloche?', 'Cerro Catedral', 'Las Leñas', 'Cerro Castor', 'Chapelco', 'MULTIPLE_CHOICE', 'DEPORTE', 16);

-- --- SALTA (provincia_id: 17) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (49, '¿Con qué apodo temible eran conocidos los gauchos de caballería que frenaron los avances realistas en el norte?', 'Los Infernales', 'Los Colorados', 'Los Blandengues', 'Los Patricios', 'MULTIPLE_CHOICE', 'HISTORIA', 17);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (50, '¿Qué histórico conjunto folclórico, fundado en 1948, vistió como marca registrada el poncho tinto característico?', 'Los Chalchaleros', 'Los Nocheros', 'Los Fronterizos', 'Los Tucu Tucu', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 17);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (51, '¿Qué destacado piloto motociclista hizo historia al ganar dos ediciones del exigente Rally Dakar?', 'Kevin Benavides', 'Marcos Patronelli', 'Orlando Terranova', 'Lucio Álvarez', 'MULTIPLE_CHOICE', 'DEPORTE', 17);

-- --- SAN JUAN (provincia_id: 18) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (52, '¿Qué prócer de la educación y expresidente de la nación redactó su famosa obra sociológica ''Facundo''?', 'Domingo Faustino Sarmiento', 'Nicolás Avellaneda', 'Juan Bautista Alberdi', 'Bartolomé Mitre', 'MULTIPLE_CHOICE', 'HISTORIA', 18);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (53, '¿A qué figura o elemento vital de la naturaleza se le rinde homenaje en su fiesta nacional más importante?', 'Al Sol', 'A la Pachamama', 'A la Vendimia', 'Al Viento Zonda', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 18);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (54, '¿En qué deporte sobre ruedas los clubes de la región son potencia internacional acumulando campeonatos mundiales?', 'Hockey sobre patines', 'Patinaje artístico', 'Ciclismo de pista', 'Skateboarding', 'MULTIPLE_CHOICE', 'DEPORTE', 18);

-- --- SAN LUIS (provincia_id: 19) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (55, '¿Qué heroico granadero, nacido en la región, acompañó a Cabral para salvar la vida de San Martín en San Lorenzo?', 'Juan Bautista Baigorria', 'Antonio Ruiz (Falucho)', 'Pedro Ríos', 'Fray Luis Beltrán', 'MULTIPLE_CHOICE', 'HISTORIA', 19);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (56, '¿A qué localidad serrana famosa por su asombroso clima y purificación del aire se la conoce como ''el tercer microclima del mundo''?', 'Villa de Merlo', 'Potrero de los Funes', 'El Trapiche', 'La Carolina', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 19);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (57, '¿En torno a qué hermoso embalse se trazó un exigente circuito automovilístico de nivel internacional?', 'Potrero de los Funes', 'Lago San Roque', 'Embalse Río Tercero', 'Dique Los Molinos', 'MULTIPLE_CHOICE', 'DEPORTE', 19);

-- --- SANTA CRUZ (provincia_id: 20) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (58, '¿Bajo qué nombre popular e histórico se conoció a la feroz huelga obrera reprimida por el ejército en 1921?', 'La Patagonia Rebelde', 'La Semana Trágica', 'El Cordobazo', 'El Grito de Alcorta', 'MULTIPLE_CHOICE', 'HISTORIA', 20);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (59, '¿Qué asombroso patrimonio prehistorico alberga miles de impresiones rupestres en negativo de extremidades humanas?', 'Cueva de las Manos', 'Cerro Colorado', 'Ruinas de Quilmes', 'Piedra Pintada', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 20);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (60, '¿Qué ex boxeador y campeón mundial, recordado por su épica victoria frente a John David Jackson, nació en Caleta Olivia?', 'Jorge ''Locomotora'' Castro', 'Sergio ''Maravilla'' Martínez', 'Carlos Monzón', 'Marcos ''Chino'' Maidana', 'MULTIPLE_CHOICE', 'DEPORTE', 20);

-- --- SANTA FE (provincia_id: 21) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (61, '¿A orillas de qué río se enarboló e izó por primera vez la bandera creada por Manuel Belgrano en 1812?', 'Río Paraná', 'Río de la Plata', 'Río Uruguay', 'Río Salado', 'MULTIPLE_CHOICE', 'HISTORIA', 21);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (62, '¿Qué emblemática banda musical tropical popularizó a nivel masivo la canción ''El Bombón Asesino''?', 'Los Palmeras', 'Ráfaga', 'Amar Azul', 'La Nueva Luna', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 21);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (63, '¿Qué talentoso futbolista y múltiple ganador del Balón de Oro se formá en las infantiles del club Newell''s Old Boys?', 'Lionel Messi', 'Ángel Di María', 'Gabriel Batistuta', 'Maxi Rodríguez', 'MULTIPLE_CHOICE', 'DEPORTE', 21);

-- --- SANTIAGO DEL ESTERO (provincia_id: 22) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (64, 'Por ser el primer asentamiento poblacional español constante en el actual país, recibe tradicionalmente el título de...', 'Madre de Ciudades', 'La Docta', 'La Cuna de la Independencia', 'El Jardín de la República', 'MULTIPLE_CHOICE', 'HISTORIA', 22);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (65, '¿Qué instrumento de percusión, tallado a mano en troncos ahuecados, es el rey indiscutido de su música folclórica?', 'El bombo legüero', 'El cajón peruano', 'Las maracas', 'El bongó', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 22);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (66, '¿Qué moderno circuito internacional recibe año a año a la espectacular categoría mundial de MotoGP?', 'Termas de Río Hondo', 'Oscar y Juan Gálvez', 'Villicum', 'Cabalén', 'MULTIPLE_CHOICE', 'DEPORTE', 22);

-- --- TIERRA DEL FUEGO (provincia_id: 23) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (67, 'Hasta el año 1947, este inhóspito territorio albergó un temido establecimiento penitenciario conocido como...', 'El Presidio del Fin del Mundo', 'La Cárcel de Caseros', 'El Penal de Magdalena', 'La Isla del Diablo', 'MULTIPLE_CHOICE', 'HISTORIA', 23);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (68, '¿Qué pueblo originario nómade, experto en sobrevivir navegando en canoas por los gélidos canales, habitaba la zona?', 'Yámanas', 'Mapuches', 'Guaraníes', 'Diaguitas', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 23);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (69, '¿Qué centro invernal es el principal polo del esquí provincial y se promociona a nivel global como ''el más austral del mundo''?', 'Cerro Castor', 'Cerro Catedral', 'Las Leñas', 'Chapelco', 'MULTIPLE_CHOICE', 'DEPORTE', 23);

-- --- TUCUMÁN (provincia_id: 24) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (70, '¿En la casa de qué importante familia de la época se sesionó el Congreso que declaró la Independencia de 1816?', 'Francisca Bazán de Laguna', 'Familia Anchorena', 'Estancia El Plumerillo', 'Posta de Yatasto', 'MULTIPLE_CHOICE', 'HISTORIA', 24);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (71, '¿Por qué halagador título o apodo es tradicionalmente conocida esta pequeña pero fértil provincia norteña?', 'El Jardín de la República', 'Madre de Ciudades', 'Tierra del Sol', 'La Docta', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 24);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (72, '¿Qué histórico club de fútbol viste a rayas celestes y blancas y hace de local en el estadio ''Monumental José Fierro''?', 'Atlético Tucumán', 'San Martín de Tucumán', 'Central Norte', 'Gimnasia y Tiro', 'MULTIPLE_CHOICE', 'DEPORTE', 24);

INSERT INTO Comodin (id, nombre, descripcion, costo)
VALUES (1, 'DOBLE_CHANCE', 'Si fallas tu primera respuesta, el juego te otorga una segunda oportunidad inmediata.', 35)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), descripcion = VALUES(descripcion), costo = VALUES(costo);

INSERT INTO Comodin (id, nombre, descripcion, costo)
VALUES (2, 'ELIMINAR_2', 'Remueve dos opciones incorrectas de la pregunta actual dejando solo la correcta y una trampa.', 50)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), descripcion = VALUES(descripcion), costo = VALUES(costo);

INSERT INTO Comodin (id, nombre, descripcion, costo)
VALUES (3, 'PASAR_PREGUNTA', 'Salta la pregunta actual sin penalizaciones y te asigna una nueva tarjeta distinta.', 25)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), descripcion = VALUES(descripcion), costo = VALUES(costo);

INSERT IGNORE INTO paquetemonedas (titulo, cantidadCoins, precioArs, beneficioExtra) 
VALUES ('Bolsita de Coins', 200, 350.00, 'Ideal para arrancar');

INSERT IGNORE INTO paquetemonedas (titulo, cantidadCoins, precioArs, beneficioExtra) 
VALUES ('Cofre Táctico', 1000, 1500.00, '+1 Doble Chance GRATIS');

 */