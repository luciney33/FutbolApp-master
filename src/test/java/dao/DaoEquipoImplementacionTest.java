package dao;

import org.example.dao.EquipoDaoImplementacion;
import org.example.dao.Liga;
import org.example.domain.Equipo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DaoEquipoImplementacionTest {

    @Mock
    Liga ligaMock;//liga porque el dao depende de él

    @InjectMocks
    EquipoDaoImplementacion equipodao;

    Equipo equipo1, equipo2;

    Set<Equipo> equipos;

    @BeforeEach
    void setUp() {
        equipo1 = new Equipo(1, "Betis", "Sevilla", "Villamarín", "Pellegrini");
        equipo2 = new Equipo(2, "Sevilla", "Sevilla", "Sánchez-Pizjuán", "Mendilibar");
        equipos = new HashSet<>(Set.of(equipo1, equipo2));

        when(ligaMock.getEquipos()).thenReturn(equipos);
    }
    @Nested
    @Order(1)
    public class pruebasTest {
        @Test
        @Order(2)
        void insertarEquipo_devuelveTrue_siNoExiste() {
            Equipo nuevo = new Equipo(3, "Cádiz", "Cádiz", "Nuevo Mirandilla", "Sergio");

            boolean resultado = equipodao.insertarEquipo(nuevo);

            assertTrue(resultado);
            assertTrue(equipos.contains(nuevo));
        }

        @Test
        @Order(3)
        void insertarEquipo_devuelveFalse_siYaExiste() {
            boolean resultado = equipodao.insertarEquipo(equipo1);

            assertFalse(resultado);
            verify(ligaMock, never()).getEquipos().add(any());
        }
    }
    @Test
    @Order(6)
    @DisplayName("Eliminar el equipo existente")
    void eliminarEquipo_devuelveTrue_siExiste() {
        boolean resultado = equipodao.eliminarEquipo(equipo1);

        assertTrue(resultado);
        assertFalse(equipos.contains(equipo1));
    }

    @Test
    @Order(7)
    void buscarPorId_devuelveEquipoCorrecto() {
        Optional<Equipo> encontrado = equipodao.buscarPorId(1);

        assertTrue(encontrado.isPresent());
        assertEquals("Betis", encontrado.get().getNombre());
    }

    @Test
    @Order(4)
    void modificarEquipo_actualizaEntrenador_siExiste() {
        equipodao.modificarEquipo(1, "Nuevo Entrenador");
        assertEquals("Nuevo Entrenador", equipo1.getEntrenador());
    }
    @Test
    @Order(5)
    void buscarPorCiudad_devuelveEquiposCorrectos() {
        Set<Equipo> resultado = equipodao.buscarPorCiudad("Sevilla");

        assertEquals(2, resultado.size());
    }
}
