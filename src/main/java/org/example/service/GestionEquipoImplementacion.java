package org.example.service;

import org.example.common.ComprobacionId;
import org.example.common.ExcepcionIdErroneo;
import org.example.dao.EquipoDAO;
import org.example.dao.EquipoDaoImplementacion;
import org.example.domain.Equipo;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GestionEquipoImplementacion implements GestionEquipo {
    private EquipoDAO equipoDAO;

    public GestionEquipoImplementacion(EquipoDAO equipoDAO) {
        this.equipoDAO = equipoDAO;
    }
    public GestionEquipoImplementacion() {
        this.equipoDAO = new EquipoDaoImplementacion();
    }

    @Override
    public boolean insertarEquipo(Equipo equipo) {
        return equipoDAO.insertarEquipo(equipo);
    }

    @Override
    public boolean eliminarEquipo(Equipo equipo) {
        return equipoDAO.eliminarEquipo(equipo);
    }

    @Override
    public Optional<Equipo> buscarPorId(int id) throws ExcepcionIdErroneo {
        ComprobacionId.comprobarId(id);
        return equipoDAO.getEquipos().stream().filter(e -> e.getId() == id ).findFirst();
    }

    @Override
    public Set<Equipo> listarEquiposOrdenadosPorNombre() {
        return equipoDAO.getEquipos().stream().sorted(Comparator.comparing(Equipo::getNombre)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Optional<Equipo> buscarPorCiudad(String ciudad) {
        return equipoDAO.getEquipos().stream().filter(e -> e.getCiudad().equalsIgnoreCase(ciudad)).findFirst();
    }

    @Override
    public void modificarEntrenador(int id, String nuevoEntrenador) {
        equipoDAO.modificarEquipo(id,nuevoEntrenador);
    }

    @Override
    public Set<Equipo> listarEquipos() {
        return equipoDAO.getEquipos();
    }
}