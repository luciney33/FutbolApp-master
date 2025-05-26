package org.example.dao;

import org.example.domain.Equipo;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EquipoDaoImplementacion implements EquipoDAO{
    private Liga liga;

    public EquipoDaoImplementacion(Liga liga) {
        this.liga = liga;
    }

    public EquipoDaoImplementacion() {
        liga = new Liga();
    }

    @Override
    public Set<Equipo> getEquipos() {
        return liga.getEquipos();
    }

    @Override
    public boolean insertarEquipo(Equipo equipo) {
        boolean insertarSi = false;
        Optional<Equipo> existe = buscarPorId(equipo.getId());
        if (existe.isEmpty()){
            insertarSi = true;
            liga.insertarEquipo(equipo);
        }else {
            insertarSi = false;
        }
        return insertarSi;
    }

    @Override
    public boolean eliminarEquipo(Equipo equipo) {
        boolean eliminado = liga.getEquipos().contains(equipo);
        if (eliminado) {
            liga.eliminarEquipo(equipo);
        }
        return eliminado;
    }

    @Override
    public void modificarEquipo(int id, String entrenador) {
        liga.getEquipos().stream().filter(e -> e.getId() == id).findAny().ifPresent(e ->
        {
            e.setEntrenador(entrenador);
            DaoFicheros.guardarEquipos(liga.getEquipos());
        });
    }

    @Override
    public Optional<Equipo> buscarPorId(int id) {
        return liga.getEquipos().stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public Set<Equipo> buscarPorCiudad(String ciudad) {
        return liga.getEquipos().stream().filter(e -> e.getCiudad().equals(ciudad)).collect(Collectors.toSet());
    }
}
