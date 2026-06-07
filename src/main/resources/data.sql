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

-- --- CÃ“RDOBA (provincia_id: 6) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (6, 'CÃ³rdoba', 0);

-- --- CORRIENTES (provincia_id: 7) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (7, 'Corrientes', 0);

-- --- ENTRE RÃOS (provincia_id: 8) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (8, 'Entre RÃ­os', 0);

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

-- --- NEUQUÃ‰N (provincia_id: 15) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (15, 'NeuquÃ©n', 0);

-- --- RÃO NEGRO (provincia_id: 16) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (16, 'RÃ­o Negro', 0);

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

-- --- TUCUMÃN (provincia_id: 24) ---
INSERT IGNORE INTO Provincia (id, nombre, puntos) VALUES (24, 'TucumÃ¡n', 0);

-- ====================== PREGUNTAS ======================= --

-- --- BUENOS AIRES (provincia_id: 1) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (1, 'Â¿En quÃ© rÃ­o tuvo lugar la defensa soberana conocida como la Batalla de la Vuelta de Obligado?', 'RÃ­o ParanÃ¡', 'RÃ­o de la Plata', 'RÃ­o Salado', 'RÃ­o Uruguay', 'MULTIPLE_CHOICE', 'HISTORIA', 1);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (2, 'Â¿QuÃ© destacado autor escribiÃ³ el poema y obra cumbre de la literatura gauchesca ''MartÃ­n Fierro''?', 'JosÃ© HernÃ¡ndez', 'Jorge Luis Borges', 'Domingo F. Sarmiento', 'Julio CortÃ¡zar', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 1);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (3, 'Â¿De quÃ© ciudad es originario el ex tenista, campeÃ³n del US Open, Juan MartÃ­n del Potro?', 'Tandil', 'Mar del Plata', 'La Plata', 'BahÃ­a Blanca', 'MULTIPLE_CHOICE', 'DEPORTE', 1);

-- --- CABA (provincia_id: 2) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (4, 'Â¿En quÃ© aÃ±o se llevÃ³ a cabo la fundaciÃ³n definitiva de la ciudad por orden de Juan de Garay?', '1580', '1536', '1810', '1492', 'MULTIPLE_CHOICE', 'HISTORIA', 2);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (5, 'Â¿QuÃ© prestigioso teatro de Ã³pera mundialmente reconocido estÃ¡ ubicado sobre la Avenida 9 de Julio?', 'Teatro ColÃ³n', 'Teatro San MartÃ­n', 'Teatro Cervantes', 'Teatro Gran Rex', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 2);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (6, 'Â¿En quÃ© barrio porteÃ±o se encuentra ubicado el estadio conocido popularmente como ''La Bombonera''?', 'La Boca', 'NÃºÃ±ez', 'Liniers', 'Boedo', 'MULTIPLE_CHOICE', 'DEPORTE', 2);

-- --- CATAMARCA (provincia_id: 3) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (7, 'Â¿QuÃ© importante fraile y orador pronunciÃ³ el famoso SermÃ³n de la ConstituciÃ³n en 1853?', 'Fray Mamerto EsquiÃº', 'Cura Brochero', 'Fray Luis BeltrÃ¡n', 'San Francisco Solano', 'MULTIPLE_CHOICE', 'HISTORIA', 3);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (8, 'Â¿QuÃ© prenda de abrigo tradicional, tejida a mano, tiene su Fiesta Nacional cada mes de julio?', 'El Poncho', 'La Ruana', 'El Manto', 'El Chal', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 3);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (9, 'Â¿Por quÃ© inmensas montaÃ±as de arena pasaba el Rally Dakar destacando su gran dificultad?', 'Dunas de FiambalÃ¡', 'Dunas del Nihuil', 'MÃ©danos Blancos', 'Dunas de TatÃ³n', 'MULTIPLE_CHOICE', 'DEPORTE', 3);

-- --- CHACO (provincia_id: 4) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (10, 'Â¿QuÃ© trÃ¡gico hecho represivo sufrieron los pueblos originarios qom y moqoit en el aÃ±o 1924?', 'Masacre de NapalpÃ­', 'Semana TrÃ¡gica', 'Patagonia Rebelde', 'CampaÃ±a del Desierto', 'MULTIPLE_CHOICE', 'HISTORIA', 4);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (11, 'Â¿QuÃ© disciplina artÃ­stica interviene las calles de la capital mediante una Bienal Internacional?', 'Escultura', 'Muralismo', 'FotografÃ­a', 'Pintura al Ã³leo', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 4);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (12, 'Â¿QuÃ© equipo de fÃºtbol es el mÃ¡ximo y clÃ¡sico rival del Club AtlÃ©tico Sarmiento?', 'Chaco For Ever', 'Textil MandiyÃº', 'Boca Unidos', 'Crucero del Norte', 'MULTIPLE_CHOICE', 'DEPORTE', 4);

