package org.example.service;

import org.example.common.ExcepcionIdErroneo;
import org.example.domain.Equipo;

import java.util.Optional;
import java.util.Set;

public interface GestionEquipo {
        public boolean insertarEquipo(Equipo equipo);

        public boolean eliminarEquipo(Equipo equipo);

        public Optional<Equipo> buscarPorId(int id) throws ExcepcionIdErroneo;

        public Set<Equipo> listarEquiposOrdenadosPorNombre();

        public Optional<Equipo> buscarPorCiudad(String ciudad);

        public void modificarEntrenador(int id, String nuevoEntrenador);

        public Set<Equipo> listarEquipos();

}
