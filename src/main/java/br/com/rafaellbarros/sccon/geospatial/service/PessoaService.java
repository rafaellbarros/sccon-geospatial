package br.com.rafaellbarros.sccon.geospatial.service;

import br.com.rafaellbarros.sccon.geospatial.domain.model.Pessoa;
import br.com.rafaellbarros.sccon.geospatial.exception.PessoaNaoEncontradaException;
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

    private static final String MSG_NOME_OBRIGATORIO =
            "Nome é obrigatório";

    private static final String MSG_DATA_NASCIMENTO_OBRIGATORIA =
            "Data de nascimento é obrigatória";

    private static final String MSG_DATA_ADMISSAO_OBRIGATORIA =
            "Data de admissão é obrigatória";

    private final PessoaInMemoryRepository repository;
    private final AtomicLong currentId = new AtomicLong(1);

    @PostConstruct
    public void init() {
        carregarBaseInicial();
    }

    public List<Pessoa> buscarTodas() {
        log.info("Buscando todas as pessoas");

        return repository.buscarTodas()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Pessoa::getNome,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .toList();
    }

    public Pessoa criarPessoa(Pessoa pessoa) {
        validarPessoaObrigatoria(pessoa);

        Long id = gerarOuObterId(pessoa.getId());
        pessoa.setId(id);

        Pessoa pessoaSalva = repository.criar(id, pessoa);

        log.info(
                "Pessoa criada com sucesso. id={}, nome={}",
                id,
                pessoaSalva.getNome()
        );

        return pessoaSalva;
    }

    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) {
        validarPessoaObrigatoria(pessoa);

        Pessoa pessoaAtualizada = repository.atualizar(id, pessoa);

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
        Pessoa pessoa = repository.buscarPorId(id);

        atualizarNomeSeInformado(campos, pessoa);
        atualizarDataNascimentoSeInformada(campos, pessoa);
        atualizarDataAdmissaoSeInformada(campos, pessoa);

        Pessoa pessoaAtualizada =
                repository.atualizar(id, pessoa);

        log.info(
                "Pessoa atualizada parcialmente. id={}, campos={}",
                id,
                campos.keySet()
        );

        return pessoaAtualizada;
    }

    public void removerPessoa(Long id) {
        repository.remover(id);

        log.info("Pessoa removida com sucesso. id={}", id);
    }

    public Pessoa buscarPorId(Long id) {
        Pessoa pessoa = repository.buscarPorId(id);

        log.info(
                "Pessoa encontrada. id={}, nome={}",
                id,
                pessoa.getNome()
        );

        return pessoa;
    }

    private void validarPessoaObrigatoria(Pessoa pessoa) {
        validarTextoObrigatorio(
                pessoa.getNome(),
                MSG_NOME_OBRIGATORIO
        );

        validarNaoNulo(
                pessoa.getDataNascimento(),
                MSG_DATA_NASCIMENTO_OBRIGATORIA
        );

        validarNaoNulo(
                pessoa.getDataAdmissao(),
                MSG_DATA_ADMISSAO_OBRIGATORIA
        );
    }

    private void validarTextoObrigatorio(
            String valor,
            String mensagem
    ) {
        if (valor == null || valor.trim().isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private void validarNaoNulo(
            Object valor,
            String mensagem
    ) {
        if (valor == null) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private Long gerarOuObterId(Long idExistente) {
        Long id = idExistente != null
                ? idExistente
                : currentId.getAndIncrement();

        atualizarSequenciaId(id);

        return id;
    }

    private void atualizarSequenciaId(Long id) {
        currentId.updateAndGet(
                current -> Math.max(current, id + 1)
        );
    }

    private void carregarBaseInicial() {
        criarPessoasIniciais().forEach(this::criarPessoa);

        log.info(
                "Base inicializada com {} pessoas",
                buscarTodas().size()
        );
    }

    private List<Pessoa> criarPessoasIniciais() {
        return List.of(
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
    }

    private void atualizarNomeSeInformado(
            Map<String, Object> campos,
            Pessoa pessoa
    ) {
        if (!campos.containsKey("nome")) {
            return;
        }

        String nome = (String) campos.get("nome");

        validarTextoObrigatorio(
                nome,
                MSG_NOME_OBRIGATORIO
        );

        pessoa.setNome(nome);
    }

    private void atualizarDataNascimentoSeInformada(
            Map<String, Object> campos,
            Pessoa pessoa
    ) {
        if (campos.containsKey("data_nascimento")) {
            pessoa.setDataNascimento(
                    LocalDate.parse(
                            campos.get("data_nascimento").toString()
                    )
            );
        }
    }

    private void atualizarDataAdmissaoSeInformada(
            Map<String, Object> campos,
            Pessoa pessoa
    ) {
        if (campos.containsKey("data_admissao")) {
            pessoa.setDataAdmissao(
                    LocalDate.parse(
                            campos.get("data_admissao").toString()
                    )
            );
        }
    }
}