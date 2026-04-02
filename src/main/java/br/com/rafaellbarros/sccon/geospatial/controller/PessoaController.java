package br.com.rafaellbarros.sccon.geospatial.controller;


import br.com.rafaellbarros.sccon.geospatial.domain.model.Pessoa;
import br.com.rafaellbarros.sccon.geospatial.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPessoa(
            @PathVariable Long id
    ) {
        pessoaService.removerPessoa(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(
            @PathVariable Long id,
            @RequestBody Pessoa pessoa
    ) {
        Pessoa pessoaAtualizada = pessoaService.atualizarPessoa(id, pessoa);

        return ResponseEntity.ok(pessoaAtualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoaParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> campos
    ) {
        Pessoa pessoaAtualizada =
                pessoaService.atualizarPessoaParcial(id, campos);

        return ResponseEntity.ok(pessoaAtualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPorId(
            @PathVariable Long id
    ) {
        Pessoa pessoa = pessoaService.buscarPorId(id);

        return ResponseEntity.ok(pessoa);
    }

}