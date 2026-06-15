/*Importo las funciones*/
import { 
    obtenerColorProvincia,
    crearEstiloProvincia,
    hoverProvincia
 } from "../mapa_funciones.js";

 /*Tests: */
describe("Mapa Funciones", function() {

    /*Test 1: obtenerColorProvincia*/
    describe("obtenerColorProvincia", function() {

        /*Describo el 1er caso */
        it("debe devolver rojo", function() {

            /*mockeo*/
            const botonMock = {
                classList: {
                    contains: function(clase) {
                        return clase === 'provincia-rojo';
                    }
                }
            };

            /*Espero recibir rojo*/
            expect(obtenerColorProvincia(botonMock)).toBe('#dc2626');

        })

        /*Describo el 2do caso */
        it("debe devolver azul", function() {

            /*mockeo*/
            const botonMock = {
                classList: {
                    contains: function(clase) {
                        return clase === 'provincia-azul';
                    }
                }
            };

            /*Espero recibir azul*/
            expect(obtenerColorProvincia(botonMock)).toBe('#2563eb');

        })            
           
        /*Describo el 3er caso */
        it("debe devolver verde", function() {

            /*mockeo*/
            const botonMock = {
                classList: {
                    contains: function(clase) {
                        return clase === 'provincia-verde';
                    }
                }
            };

            /*Espero recibir verde*/
            expect(obtenerColorProvincia(botonMock)).toBe('#16a34a');

        })

        /*Describo el 4to caso */
        it("debe devolver el color neutral", function() {

            /*Espero recibir el color neutral*/
            expect(obtenerColorProvincia(null)).toBe('#e0d8b0');

        })

    });

    /*Test 2: crearEstiloProvincia*/
    describe("crearEstiloProvincia", function() {

        /*Defino el 1er caso */
        it("Debe crear el estilo con color rojo", function() {

            const color = '#dc2626';

            const estiloCreado = crearEstiloProvincia(color);

            expect(estiloCreado).toEqual({
                color: '#333',
                weight: 1,
                fillColor: '#dc2626',
                fillOpacity: 1
            })

        })

        /*Defino el 2do caso */
        it("Debe crear el estilo con color azul", function() {

            const color = '#2563eb';

            const estiloCreado = crearEstiloProvincia(color);

            expect(estiloCreado).toEqual({
                color: '#333',
                weight: 1,
                fillColor: '#2563eb',
                fillOpacity: 1
            })

        })

        /*Defino el 3er caso */
        it("Debe crear el estilo con color verde", function() {

            const color = '#16a34a';

            const estiloCreado = crearEstiloProvincia(color);

            expect(estiloCreado).toEqual({
                color: '#333',
                weight: 1,
                fillColor: '#16a34a',
                fillOpacity: 1
            })

        })

        /*Defino el 4to caso */
        it("Debe crear el estilo con color neutro", function() {

            const color = '#e0d8b0';

            const estiloCreado = crearEstiloProvincia(color);

            expect(estiloCreado).toEqual({
                color: '#333',
                weight: 1,
                fillColor: '#e0d8b0',
                fillOpacity: 1
            })

        })

    })

    /*Test 3: hoverProvincia*/
    describe("hoverProvincia", function() {
        
        /*Defino el caso*/
        it("Debe aumentar el grosor del borde de la provincia en 3", function() {

            /*Creo la variable*/
            let estiloAplicado = null;

            /*Creo el Mock*/
            const layerMock = {
                setStyle: function(estilo) {
                    estiloAplicado = estilo;
                }
            };

            /*Aplico el Mock*/
            hoverProvincia(layerMock);

            /*Lo que se espera*/
            expect(estiloAplicado).toEqual({
                weight: 3
            });

        })

    });

});