-- --- CHUBUT (provincia_id: 5) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (13, 'Â¿De quÃ© origen eran los inmigrantes colonos que fundaron ciudades como Trelew y Gaiman?', 'Galeses', 'Alemanes', 'Suizos', 'Rusos', 'MULTIPLE_CHOICE', 'HISTORIA', 5);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (14, 'Â¿QuÃ© mamÃ­fero marino es la principal atracciÃ³n turÃ­stica de la PenÃ­nsula ValdÃ©s?', 'Ballena Franca Austral', 'Orca', 'Lobo Marino', 'DelfÃ­n Rosado', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 5);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (15, 'Â¿QuÃ© boxeador nacido en Trelew, apodado ''El HuracÃ¡n'', fue un histÃ³rico campeÃ³n mundial supermosca?', 'Omar NarvÃ¡ez', 'Sergio MartÃ­nez', 'Marcos Maidana', 'Jorge Castro', 'MULTIPLE_CHOICE', 'DEPORTE', 5);

-- --- CÃ“RDOBA (provincia_id: 6) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (16, 'Â¿QuÃ© movimiento impulsado por estudiantes en 1918 transformÃ³ los estatutos universitarios de toda AmÃ©rica?', 'Reforma Universitaria', 'Cordobazo', 'Grito de Alcorta', 'Noche de los LÃ¡pices', 'MULTIPLE_CHOICE', 'HISTORIA', 6);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (17, 'Â¿QuÃ© ritmo musical popular e identitario fue popularizado inicialmente por Leonor Marzano?', 'Cuarteto', 'ChamamÃ©', 'Cumbia Villera', 'Chacarera', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 6);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (18, 'Â¿QuÃ© basquetbolista de la GeneraciÃ³n Dorada, ex campeÃ³n de la NBA, naciÃ³ en Las Varillas?', 'Fabricio Oberto', 'Emanuel GinÃ³bili', 'Luis Scola', 'Carlos Delfino', 'MULTIPLE_CHOICE', 'DEPORTE', 6);

-- --- CORRIENTES (provincia_id: 7) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (19, 'Â¿En quÃ© histÃ³rica localizaciÃ³n ribereÃ±a naciÃ³ el general JosÃ© de San MartÃ­n?', 'YapeyÃº', 'San Lorenzo', 'Paso de los Libres', 'ItuzaingÃ³', 'MULTIPLE_CHOICE', 'HISTORIA', 7);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (20, 'Â¿QuÃ© figura mÃ­tica del litoral es venerada cada 8 de enero con santuarios y banderas rojas?', 'Gauchito Gil', 'Difunta Correa', 'San La Muerte', 'Virgen de ItatÃ­', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 7);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (21, 'Â¿QuÃ© jugador de fÃºtbol, apodado ''El Pepe'', es el goleador histÃ³rico del club LanÃºs?', 'JosÃ© Sand', 'JosÃ© Sosa', 'JosÃ© Chatruc', 'JosÃ© Luis CalderÃ³n', 'MULTIPLE_CHOICE', 'DEPORTE', 7);

-- --- ENTRE RÃOS (provincia_id: 8) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (22, 'Â¿QuÃ© militar e influyente caudillo mandÃ³ a construir la suntuosa residencia llamada Palacio San JosÃ©?', 'Justo JosÃ© de Urquiza', 'Francisco RamÃ­rez', 'Juan Manuel de Rosas', 'Facundo Quiroga', 'MULTIPLE_CHOICE', 'HISTORIA', 8);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (23, 'Â¿En quÃ© ciudad se realiza anualmente el Carnaval del PaÃ­s, el evento a cielo abierto mÃ¡s grande de Argentina?', 'GualeguaychÃº', 'ParanÃ¡', 'Concordia', 'ColÃ³n', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 8);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (24, 'Â¿QuÃ© aguerrido defensor central de la SelecciÃ³n Argentina, campeÃ³n del mundo en 2022, naciÃ³ en Gualeguay?', 'Lisandro MartÃ­nez', 'Cristian Romero', 'NicolÃ¡s Otamendi', 'Marcos AcuÃ±a', 'MULTIPLE_CHOICE', 'DEPORTE', 8);

