package org.example.dao;

import org.example.common.ExcepcionIdErroneo;
import org.example.domain.Jugador;

import java.util.Optional;
import java.util.Set;

public interface JugadorDAO {
    public Set<Jugador> getJugadores();
    public boolean insertarJugador(Jugador jugador) throws ExcepcionIdErroneo;
    public boolean eliminarJugador(Jugador jugador);
    public void modificarJugador(int id, int goles, int asistencias, String equipo);
    public Optional<Jugador> buscarPorId(int id) throws ExcepcionIdErroneo;

}
