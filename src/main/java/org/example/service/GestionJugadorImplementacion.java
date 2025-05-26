package org.example.service;

import org.example.common.ComprobacionId;
import org.example.common.Constantes;
import org.example.common.ExcepcionIdErroneo;
import org.example.dao.JugadorDAO;
import org.example.dao.JugadorDaoImplementacion;
import org.example.domain.DatosAleatorios;
import org.example.domain.Jugador;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GestionJugadorImplementacion implements GestionJugador{
    private JugadorDAO jugadorDAO;

    public GestionJugadorImplementacion(JugadorDAO jugadorDAO) {
        this.jugadorDAO = jugadorDAO;
    }
    public GestionJugadorImplementacion() {
        this.jugadorDAO = new JugadorDaoImplementacion();
    }
    @Override
    public boolean insertarJugador(Jugador jugador) throws ExcepcionIdErroneo {
        ComprobacionId.comprobarId(jugador.getId());
        return jugadorDAO.insertarJugador(jugador);
    }


    @Override
    public boolean eliminarJugador(Jugador jugador) {
        return jugadorDAO.eliminarJugador(jugador);
    }

    @Override
    public Jugador obtenerJugadorMasJoven() {
        return  jugadorDAO.getJugadores().stream().min(Comparator.comparing(Jugador::calcularEdad)).orElse(null);
    }

    @Override
    public Jugador obtenerJugadorMasGoleador() {
        return jugadorDAO.getJugadores().stream().max(Comparator.comparingInt(Jugador::getGoles)).orElse(null);
    }

    @Override
    public Set<Jugador> listarJugadores() {
        return jugadorDAO.getJugadores();
    }

    @Override
    public String mostrarEstadisticasJugador(int id) throws ExcepcionIdErroneo {
        Optional<Jugador> jugadorOpt = buscarPorId(id);
        if (jugadorOpt.isPresent()) {
            Jugador j = jugadorOpt.get();
            StringBuilder sb = new StringBuilder();
            sb.append("Jugador: ").append(j.getNombre()).append("\n");
            sb.append("Equipo: ").append(j.getEquipo()).append("\n");
            sb.append("Edad: ").append(j.calcularEdad()).append("\n");
            sb.append("Goles: ").append(j.getGoles()).append("\n");
            sb.append("Asistencias: ").append(j.getAsistencias()).append("\n");
            return sb.toString();
        } else {
            return "Jugador no encontrado";
        }
    }

    @Override
    public Set<Jugador> listarJugadoresPorEdadAscendente() {
        return jugadorDAO.getJugadores().stream().sorted(Comparator.comparing(Jugador::calcularEdad)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Jugador> filtrarPorEquipo(String nombreEquipo) {
        return jugadorDAO.getJugadores().stream().filter(j -> j.getEquipo().equalsIgnoreCase(nombreEquipo) ).collect(Collectors.toSet());
    }

    @Override
    public Optional<Jugador> buscarPorId(int id) throws ExcepcionIdErroneo {
        return jugadorDAO.buscarPorId(id);
    }

}
