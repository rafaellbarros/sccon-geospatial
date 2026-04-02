package br.com.rafaellbarros.sccon.geospatial.repository;

import br.com.rafaellbarros.sccon.geospatial.domain.model.Pessoa;
import br.com.rafaellbarros.sccon.geospatial.exception.PessoaDuplicadaException;
import br.com.rafaellbarros.sccon.geospatial.exception.PessoaNaoEncontradaException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class PessoaInMemoryRepository {

    private final Map<Long, Pessoa> pessoas = new ConcurrentHashMap<>();

    public List<Pessoa> buscarTodas() {
        return List.copyOf(pessoas.values());
    }

    public Pessoa criar(Long id, Pessoa pessoa) {
        Pessoa pessoaExistente = pessoas.putIfAbsent(id, pessoa);

        if (pessoaExistente != null) {
            throw new PessoaDuplicadaException(
                    String.format(
                            "Já existe uma pessoa com o ID %d. Nome: %s",
                            id,
                            pessoaExistente.getNome()
                    )
            );
        }

        pessoa.setId(id);
        return pessoa;
    }

    public Pessoa atualizar(Long id, Pessoa pessoa) {
        buscarPorId(id);

        pessoa.setId(id);
        pessoas.put(id, pessoa);

        return pessoa;
    }

    public void remover(Long id) {
        Pessoa pessoa = buscarPorId(id);
        pessoas.remove(pessoa.getId());
    }

    public Pessoa buscarPorId(Long id) {
        Pessoa pessoa = pessoas.get(id);

        if (pessoa == null) {
            throw new PessoaNaoEncontradaException(
                    String.format(
                            "Pessoa com ID %d não encontrada",
                            id
                    )
            );
        }

        return pessoa;
    }
}