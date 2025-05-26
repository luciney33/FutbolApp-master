package service;

import lombok.extern.flogger.Flogger;
import org.example.common.ExcepcionIdErroneo;
import org.example.dao.JugadorDAO;
import org.example.domain.Jugador;
import org.example.service.GestionJugadorImplementacion;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)

public class GestionJugadorImplementacionTest{
    private static final Logger logger = Logger.getLogger(GestionJugadorImplementacionTest.class.getName());
    @Mock
    JugadorDAO jugadorDAO;

    @InjectMocks
    GestionJugadorImplementacion gestionJugador;

    Jugador jugador = new Jugador(-1,"David","Atleti",12,5, LocalDate.of(1999, 5, 12),"delantero");
    Jugador jugador2 = new Jugador(1, "Pepe", "Atleti", 18, 10, LocalDate.of(2000, 3, 22), "delantero");
    Jugador jugador3 = new Jugador(2, "Juan", "Madrid", 20, 3, LocalDate.of(1998, 8, 10), "defensa");

    @Test
    @Order(1)
    @DisplayName("Insertar jugador devuelve false si el jugador ya existe")
    void insertarJugador_devuelveFalse_siJugadorYaExiste() throws ExcepcionIdErroneo {
        when(jugadorDAO.buscarPorId(anyInt())).thenReturn(Optional.of(jugador));
        logger.info("Insertar Jugador devuelve true si el jugador ya existe");

        ExcepcionIdErroneo e = assertThrows(ExcepcionIdErroneo.class, () -> gestionJugador.insertarJugador(jugador));

        assertEquals("Id negativo",e.getMessage());
    }
    @ParameterizedTest
    @Order(3)
    @ValueSource(strings = {"Atleti", "Madrid"})
    void filtrarPorEquipo_devuelveSoloJugadoresDelEquipoEspecificado() {
        when(jugadorDAO.getJugadores()).thenReturn(Set.of(jugador2, jugador3));

            Set<Jugador> resultado = gestionJugador.filtrarPorEquipo("Atleti");

        assertTrue(resultado.contains(jugador2));
        assertFalse(resultado.contains(jugador3));
    }

    @Test
    @Order(2)
    void eliminarJugador_delDAO() {
        when(jugadorDAO.eliminarJugador(jugador)).thenReturn(true);

        boolean resultado = gestionJugador.eliminarJugador(jugador);

        assertTrue(resultado);
        verify(jugadorDAO,times(1)).eliminarJugador(jugador);
    }
    @Nested
    @Order(4)
    public class pruebasTest {
        @Test
        @Order(5)
        void obtenerJugadorMasGoleador_devuelveJugadorConMasGoles() {
            when(jugadorDAO.getJugadores()).thenReturn(Set.of(jugador, jugador2, jugador3));

            Jugador j = gestionJugador.obtenerJugadorMasGoleador();

            assertTrue(j.getGoles() == 20);
        }

        @Test
        @Order(6)
        void obtenerJugadorMasJoven_devuelveJugadorConMenorEdad() {

            when(jugadorDAO.getJugadores()).thenReturn(Set.of(jugador, jugador2, jugador3));

            Jugador masJoven = gestionJugador.obtenerJugadorMasJoven();

            assertThat(masJoven.getNombre()).isEqualTo(jugador2.getNombre());
        }
    }
        @Test
        @Order(7)
        void listarJugadoresPorEdadAscendente_devuelveJugadoresOrdenados() {

            when(jugadorDAO.getJugadores()).thenReturn(Set.of(jugador, jugador2, jugador3));

            Set<Jugador> resultado = gestionJugador.listarJugadoresPorEdadAscendente();
            List<Jugador> listaOrdenada = new ArrayList<>(resultado);

            assertThat(listaOrdenada.get(0)).isEqualTo(jugador3.getNombre());
            assertThat(listaOrdenada.get(1)).isEqualTo(jugador.getNombre());
            assertThat(listaOrdenada.get(2)).isEqualTo(jugador2.getNombre());
        }

}
