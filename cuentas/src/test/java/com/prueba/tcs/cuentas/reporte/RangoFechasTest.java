package com.prueba.tcs.cuentas.reporte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.prueba.tcs.cuentas.reporte.exception.RangoFechasInvalidoException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class RangoFechasTest {

    private static final LocalDate DESDE = LocalDate.of(2022, 2, 1);
    private static final LocalDate HASTA = LocalDate.of(2022, 2, 10);

    @Test
    void should_return_same_day_when_one_fecha() {

        // ---------------------- Act -------------------------------------------------------
        RangoFechas rango = RangoFechas.of(List.of(HASTA));

        // ---------------------- Assert ----------------------------------------------------
        assertEquals(HASTA, rango.desde());
        assertEquals(HASTA, rango.hasta());
    }

    @Test
    void should_return_range_when_two_fechas() {

        // ---------------------- Act -------------------------------------------------------
        RangoFechas rango = RangoFechas.of(List.of(DESDE, HASTA));

        // ---------------------- Assert ----------------------------------------------------
        assertEquals(DESDE, rango.desde());
        assertEquals(HASTA, rango.hasta());
    }

    @Test
    void should_include_whole_last_day_when_fin_exclusivo() {

        // ---------------------- Arrange ---------------------------------------------------
        RangoFechas rango = RangoFechas.of(List.of(DESDE, HASTA));

        // ---------------------- Act -------------------------------------------------------
        LocalDateTime inicio = rango.inicio();
        LocalDateTime fin = rango.endCompleteDay();

        // ---------------------- Assert ----------------------------------------------------
        assertEquals(LocalDateTime.of(2022, 2, 1, 0, 0), inicio);
        assertEquals(LocalDateTime.of(2022, 2, 11, 0, 0), fin);
    }

    @Test
    void should_throw_when_inicio_after_fin() {

        // ---------------------- Act & Assert ----------------------------------------------
        assertThrows(RangoFechasInvalidoException.class, () -> RangoFechas.of(List.of(HASTA, DESDE)));
    }

    @Test
    void should_throw_when_more_than_two_fechas() {

        // ---------------------- Act & Assert ----------------------------------------------
        assertThrows(RangoFechasInvalidoException.class, () -> RangoFechas.of(List.of(DESDE, HASTA, HASTA)));
    }
}