-- --- FORMOSA (provincia_id: 9) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (25, 'En 1955 dejÃ³ de ser formalmente un ''Territorio Nacional''. Â¿QuÃ© presidente firmÃ³ su provincializaciÃ³n?', 'Juan Domingo PerÃ³n', 'Arturo Frondizi', 'Julio Argentino Roca', 'HipÃ³lito Yrigoyen', 'MULTIPLE_CHOICE', 'HISTORIA', 9);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (26, 'Â¿QuÃ© peculiar instrumento de cuerda Ãºnica, fabricado tradicionalmente con una lata, es tÃ­pico del pueblo qom?', 'Nvike', 'Siku', 'Charango', 'Erke', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 9);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (27, 'Â¿QuÃ© equipo de bÃ¡squet representa habitualmente a la regiÃ³n en la primera divisiÃ³n de la Liga Nacional?', 'La UniÃ³n', 'Regatas', 'San MartÃ­n', 'Quimsa', 'MULTIPLE_CHOICE', 'DEPORTE', 9);

-- --- JUJUY (provincia_id: 10) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (28, 'Â¿QuÃ© prÃ³cer liderÃ³ el gran Ã©xodo de poblaciÃ³n y ordenÃ³ quemar las tierras en 1812 para dejar sin recursos al enemigo?', 'Manuel Belgrano', 'JosÃ© de San MartÃ­n', 'MartÃ­n Miguel de GÃ¼emes', 'Juan JosÃ© Castelli', 'MULTIPLE_CHOICE', 'HISTORIA', 10);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (29, 'Durante el tradicional Carnaval de la regiÃ³n, la celebraciÃ³n oficial inicia cuando se desentierra a...', 'El Diablo (Pujllay)', 'La Pachamama', 'El Ekeko', 'El Coquena', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 10);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (30, 'Â¿QuÃ© habilidoso ex futbolista y emblema de River Plate, apodado ''El Burrito'', naciÃ³ en Ledesma?', 'Ariel Ortega', 'Marcelo Gallardo', 'Pablo Aimar', 'Javier Saviola', 'MULTIPLE_CHOICE', 'DEPORTE', 10);

-- --- LA PAMPA (provincia_id: 11) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (31, 'La controversial operaciÃ³n militar del siglo XIX que ocupÃ³ estos territorios fue liderada por...', 'Julio Argentino Roca', 'Juan Manuel de Rosas', 'BartolomÃ© Mitre', 'Domingo F. Sarmiento', 'MULTIPLE_CHOICE', 'HISTORIA', 11);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (32, 'Â¿QuÃ© destacado cardiÃ³logo, inventor del bypass coronario, ejerciÃ³ la medicina rural durante 12 aÃ±os en Jacinto Arauz?', 'RenÃ© Favaloro', 'Bernardo Houssay', 'Luis Federico Leloir', 'RamÃ³n Carrillo', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 11);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (33, 'Â¿QuÃ© inteligente mediocampista campeÃ³n del mundo en Qatar 2022 es oriundo de Santa Rosa?', 'Alexis Mac Allister', 'Leandro Paredes', 'Rodrigo De Paul', 'Enzo FernÃ¡ndez', 'MULTIPLE_CHOICE', 'DEPORTE', 11);

-- --- LA RIOJA (provincia_id: 12) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (34, 'Â¿CÃ³mo apodaban popularmente al temido y cÃ©lebre caudillo federal Facundo Quiroga?', 'El Tigre de los Llanos', 'El Restaurador de las Leyes', 'El Supremo Entrerriano', 'El Chacho', 'MULTIPLE_CHOICE', 'HISTORIA', 12);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (35, 'Â¿QuÃ© festividad de AÃ±o Nuevo escenifica en las calles el encuentro pacÃ­fico entre espaÃ±oles y diaguitas?', 'El Tinkunaco', 'La Chaya', 'El Desentierro', 'La Pachamama', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 12);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (36, 'Â¿QuÃ© ex jugador y mÃºltiple director tÃ©cnico campeÃ³n con River Plate, apodado ''El Pelado'', naciÃ³ aquÃ­?', 'RamÃ³n DÃ­az', 'MatÃ­as Almeyda', 'Leonardo Astrada', 'Marcelo Gallardo', 'MULTIPLE_CHOICE', 'DEPORTE', 12);

