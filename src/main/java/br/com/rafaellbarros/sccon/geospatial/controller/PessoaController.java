package br.com.rafaellbarros.sccon.geospatial.controller;


import br.com.rafaellbarros.sccon.geospatial.domain.model.Pessoa;
import br.com.rafaellbarros.sccon.geospatial.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PessoaController {

    private final PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarPessoas() {
        return ResponseEntity.ok(pessoaService.buscarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                pessoaService.buscarPorId(id)
        );
    }

    @GetMapping("/{id}/age")
    public ResponseEntity<Long> calcularIdade(
            @PathVariable Long id,
            @RequestParam String output
    ) {
        return ResponseEntity.ok(
                pessoaService.calcularIdade(id, output)
        );
    }

    @GetMapping("/{id}/salary")
    public ResponseEntity<BigDecimal> calcularSalario(
            @PathVariable Long id,
            @RequestParam String output
    ) {
        return ResponseEntity.ok(
                pessoaService.calcularSalario(id, output)
        );
    }

    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(
            @RequestBody Pessoa pessoa
    ) {
        Pessoa novaPessoa = pessoaService.criarPessoa(pessoa);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaPessoa.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(novaPessoa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(
            @PathVariable Long id,
            @RequestBody Pessoa pessoa
    ) {
        return ResponseEntity.ok(
                pessoaService.atualizarPessoa(id, pessoa)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoaParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> campos
    ) {
        return ResponseEntity.ok(
                pessoaService.atualizarPessoaParcial(id, campos)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPessoa(
            @PathVariable Long id
    ) {
        pessoaService.removerPessoa(id);

        return ResponseEntity.noContent().build();
    }
}