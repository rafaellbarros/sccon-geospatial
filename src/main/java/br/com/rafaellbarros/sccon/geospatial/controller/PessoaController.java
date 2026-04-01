package br.com.rafaellbarros.sccon.geospatial.controller;


import br.com.rafaellbarros.sccon.geospatial.domain.model.Pessoa;
import br.com.rafaellbarros.sccon.geospatial.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PessoaController {

    private final PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarTodas() {
        return ResponseEntity.ok(pessoaService.buscarTodas());
    }

    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody Pessoa pessoa
    ) {
        Pessoa novaPessoa = pessoaService.criarPessoa(pessoa);

        URI location = URI.create("/pessoas/" + novaPessoa.getId());

        return ResponseEntity
                .created(location)
                .body(novaPessoa);
    }

}