-- --- MENDOZA (provincia_id: 13) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (37, 'Â¿CÃ³mo se llamÃ³ el campamento militar donde San MartÃ­n instruyÃ³ al ejÃ©rcito antes de cruzar la cordillera?', 'El Plumerillo', 'YapeyÃº', 'San Lorenzo', 'Cancha Rayada', 'MULTIPLE_CHOICE', 'HISTORIA', 13);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (38, 'Â¿En quÃ© famoso y gran anfiteatro se celebra el imponente acto central de la Vendimia?', 'Frank Romero Day', 'PrÃ³spero Molina', 'Mario Alberto Kempes', 'Cocomarola', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 13);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (39, 'Â¿QuÃ© mediocampista histÃ³rico de la SelecciÃ³n Argentina, figura en el Mundial 2014, comenzÃ³ su carrera en Godoy Cruz?', 'Enzo PÃ©rez', 'Javier Mascherano', 'Lucas Biglia', 'Fernando Gago', 'MULTIPLE_CHOICE', 'DEPORTE', 13);

-- --- MISIONES (provincia_id: 14) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (40, 'Â¿A quÃ© orden religiosa pertenecÃ­an las misiones que dieron nombre a la regiÃ³n durante el siglo XVII?', 'Jesuitas', 'Franciscanos', 'Dominicos', 'Agustinos', 'MULTIPLE_CHOICE', 'HISTORIA', 14);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (41, 'Â¿QuÃ© destacado escritor uruguayo se instalÃ³ en la selva local y se inspirÃ³ en ella para escribir ''Cuentos de la selva''?', 'Horacio Quiroga', 'Eduardo Galeano', 'Mario Benedetti', 'Juan Carlos Onetti', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 14);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (42, 'Â¿QuÃ© histÃ³rico arquero, subcampeÃ³n del mundo en Brasil 2014, naciÃ³ en la ciudad de Bernardo de Irigoyen?', 'Sergio Romero', 'Franco Armani', 'Emiliano MartÃ­nez', 'Willy Caballero', 'MULTIPLE_CHOICE', 'DEPORTE', 14);

-- --- NEUQUÃ‰N (provincia_id: 15) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (43, 'Â¿QuÃ© importante formaciÃ³n geolÃ³gica rica en petrÃ³leo y gas no convencional transformÃ³ la matriz energÃ©tica nacional?', 'Vaca Muerta', 'Cuenca Austral', 'Golfo San Jorge', 'Cerro Vanguardia', 'MULTIPLE_CHOICE', 'HISTORIA', 15);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (44, 'Â¿QuÃ© fÃ³sil de dinosaurio gigante, uno de los carnÃ­voros mÃ¡s grandes del mundo, fue descubierto en este territorio?', 'Giganotosaurus', 'Tyrannosaurus rex', 'Argentinosaurus', 'Triceratops', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 15);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (45, 'Â¿QuÃ© lateral izquierdo de la selecciÃ³n nacional, campeÃ³n del mundo y apodado ''El Huevo'', naciÃ³ en Zapala?', 'Marcos AcuÃ±a', 'Nahuel Molina', 'Gonzalo Montiel', 'NicolÃ¡s Tagliafico', 'MULTIPLE_CHOICE', 'DEPORTE', 15);

-- --- RÃO NEGRO (provincia_id: 16) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (46, 'Â¿QuÃ© presidente propuso en la dÃ©cada de 1980 mudar la Capital Federal a la ciudad de Viedma?', 'RaÃºl AlfonsÃ­n', 'Carlos Menem', 'NÃ©stor Kirchner', 'Fernando de la RÃºa', 'MULTIPLE_CHOICE', 'HISTORIA', 16);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (47, 'Â¿QuÃ© fruta es el sÃ­mbolo econÃ³mico por excelencia del Alto Valle y se exporta a nivel mundial?', 'La Manzana', 'El LimÃ³n', 'La Uva', 'El ArÃ¡ndano', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 16);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (48, 'Â¿QuÃ© famoso centro de deportes invernales e instalaciones de esquÃ­ estÃ¡ ubicado en San Carlos de Bariloche?', 'Cerro Catedral', 'Las LeÃ±as', 'Cerro Castor', 'Chapelco', 'MULTIPLE_CHOICE', 'DEPORTE', 16);

