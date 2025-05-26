package org.example.common;

public class ComprobacionId {
    public static void comprobarId(int id) throws ExcepcionIdErroneo{
        if (id < 0) {
            throw new ExcepcionIdErroneo();
        }
    }
}
