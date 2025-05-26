package org.example.domain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.ExcepcionAsistencias;
import org.example.common.ExcepcionGoles;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Jugador {
    @EqualsAndHashCode.Include
    private int id;

    private String nombre;
    private String equipo;
    private int goles;
    private int asistencias;
    private LocalDate fechaNac;
    private String posicion;

    public Jugador(int id, String nombre, String equipo, int goles, int asistencias, LocalDate fechaNac, String posicion) {
        this.id = id;
        this.nombre = nombre;
        this.equipo = equipo;
        this.goles = goles;
        this.asistencias = asistencias;
        this.fechaNac = fechaNac;
        this.posicion = posicion;
    }
    public Jugador(){}

    public int calcularEdad(){
        LocalDate fechaAct = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(fechaNac, fechaAct);
    }
    public double calcularPromedioGolesPorPartido(int totalPartidos){
        double promedio = 0;
        if(totalPartidos > 0){
            promedio = (double) goles / totalPartidos;
        }else{
            System.out.println("ingrese los partidos");
        }
        return promedio;
    }
    public void incrementarGoles(int cantidad) throws ExcepcionGoles {
        if(cantidad > 0){
            goles += cantidad;
        }else{
            throw new ExcepcionGoles();
        }
    }
    public void incrementarAsistencias(int cantidad2) throws ExcepcionAsistencias {
        if(cantidad2 > 0){
            asistencias += cantidad2;
        }else{
            throw new ExcepcionAsistencias();
        }
    }

    public boolean haSuperadoA(Jugador otroJugador){
        return this.goles > otroJugador.getGoles();
    }

}