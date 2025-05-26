package org.example.service;

import org.example.common.ExcepcionIdErroneo;
import org.example.domain.Jugador;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface GestionJugador{
    public boolean insertarJugador(Jugador jugador) throws ExcepcionIdErroneo;
    public boolean eliminarJugador(Jugador jugador);
    public Jugador obtenerJugadorMasJoven();
    public Jugador obtenerJugadorMasGoleador();
    public Set<Jugador> listarJugadores();
    public String mostrarEstadisticasJugador(int id) throws ExcepcionIdErroneo;
    public Set<Jugador> listarJugadoresPorEdadAscendente();
    public Set<Jugador> filtrarPorEquipo(String nombreEquipo);
    public Optional<Jugador> buscarPorId(int id) throws ExcepcionIdErroneo;
}
