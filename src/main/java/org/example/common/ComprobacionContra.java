package org.example.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComprobacionContra {
    public static boolean validarContra(String contra) {
        Pattern pat = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{6,}$");
        Matcher mat = pat.matcher(contra);
        return mat.find();
    }
}
