package com.prueba.tcs.cuentas.reporte;

import com.prueba.tcs.cuentas.reporte.exception.RangoFechasInvalidoException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Inclusive date range of a reporte, built from one date (a single day) or two dates.
 *
 * @param desde
 * @param hasta
 */
public record RangoFechas(LocalDate desde, LocalDate hasta) {

    /**
     * Builds the range from the fecha query param.
     *
     * @param fecha
     */
    public static RangoFechas of(List<LocalDate> fecha) {

        if (fecha.size() == 1) {
            return new RangoFechas(fecha.getFirst(), fecha.getFirst());
        }
        if (fecha.size() == 2 && !fecha.get(0).isAfter(fecha.get(1))) {
            return new RangoFechas(fecha.get(0), fecha.get(1));
        }
        throw new RangoFechasInvalidoException(fecha);
    }

    public LocalDateTime inicio() {
        return desde.atStartOfDay();
    }

    // Exclusive end so movimientos of the last day are included at any hour
    // it ends on 0:00 the last day.
    public LocalDateTime endCompleteDay() {
        return hasta.plusDays(1).atStartOfDay();
    }
}
