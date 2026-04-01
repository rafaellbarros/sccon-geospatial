package br.com.rafaellbarros.sccon.geospatial.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Pessoa {

    private Long id;

    private String nome;

    @JsonProperty("data_nascimento")
    private LocalDate dataNascimento;

    @JsonProperty("data_admissão")
    private LocalDate dataAdmissao;

    public Pessoa() {
    }

    public Pessoa(String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
    }
}