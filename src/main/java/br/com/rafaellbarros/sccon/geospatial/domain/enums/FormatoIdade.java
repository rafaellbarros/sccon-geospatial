package br.com.rafaellbarros.sccon.geospatial.domain.enums;

public enum FormatoIdade {
    DAYS,
    MONTHS,
    YEARS;

    public static FormatoIdade from(String value) {
        try {
            return FormatoIdade.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Formato de saída inválido. Use: days, months ou years"
            );
        }
    }
}