package br.com.rafaellbarros.sccon.geospatial.domain.enums;

public enum FormatoSalario {
    MIN,
    FULL;

    public static FormatoSalario from(String value) {
        try {
            return FormatoSalario.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Formato de saída inválido. Use: min ou full"
            );
        }
    }
}