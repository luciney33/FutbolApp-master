package org.example.dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.common.Constantes;
import org.example.domain.Equipo;
import org.example.domain.Jugador;
import org.example.domain.Usuario;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DaoFicheros {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                    LocalDate.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) ->
                    new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .setPrettyPrinting()
            .create();
    public static Set<Jugador> leerJugadores() {
        Set<Jugador> jugadores = new HashSet<>();
        File fichero = new File(Constantes.FICHERO_JUGADORES_JSON);
        if (!fichero.exists()) {
            System.out.println("No existe el fichero " + Constantes.FICHERO_JUGADORES_JSON);
            return jugadores;
        }
        try (FileReader reader = new FileReader(fichero)) {
            Type tipoSet = new TypeToken<Set<Jugador>>(){}.getType();
            jugadores = gson.fromJson(reader, tipoSet);
            if (jugadores == null) {
                jugadores = new HashSet<>();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return jugadores;
    }

    public static boolean guardarJugadores(Set<Jugador> jugadores) {
        try (PrintWriter pw = new PrintWriter(Constantes.FICHERO_JUGADORES_JSON)) {
            gson.toJson(jugadores, pw);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static Set<Equipo> leerEquipos() {
        Set<Equipo> equipos = new HashSet<>();
        File fichero = new File(Constantes.FICHERO_EQUIPOS_TXT);

        if (!fichero.exists()) {
            System.out.println("No existe el fichero " + Constantes.FICHERO_EQUIPOS_TXT);
            return equipos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    int id = Integer.parseInt(partes[0].trim());
                    String nombre = partes[1].trim();
                    String ciudad = partes[2].trim();
                    String estadio = partes[3].trim();
                    String entrenador = partes[4].trim();
                    equipos.add(new Equipo(id, nombre, ciudad, estadio, entrenador));
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo equipos: " + e.getMessage());
        }

        return equipos;
    }

    public static boolean guardarEquipos(Set<Equipo> equipos) {
        try (PrintWriter pw = new PrintWriter(Constantes.FICHERO_EQUIPOS_TXT)) {
            for (Equipo equipo : equipos) {
                pw.printf("%d;%s;%s;%s;%s%n",
                        equipo.getId(),
                        equipo.getNombre(),
                        equipo.getCiudad(),
                        equipo.getEstadio(),
                        equipo.getEntrenador()
                );
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error guardando equipos: " + e.getMessage());
            return false;
        }
    }

    public static void crearFicheroAdminPorDefecto() {
        File fichero = new File(Constantes.FICHERO_ADMI);
        if (!fichero.exists()) {
            List<Usuario> adminUnico = new ArrayList<>();
            adminUnico.add(new Usuario("admi", "1234"));
            try {
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fichero));
                os.writeObject(adminUnico);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<Usuario> leerAdmiBinario() {
        File fichero = new File(Constantes.FICHERO_ADMI);
        List<Usuario> admins = new ArrayList<>();
        if (!fichero.exists()) {
            return admins;
        }
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fichero));
            admins = (List<Usuario>) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return admins;
    }

    public static List<Usuario> leerUsuariosBinario() {
        List<Usuario> lista = new ArrayList<>();
        File fichero = new File(Constantes.FICHERO_USUARIOS);
        if (!fichero.exists()) {
            return lista;
        }
        try {
            ObjectInputStream os = new ObjectInputStream(new FileInputStream(fichero));
            lista = (List<Usuario>) os.readObject();
            os.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public static void escribirUsuariosBinario(List<Usuario> lista) {
        File fichero = new File(Constantes.FICHERO_USUARIOS);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fichero));
            os.writeObject(lista);
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
