package br.com.rafaellbarros.sccon.geospatial.controller;


import br.com.rafaellbarros.sccon.geospatial.domain.model.Pessoa;
import br.com.rafaellbarros.sccon.geospatial.service.PessoaMemoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PessoaMemoriaController {
    
    private final PessoaMemoriaService pessoaMemoriaService;
    
    public PessoaMemoriaController(PessoaMemoriaService pessoaMemoriaService) {
        this.pessoaMemoriaService = pessoaMemoriaService;
    }
    
    @GetMapping
    public ResponseEntity<List<Pessoa>> listarTodas() {
        return ResponseEntity.ok(pessoaMemoriaService.buscarTodas());
    }

}