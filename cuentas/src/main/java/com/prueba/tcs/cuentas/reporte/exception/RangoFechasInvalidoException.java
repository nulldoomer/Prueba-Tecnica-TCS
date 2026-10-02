package com.prueba.tcs.cuentas.reporte.exception;

import com.prueba.tcs.cuentas.infrastructure.exception.BusinessException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * Thrown when the fecha param is not one date or a valid range of two.
 */
public class RangoFechasInvalidoException extends BusinessException {

    public RangoFechasInvalidoException(List<LocalDate> fecha) {
        super(
                "El rango de fechas debe ser una fecha o dos, la de inicio menor o igual a la final",
                HttpStatus.BAD_REQUEST,
                "RANGO_FECHAS_INVALIDO",
                Map.of("fecha", fecha.stream().map(LocalDate::toString).toList()));
    }
}
