package br.com.rafaellbarros.sccon.geospatial.service;

import br.com.rafaellbarros.sccon.geospatial.domain.model.Pessoa;
import br.com.rafaellbarros.sccon.geospatial.repository.PessoaInMemoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaInMemoryRepository repository;
    private final AtomicLong currentId = new AtomicLong(1);

    @PostConstruct
    public void init() {
        inicializarPessoas();
    }

    private void inicializarPessoas() {
        List<Pessoa> pessoasIniciais = List.of(
                new Pessoa(
                        "João Silva",
                        LocalDate.of(1990, 5, 15),
                        LocalDate.of(2015, 3, 10)
                ),
                new Pessoa(
                        "Maria Oliveira",
                        LocalDate.of(1985, 8, 22),
                        LocalDate.of(2010, 7, 1)
                ),
                new Pessoa(
                        "Carlos Santos",
                        LocalDate.of(1995, 12, 3),
                        LocalDate.of(2018, 1, 15)
                )
        );

        pessoasIniciais.forEach(this::criarPessoa);

        log.info("Base inicializada com {} pessoas", buscarTodas().size());
    }

    public List<Pessoa> buscarTodas() {
        log.info("Buscando todas as pessoas");

        return repository.buscarTodas()
                .stream()
                .sorted(Comparator.comparing(
                        Pessoa::getNome,
                        String.CASE_INSENSITIVE_ORDER
                ))
                .toList();
    }

    public Pessoa criarPessoa(Pessoa pessoa) {
        validarCamposObrigatorios(pessoa);

        Long id = obterOuGerarId(pessoa);

        atualizarSequenciaIdSeNecessario(id);

        Pessoa pessoaSalva = repository.salvarSeAusente(id, pessoa);

        log.info(
                "Pessoa criada com sucesso. id={}, nome={}",
                pessoaSalva.getId(),
                pessoaSalva.getNome()
        );

        return pessoaSalva;
    }

    public void removerPessoa(Long id) {
        repository.removerPorId(id);

        log.info("Pessoa removida com sucesso. id={}", id);
    }

    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) {
        validarCamposObrigatorios(pessoa);

        Pessoa pessoaAtualizada = repository.atualizarPorId(id, pessoa);

        log.info(
                "Pessoa atualizada com sucesso. id={}, nome={}",
                id,
                pessoaAtualizada.getNome()
        );

        return pessoaAtualizada;
    }

    public Pessoa atualizarPessoaParcial(
            Long id,
            Map<String, Object> campos
    ) {
        Pessoa pessoaAtualizada =
                repository.atualizarParcialPorId(id, campos);

        log.info(
                "Pessoa atualizada parcialmente. id={}, campos={}",
                id,
                campos.keySet()
        );

        return pessoaAtualizada;
    }

    private void validarCamposObrigatorios(Pessoa pessoa) {
        validarTextoObrigatorio(
                pessoa.getNome(),
                "Nome é obrigatório"
        );

        validarCampoNulo(
                pessoa.getDataNascimento(),
                "Data de nascimento é obrigatória"
        );

        validarCampoNulo(
                pessoa.getDataAdmissao(),
                "Data de admissão é obrigatória"
        );
    }

    private void validarTextoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private void validarCampoNulo(Object valor, String mensagem) {
        if (valor == null) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private Long obterOuGerarId(Pessoa pessoa) {
        if (pessoa.getId() == null) {
            Long novoId = currentId.getAndIncrement();
            pessoa.setId(novoId);
            return novoId;
        }

        return pessoa.getId();
    }

    private void atualizarSequenciaIdSeNecessario(Long id) {
        currentId.updateAndGet(current -> Math.max(current, id + 1));
    }
}