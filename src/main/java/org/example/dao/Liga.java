package org.example.dao;

import lombok.Data;
import org.example.domain.Equipo;
import org.example.domain.Jugador;

import java.util.HashSet;
import java.util.Set;

@Data
public class Liga {
    private Set<Jugador> jugadores = new HashSet<>();//lo puedo hacer con faker/json
    private Set<Equipo> equipos = new HashSet<>();//lo puedo hacer con fichero
    public Liga(){
        jugadores = DaoFicheros.leerJugadores();
        equipos = DaoFicheros.leerEquipos();
    }


    public void insertarJugador(Jugador j) {
        jugadores.add(j);
        DaoFicheros.guardarJugadores(jugadores);
    }

    public void eliminarJugador(Jugador j) {
        jugadores.remove(j);
        DaoFicheros.guardarJugadores(jugadores);
    }
    public void insertarEquipo(Equipo e) {
        equipos.add(e);
        DaoFicheros.guardarEquipos(equipos);
    }

    public void eliminarEquipo(Equipo e) {
        equipos.remove(e);
        DaoFicheros.guardarEquipos(equipos);
    }

}
