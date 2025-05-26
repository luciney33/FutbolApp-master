package org.example.common;

public class ExcepcionIdErroneo extends Exception {
    public ExcepcionIdErroneo() {
        super("El ID del jugador no puede ser negativo");
    }
}
