package org.example.ui;

import org.example.common.*;
import org.example.dao.DaoFicheros;
import org.example.domain.DatosAleatorios;
import org.example.domain.Equipo;
import org.example.domain.Jugador;
import org.example.domain.Usuario;
import org.example.service.GestionEquipo;
import org.example.service.GestionEquipoImplementacion;
import org.example.service.GestionJugador;
import org.example.service.GestionJugadorImplementacion;

import javax.swing.plaf.IconUIResource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EntradaSalida {
    private static GestionJugador gestionJugador = new GestionJugadorImplementacion();
    private static GestionEquipo gestionEquipo = new GestionEquipoImplementacion();
    private static DaoFicheros daoFicheros = new DaoFicheros();
    private static List<Usuario> usuariosRegistrados = daoFicheros.leerUsuariosBinario();
    private static DatosAleatorios datosAleatorios = new DatosAleatorios();
    private static Scanner sc = new Scanner(System.in);

    public EntradaSalida() {
        DaoFicheros.crearFicheroAdminPorDefecto();
    }

    private static void mostrarEstadisticasJugador() throws ExcepcionIdErroneo {
        int id = leerEntero("Introduce el ID del jugador para ver estadísticas:");
        String estadisticas = gestionJugador.mostrarEstadisticasJugador(id);
        mostrarMensaje(estadisticas);
    }
    private static int leerEntero(String mensaje) {
        int numero = -1;
        boolean valido = false;
        do {
            System.out.print(mensaje + " ");
            try {
                numero = sc.nextInt();
                valido = true;
            } catch (InputMismatchException e) {
                mostrarError(Constantes.OPCION_INVALIDA);
                sc.nextLine();
            }
        } while (!valido);
        return numero;
    }

    private static void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private static void mostrarError(String mensaje) {
        System.out.println(mensaje);
    }

    private static void mostrarSeparador(String mensaje) {
        System.out.println("\n" + mensaje);
    }

    private static void registrarUsuario(){
        mostrarMensaje(Constantes.USU);
        String nombreUsuario = sc.nextLine();
        boolean existe = usuariosRegistrados.stream().anyMatch(u -> u.getNombreUsu().equals(nombreUsuario));
        if (!existe) {
            mostrarMensaje(Constantes.CONTRA);
            String contra = sc.nextLine();
            Usuario nuevo = new Usuario(nombreUsuario, contra);
            usuariosRegistrados.add(nuevo);
            DaoFicheros.escribirUsuariosBinario(usuariosRegistrados);
            mostrarMensaje(Constantes.REGISTRADO);
        } else {
            mostrarError(Constantes.ERROR_INICIOSESION2);
        }
    }

    private static Usuario iniciarSesion(){
        mostrarMensaje(Constantes.USU);
        String nommbreUsu = sc.nextLine();
        mostrarMensaje(Constantes.CONTRA);
        String contra = sc.nextLine();
        for (int i = 0; i < usuariosRegistrados.size(); i++) {
           Usuario u = usuariosRegistrados.get(i);
           if (u.getNombreUsu().equals(nommbreUsu) && u.getContraseya().equals(contra)){
               mostrarMensaje(Constantes.INICIOSESION + nommbreUsu);
               return u;
           }
        }
        mostrarError(Constantes.ERROR_INICIOSESION);
        return null;

    }
    private static boolean loginAdmin() {
        List<Usuario> admins = DaoFicheros.leerAdmiBinario();
        boolean coincide = false;

        if (admins.isEmpty()) {
            mostrarError(Constantes.ERROR_ADMI);
        } else {
            mostrarMensaje(Constantes.USU);
            String nombre = sc.nextLine();
            mostrarMensaje(Constantes.CONTRA);
            String contra = sc.nextLine();

            Usuario admin = admins.get(0); //admi unico

            if (admin.getNombreUsu().equals(nombre) && admin.getContraseya().equals(contra)) {
                mostrarMensaje(Constantes.INICIOSESION);
                coincide = true;
            } else {
                mostrarError(Constantes.ERROR_INICIOSESION);
            }
        }

        return coincide;
    }


    public static void menuPrincipal() throws ExcepcionIdErroneo {
        boolean salir = false;
        mostrarMensaje(Constantes.BIENVENIDA);
        while (!salir) {
            mostrarSeparador(Constantes.SEPARADOR);
            mostrarMensaje(Constantes.MENU_ROL);
            int opc = leerEntero(Constantes.ELEGIR_OPCION);
            sc.nextLine();

            switch (opc) {
                case 1 :
                    if (loginAdmin()) {
                        try {
                            menuAdministrador();
                        } catch (ExcepcionIdErroneo e) {
                            mostrarError("Error en menu admi: " + e.getMessage());
                        }
                    }
                    break;
                case 2 :
                    menuUsuario();
                    break;
                case 0 :
                    salir = true;
                    mostrarMensaje(Constantes.SALIR_APP);
                    break;
                default :
                    mostrarError(Constantes.OPCION_INVALIDA);
                    break;
            }
        }
    }

    private static void menuAdministrador() throws ExcepcionIdErroneo {
        boolean volver = false;
        while (!volver) {
            mostrarSeparador(Constantes.SEPARADOR);
            mostrarMensaje(Constantes.MENU_ADMIN1);
            int opcion = leerEntero(Constantes.ELEGIR_OPCION);
            sc.nextLine();

            switch (opcion) {
                case 1:
                    menuGestionJugadores();
                    break;
                case 2:
                    menuGestionEquipos();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    mostrarError(Constantes.OPCION_INVALIDA);
                    break;
            }
        }
    }
    private static void menuGestionJugadores() throws ExcepcionIdErroneo {
        boolean volver = false;
        while (!volver) {
            mostrarSeparador(Constantes.SEPARADOR);
            mostrarMensaje(Constantes.MENU_ADMIN2);
            int opcion = leerEntero(Constantes.ELEGIR_OPCION);
            sc.nextLine();

            switch (opcion) {
                case 1:
                    Jugador nuevo = datosAleatorios.crearJugadores();
                    gestionJugador.insertarJugador(nuevo);
                    mostrarMensaje(Constantes.JUGADOR_INSERTADO);
                    break;

                case 2:
                    gestionJugador.listarJugadores().forEach(j -> mostrarMensaje(j.toString()));
                    break;
                case 3:
                    int id = leerEntero(Constantes.PIDE_ID_JUGADOR);
                    try {
                        ComprobacionId.comprobarId(id);
                    } catch (ExcepcionIdErroneo e) {
                        mostrarError(Constantes.ID_INVALIDO);
                        System.out.println("Error capturado: " + e.getMessage());
                        break;
                    }
                    gestionJugador.buscarPorId(id).ifPresentOrElse(j -> {
                        int goles = leerEntero(Constantes.PIDE_GOLES);
                        try {
                            j.incrementarGoles(goles);
                            mostrarMensaje(Constantes.GOLES_ACTUALIZADOS);
                        } catch (ExcepcionGoles e) {
                            mostrarError(e.getMessage());
                        }
                    }, () -> mostrarError(Constantes.JUGADOR_NO_ENCONTRADO));
                    break;

                case 4:
                    insertarJugadorManual();
                    break;

                case 5:
                    int idAsis = leerEntero(Constantes.PIDE_ID_JUGADOR);
                    try {
                        ComprobacionId.comprobarId(idAsis);
                    } catch (ExcepcionIdErroneo e) {
                        mostrarError(Constantes.ID_INVALIDO);
                        System.out.println("Error capturado: " + e.getMessage());
                        break;
                    }
                    gestionJugador.buscarPorId(idAsis).ifPresentOrElse(j -> {
                        int asistencias = leerEntero(Constantes.PIDE_ASISTENCIAS);
                        try {
                            j.incrementarAsistencias(asistencias);
                            mostrarMensaje(Constantes.ASISTENCIAS_ACTUALIZADAS);
                        } catch (ExcepcionAsistencias e) {
                            mostrarError(e.getMessage());
                        }
                    }, () -> mostrarError(Constantes.JUGADOR_NO_ENCONTRADO));
                    break;

                case 6:
                    int idEliminar = leerEntero(Constantes.PIDE_ID_JUGADOR);
                    try {
                        ComprobacionId.comprobarId(idEliminar);
                    } catch (ExcepcionIdErroneo e) {
                        mostrarError(Constantes.ID_INVALIDO);
                        break;
                    }
                    gestionJugador.buscarPorId(idEliminar).ifPresentOrElse(j -> {
                        boolean eliminado = gestionJugador.eliminarJugador(j);
                        if (eliminado) {
                            mostrarMensaje("Jugador eliminado correctamente.");
                        } else {
                            mostrarError("No se pudo eliminar el jugador.");
                        }
                    }, () -> mostrarError(Constantes.JUGADOR_NO_ENCONTRADO));
                    break;
                case 0:
                    volver = true;
                    break;

                default:
                    mostrarError(Constantes.OPCION_INVALIDA);
            }
        }
    }
    private static void menuGestionEquipos() throws ExcepcionIdErroneo {
        boolean volver = false;
        while (!volver) {
            mostrarSeparador(Constantes.SEPARADOR);
            mostrarMensaje(Constantes.MENU_ADMIN3);
            int opcion = leerEntero(Constantes.ELEGIR_OPCION);
            sc.nextLine();

            switch (opcion) {
                case 1:
                    insertarEquipoManual();
                    break;

                case 2:
                    Set<Equipo> equiposOrdenados = gestionEquipo.listarEquiposOrdenadosPorNombre();
                    if (!equiposOrdenados.isEmpty()) {
                        equiposOrdenados.forEach(e -> mostrarMensaje(e.toString()));
                    } else {
                        mostrarError("No hay equipos registrados.");
                    }
                    break;

                case 3: {
                    int id = leerEntero("Introduce ID del equipo a modificar:");
                    try {
                        ComprobacionId.comprobarId(id);
                    } catch (ExcepcionIdErroneo e) {
                        mostrarError("ID inválido.");
                        break;
                    }
                    System.out.println("Introduce nuevo entrenador:");
                    sc.nextLine(); // limpiar buffer
                    String nuevoEntrenador = sc.nextLine();

                    gestionEquipo.modificarEntrenador(id, nuevoEntrenador);
                    break;
                }

                case 4: {
                    System.out.println("Introduce ciudad para buscar equipos:");
                    sc.nextLine(); // limpiar buffer
                    String ciudad = sc.nextLine();
                    Optional<Equipo> equipo = gestionEquipo.buscarPorCiudad(ciudad);
                    equipo.ifPresentOrElse(
                            e -> mostrarMensaje(e.toString()),
                            () -> mostrarError("No se encontró equipo en la ciudad: " + ciudad)
                    );
                    break;
                }

                case 5: {
                    int idEliminar = leerEntero("Introduce ID del equipo a eliminar:");
                    try {
                        ComprobacionId.comprobarId(idEliminar);
                    } catch (ExcepcionIdErroneo e) {
                        mostrarError("ID inválido.");
                        break;
                    }
                    Optional<Equipo> equipoEliminar = gestionEquipo.buscarPorId(idEliminar);
                    equipoEliminar.ifPresentOrElse(equipo -> {
                        boolean eliminado = gestionEquipo.eliminarEquipo(equipo);
                        if (eliminado) {
                            mostrarMensaje("Equipo eliminado correctamente.");
                        } else {
                            mostrarError("No se pudo eliminar el equipo.");
                        }
                    }, () -> mostrarError("Equipo no encontrado."));
                    break;
                }

                case 0:
                    volver = true;
                    break;

                default:
                    mostrarError(Constantes.OPCION_INVALIDA);
            }
        }
    }
    private static void menuUsuario() throws ExcepcionIdErroneo {
        boolean volver = false;
        while (!volver) {
            mostrarSeparador(Constantes.SEPARADOR);
            mostrarMensaje(Constantes.MENU_USUARIO);
            int opc = leerEntero(Constantes.ELEGIR_OPCION);
            sc.nextLine();

            switch (opc) {
                case 1:
                    Usuario usuario = iniciarSesion();
                    if (usuario == null) {
                        mostrarError(Constantes.ERROR_INICIOSESION3);
                    } else {
                        menuUsuarioLogueado(usuario);
                    }
                    break;
                case 2:
                    registrarUsuario();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    mostrarError(Constantes.OPCION_INVALIDA);
                    break;
            }
        }
    }
    private static void menuUsuarioLogueado(Usuario usuario) throws ExcepcionIdErroneo {
        boolean volver = false;
        while (!volver) {
            mostrarSeparador(Constantes.SEPARADOR);
            mostrarMensaje(Constantes.MENU_USUARIOLOG);
            int opcion = leerEntero(Constantes.ELEGIR_OPCION);
            sc.nextLine();

            switch (opcion) {
                case 1:
                    menuUsuarioJugadores();
                    break;
                case 2:
                    menuUsuarioEquipos();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    mostrarError(Constantes.OPCION_INVALIDA);
            }
        }
    }
    private static void menuUsuarioJugadores() throws ExcepcionIdErroneo {
        boolean volver = false;
        while (!volver) {
            mostrarSeparador(Constantes.SEPARADOR);
            mostrarMensaje(Constantes.MENU_USUARIO2);
            int opcion = leerEntero(Constantes.ELEGIR_OPCION);
            sc.nextLine();

            switch (opcion) {
                case 1:
                    gestionJugador.listarJugadores().forEach(j -> mostrarMensaje(j.toString()));
                    break;
                case 2:
                    int idBuscar = leerEntero("Introduce ID del jugador:");
                    gestionJugador.buscarPorId(idBuscar).ifPresentOrElse(
                            j -> mostrarMensaje(j.toString()),
                            () -> mostrarError("Jugador no encontrado."));
                    break;
                case 3:
                    filtrarJugadoresPorEquipo();
                    break;
                case 4:
                    int idEdad = leerEntero("Introduce ID del jugador:");
                    gestionJugador.buscarPorId(idEdad).ifPresentOrElse(
                            j -> mostrarMensaje("Edad: " + j.calcularEdad() + " años"),
                            () -> mostrarError("Jugador no encontrado."));
                    break;
                case 5:
                    mostrarEstadisticasJugador();
                    break;
                case 6:
                    int idProm = leerEntero("Introduce ID del jugador:");
                    int partidos = leerEntero("Introduce número de partidos jugados:");
                    gestionJugador.buscarPorId(idProm).ifPresentOrElse(
                            j -> mostrarMensaje("Promedio de goles por partido: " + j.calcularPromedioGolesPorPartido(partidos)),
                            () -> mostrarError("Jugador no encontrado."));
                    break;
                case 7:
                    Jugador joven = gestionJugador.obtenerJugadorMasJoven();
                    if (joven != null) {
                        mostrarMensaje("Jugador más joven:\n" + joven.toString());
                    } else {
                        mostrarError("No hay jugadores registrados.");
                    }
                    break;

                case 8:
                    Jugador goleador = gestionJugador.obtenerJugadorMasGoleador();
                    if (goleador != null) {
                        mostrarMensaje("Jugador más goleador:\n" + goleador.toString());
                    } else {
                        mostrarError("No hay jugadores registrados.");
                    }
                    break;
                case 9:
                    Set<Jugador> lista = gestionJugador.listarJugadoresPorEdadAscendente();
                    if (!lista.isEmpty()) {
                        lista.forEach(j -> mostrarMensaje(j.toString()));
                    } else {
                        mostrarError("No hay jugadores registrados.");
                    }
                    break;

                case 10:
                    int id1 = leerEntero("Introduce ID del primer jugador:");
                    int id2 = leerEntero("Introduce ID del segundo jugador:");

                    Optional<Jugador> jugador1 = gestionJugador.buscarPorId(id1);
                    Optional<Jugador> jugador2 = gestionJugador.buscarPorId(id2);

                    if (jugador1.isPresent() && jugador2.isPresent()) {
                        if (jugador1.get().haSuperadoA(jugador2.get())) {
                            mostrarMensaje(jugador1.get().getNombre() + " ha marcado más goles que " + jugador2.get().getNombre());
                        } else if (jugador2.get().haSuperadoA(jugador1.get())) {
                            mostrarMensaje(jugador2.get().getNombre() + " ha marcado más goles que " + jugador1.get().getNombre());
                        } else {
                            mostrarMensaje("Ambos jugadores tienen la misma cantidad de goles.");
                        }
                    } else {
                        mostrarError("Uno o ambos jugadores no fueron encontrados.");
                    }
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    mostrarError(Constantes.OPCION_INVALIDA);
                    break;
            }
        }
    }
    private static void menuUsuarioEquipos() throws ExcepcionIdErroneo {
        boolean volver = false;
        while (!volver) {
            mostrarSeparador(Constantes.SEPARADOR);
            mostrarMensaje(Constantes.MENU_USUARIO3);
            int opcion = leerEntero(Constantes.ELEGIR_OPCION);
            sc.nextLine();

            switch (opcion) {
                case 1:
                    gestionEquipo.listarEquipos().forEach(e -> mostrarMensaje(e.toString()));
                    break;
                case 2:
                    String ciudad = sc.nextLine();
                    Set<Equipo> equipos = gestionEquipo.listarEquipos().stream()
                            .filter(e -> e.getCiudad().equalsIgnoreCase(ciudad))
                            .collect(Collectors.toSet());
                    if (!equipos.isEmpty()) {
                        equipos.forEach(e -> mostrarMensaje(e.toString()));
                    } else {
                        mostrarError("No se encontraron equipos en esa ciudad.");
                    }
                    break;
                case 3:
                    int idEquipo = leerEntero("Introduce ID del equipo:");
                    gestionEquipo.buscarPorId(idEquipo).ifPresentOrElse(
                            e -> mostrarMensaje("Entrenador: " + e.getEntrenador()),
                            () -> mostrarError("Equipo no encontrado."));
                    break;
                case 4:
                    Set<Equipo> ordenados = gestionEquipo.listarEquiposOrdenadosPorNombre();
                    ordenados.forEach(e -> mostrarMensaje(e.toString()));
                    break;
                case 5:
                    System.out.println("Introduce el nombre del equipo:");
                    String nombreEquipo = sc.nextLine();
                    Set<Jugador> jugadores = gestionJugador.listarJugadores().stream()
                            .filter(j -> j.getEquipo().equalsIgnoreCase(nombreEquipo))
                            .collect(Collectors.toSet());
                    if (!jugadores.isEmpty()) {
                        mostrarMensaje("Jugadores del equipo " + nombreEquipo + ":");
                        jugadores.forEach(j -> mostrarMensaje(j.toString()));
                    } else {
                        mostrarError("No se encontraron jugadores para ese equipo.");
                    }
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    mostrarError(Constantes.OPCION_INVALIDA);
                    break;
            }
        }
    }

    private static void insertarEquipoManual() {
        System.out.println("Introduce ID del equipo:");
        int id = leerEntero("");
        sc.nextLine();
        System.out.println("Introduce nombre del equipo:");
        String nombre = sc.nextLine();
        System.out.println("Introduce ciudad:");
        String ciudad = sc.nextLine();
        System.out.println("Introduce estadio:");
        String estadio = sc.nextLine();
        System.out.println("Introduce entrenador:");
        String entrenador = sc.nextLine();

        Equipo nuevo = new Equipo(id, nombre, ciudad, estadio, entrenador);

        boolean insertado = gestionEquipo.insertarEquipo(nuevo);
        if (insertado) {
            mostrarMensaje("Equipo insertado correctamente.");
        } else {
            mostrarError("Ya existe un equipo con ese ID.");
        }
    }

    private static void insertarJugadorManual() throws ExcepcionIdErroneo {
        System.out.println("Introduce ID del jugador:");
        int id = leerEntero("");
        sc.nextLine();
        System.out.println("Nombre:");
        String nombre = sc.nextLine();
        System.out.println("Equipo:");
        String equipo = sc.nextLine();
        System.out.println("Goles:");
        int goles = leerEntero("");
        sc.nextLine();
        System.out.println("Asistencias:");
        int asistencias = leerEntero("");
        sc.nextLine();
        System.out.println("Posición:");
        String posicion = sc.nextLine();

        LocalDate fechaNac = LocalDate.of(2000, 1, 1);

        Jugador nuevo = new Jugador(id, nombre, equipo, goles, asistencias, fechaNac, posicion);

        boolean insertado = gestionJugador.insertarJugador(nuevo);
        if (insertado) {
            mostrarMensaje("Jugador insertado correctamente.");
        } else {
            mostrarError("Ya existe un jugador con ese ID.");
        }
    }
    private static void filtrarJugadoresPorEquipo() {
        System.out.println("Introduce nombre del equipo para filtrar:");
        String equipo = sc.nextLine();
        Set<Jugador> filtrados = gestionJugador.filtrarPorEquipo(equipo);
        if (!filtrados.isEmpty()) {
            filtrados.forEach(j -> mostrarMensaje(j.toString()));
        } else {
            mostrarError("No se encontraron jugadores para el equipo: " + equipo);
        }
    }

}

