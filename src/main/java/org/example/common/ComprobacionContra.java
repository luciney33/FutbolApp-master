package org.example.common;

public class ComprobacionContra {
    public static boolean validarContra(String contra) {
        return contra.matches("^(?=.*[A-Z])(?=.*\\d).{6,}$");
    }
}
