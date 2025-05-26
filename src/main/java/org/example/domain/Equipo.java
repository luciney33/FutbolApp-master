package org.example.domain;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Equipo {
    @EqualsAndHashCode.Include
        private int id;
        private String nombre;
        private String ciudad;
        private String estadio;
        private String entrenador;

        public Equipo(int id, String nombre, String ciudad, String estadio, String entrenador) {
                this.id = id;
                this.nombre = nombre;
                this.ciudad =ciudad;
                this.estadio = estadio;
                this.entrenador = entrenador;
        }
}
