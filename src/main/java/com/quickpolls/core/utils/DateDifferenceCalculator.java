package com.quickpolls.core.utils;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateDifferenceCalculator {
    public static long calcularDiferenciaDias(String fechaInicioISO, String fechaFinISO) {
        try {
            LocalDate fechaInicio = LocalDate.parse(fechaInicioISO);
            LocalDate fechaFin = LocalDate.parse(fechaFinISO);
            return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha invalido. Use el formato ISO: yyyy-MM-dd", e);
        }
    }
}
