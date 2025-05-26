package org.example.dao;

import org.example.domain.Equipo;

import java.util.Optional;
import java.util.Set;

public interface EquipoDAO {
    public boolean insertarEquipo(Equipo equipo);
    public boolean eliminarEquipo(Equipo equipo);
    public void modificarEquipo(int id, String entrenador);
    public Set<Equipo> getEquipos();
    public Optional<Equipo> buscarPorId(int id);
    public Set<Equipo> buscarPorCiudad(String ciudad);
}
