package dev.trier.ecommerce.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CategoriaProduto {
    PERIFERICOS,
    PROCESSADOR,
    RAM,
    GABINETE;


    @JsonCreator
    public static CategoriaProduto fromString(String value) {
        return CategoriaProduto.valueOf(value.toUpperCase().replace(" ", "_"));

    }
}