-- --- SALTA (provincia_id: 17) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (49, 'Â¿Con quÃ© apodo temible eran conocidos los gauchos de caballerÃ­a que frenaron los avances realistas en el norte?', 'Los Infernales', 'Los Colorados', 'Los Blandengues', 'Los Patricios', 'MULTIPLE_CHOICE', 'HISTORIA', 17);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (50, 'Â¿QuÃ© histÃ³rico conjunto folclÃ³rico, fundado en 1948, vistiÃ³ como marca registrada el poncho tinto caracterÃ­stico?', 'Los Chalchaleros', 'Los Nocheros', 'Los Fronterizos', 'Los Tucu Tucu', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 17);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (51, 'Â¿QuÃ© destacado piloto motociclista hizo historia al ganar dos ediciones del exigente Rally Dakar?', 'Kevin Benavides', 'Marcos Patronelli', 'Orlando Terranova', 'Lucio Ãlvarez', 'MULTIPLE_CHOICE', 'DEPORTE', 17);

-- --- SAN JUAN (provincia_id: 18) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (52, 'Â¿QuÃ© prÃ³cer de la educaciÃ³n y expresidente de la naciÃ³n redactÃ³ su famosa obra sociolÃ³gica ''Facundo''?', 'Domingo Faustino Sarmiento', 'NicolÃ¡s Avellaneda', 'Juan Bautista Alberdi', 'BartolomÃ© Mitre', 'MULTIPLE_CHOICE', 'HISTORIA', 18);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (53, 'Â¿A quÃ© figura o elemento vital de la naturaleza se le rinde homenaje en su fiesta nacional mÃ¡s importante?', 'Al Sol', 'A la Pachamama', 'A la Vendimia', 'Al Viento Zonda', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 18);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (54, 'Â¿En quÃ© deporte sobre ruedas los clubes de la regiÃ³n son potencia internacional acumulando campeonatos mundiales?', 'Hockey sobre patines', 'Patinaje artÃ­stico', 'Ciclismo de pista', 'Skateboarding', 'MULTIPLE_CHOICE', 'DEPORTE', 18);

-- --- SAN LUIS (provincia_id: 19) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (55, 'Â¿QuÃ© heroico granadero, nacido en la regiÃ³n, acompaÃ±Ã³ a Cabral para salvar la vida de San MartÃ­n en San Lorenzo?', 'Juan Bautista Baigorria', 'Antonio Ruiz (Falucho)', 'Pedro RÃ­os', 'Fray Luis BeltrÃ¡n', 'MULTIPLE_CHOICE', 'HISTORIA', 19);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (56, 'Â¿A quÃ© localidad serrana famosa por su asombroso clima y purificaciÃ³n del aire se la conoce como ''el tercer microclima del mundo''?', 'Villa de Merlo', 'Potrero de los Funes', 'El Trapiche', 'La Carolina', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 19);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (57, 'Â¿En torno a quÃ© hermoso embalse se trazÃ³ un exigente circuito automovilÃ­stico de nivel internacional?', 'Potrero de los Funes', 'Lago San Roque', 'Embalse RÃ­o Tercero', 'Dique Los Molinos', 'MULTIPLE_CHOICE', 'DEPORTE', 19);

-- --- SANTA CRUZ (provincia_id: 20) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (58, 'Â¿Bajo quÃ© nombre popular e histÃ³rico se conociÃ³ a la feroz huelga obrera reprimida por el ejÃ©rcito en 1921?', 'La Patagonia Rebelde', 'La Semana TrÃ¡gica', 'El Cordobazo', 'El Grito de Alcorta', 'MULTIPLE_CHOICE', 'HISTORIA', 20);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (59, 'Â¿QuÃ© asombroso patrimonio prehistorico alberga miles de impresiones rupestres en negativo de extremidades humanas?', 'Cueva de las Manos', 'Cerro Colorado', 'Ruinas de Quilmes', 'Piedra Pintada', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 20);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (60, 'Â¿QuÃ© ex boxeador y campeÃ³n mundial, recordado por su Ã©pica victoria frente a John David Jackson, naciÃ³ en Caleta Olivia?', 'Jorge ''Locomotora'' Castro', 'Sergio ''Maravilla'' MartÃ­nez', 'Carlos MonzÃ³n', 'Marcos ''Chino'' Maidana', 'MULTIPLE_CHOICE', 'DEPORTE', 20);

