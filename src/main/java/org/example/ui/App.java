package org.example.ui;

import org.example.common.ExcepcionIdErroneo;

public class App {
    public static void main(String[] args)  {
        try {
            new EntradaSalida().menuPrincipal();
        } catch (ExcepcionIdErroneo e) {
            throw new RuntimeException(e);
        }
    }
}