-- --- SANTA FE (provincia_id: 21) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (61, 'Â¿A orillas de quÃ© rÃ­o se enarbolÃ³ e izÃ³ por primera vez la bandera creada por Manuel Belgrano en 1812?', 'RÃ­o ParanÃ¡', 'RÃ­o de la Plata', 'RÃ­o Uruguay', 'RÃ­o Salado', 'MULTIPLE_CHOICE', 'HISTORIA', 21);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (62, 'Â¿QuÃ© emblemÃ¡tica banda musical tropical popularizÃ³ a nivel masivo la canciÃ³n ''El BombÃ³n Asesino''?', 'Los Palmeras', 'RÃ¡faga', 'Amar Azul', 'La Nueva Luna', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 21);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (63, 'Â¿QuÃ© talentoso futbolista y mÃºltiple ganador del BalÃ³n de Oro se formÃ¡ en las infantiles del club Newell''s Old Boys?', 'Lionel Messi', 'Ãngel Di MarÃ­a', 'Gabriel Batistuta', 'Maxi RodrÃ­guez', 'MULTIPLE_CHOICE', 'DEPORTE', 21);

-- --- SANTIAGO DEL ESTERO (provincia_id: 22) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (64, 'Por ser el primer asentamiento poblacional espaÃ±ol constante en el actual paÃ­s, recibe tradicionalmente el tÃ­tulo de...', 'Madre de Ciudades', 'La Docta', 'La Cuna de la Independencia', 'El JardÃ­n de la RepÃºblica', 'MULTIPLE_CHOICE', 'HISTORIA', 22);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (65, 'Â¿QuÃ© instrumento de percusiÃ³n, tallado a mano en troncos ahuecados, es el rey indiscutido de su mÃºsica folclÃ³rica?', 'El bombo legÃ¼ero', 'El cajÃ³n peruano', 'Las maracas', 'El bongÃ³', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 22);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (66, 'Â¿QuÃ© moderno circuito internacional recibe aÃ±o a aÃ±o a la espectacular categorÃ­a mundial de MotoGP?', 'Termas de RÃ­o Hondo', 'Oscar y Juan GÃ¡lvez', 'Villicum', 'CabalÃ©n', 'MULTIPLE_CHOICE', 'DEPORTE', 22);

-- --- TIERRA DEL FUEGO (provincia_id: 23) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (67, 'Hasta el aÃ±o 1947, este inhÃ³spito territorio albergÃ³ un temido establecimiento penitenciario conocido como...', 'El Presidio del Fin del Mundo', 'La CÃ¡rcel de Caseros', 'El Penal de Magdalena', 'La Isla del Diablo', 'MULTIPLE_CHOICE', 'HISTORIA', 23);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (68, 'Â¿QuÃ© pueblo originario nÃ³made, experto en sobrevivir navegando en canoas por los gÃ©lidos canales, habitaba la zona?', 'YÃ¡manas', 'Mapuches', 'GuaranÃ­es', 'Diaguitas', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 23);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (69, 'Â¿QuÃ© centro invernal es el principal polo del esquÃ­ provincial y se promociona a nivel global como ''el mÃ¡s austral del mundo''?', 'Cerro Castor', 'Cerro Catedral', 'Las LeÃ±as', 'Chapelco', 'MULTIPLE_CHOICE', 'DEPORTE', 23);

-- --- TUCUMÃN (provincia_id: 24) ---
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (70, 'Â¿En la casa de quÃ© importante familia de la Ã©poca se sesionÃ³ el Congreso que declarÃ³ la Independencia de 1816?', 'Francisca BazÃ¡n de Laguna', 'Familia Anchorena', 'Estancia El Plumerillo', 'Posta de Yatasto', 'MULTIPLE_CHOICE', 'HISTORIA', 24);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (71, 'Â¿Por quÃ© halagador tÃ­tulo o apodo es tradicionalmente conocida esta pequeÃ±a pero fÃ©rtil provincia norteÃ±a?', 'El JardÃ­n de la RepÃºblica', 'Madre de Ciudades', 'Tierra del Sol', 'La Docta', 'MULTIPLE_CHOICE', 'CULTURA_GENERAL', 24);
INSERT IGNORE INTO Pregunta (id, enunciado, respuesta_correcta, opcion_incorrecta_uno, opcion_incorrecta_dos, opcion_incorrecta_tres, tipo_pregunta, categoria_pregunta, provincia_id) VALUES (72, 'Â¿QuÃ© histÃ³rico club de fÃºtbol viste a rayas celestes y blancas y hace de local en el estadio ''Monumental JosÃ© Fierro''?', 'AtlÃ©tico TucumÃ¡n', 'San MartÃ­n de TucumÃ¡n', 'Central Norte', 'Gimnasia y Tiro', 'MULTIPLE_CHOICE', 'DEPORTE', 24);

