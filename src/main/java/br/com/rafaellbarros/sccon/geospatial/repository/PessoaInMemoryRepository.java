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

    private final Map<Long, Pessoa> pessoaMap = new ConcurrentHashMap<>();

    public List<Pessoa> buscarTodas() {
        return pessoaMap.values().stream().toList();
    }


    public Pessoa salvarSeAusente(Long id, Pessoa pessoa) {
        Pessoa existente = pessoaMap.putIfAbsent(id, pessoa);

        if (existente != null) {
            throw new PessoaDuplicadaException(
                    String.format(
                            "Já existe uma pessoa com o ID %d. Nome: %s",
                            id,
                            existente.getNome()
                    )
            );
        }

        pessoa.setId(id);
        return pessoa;
    }

    public void removerPorId(Long id) {
        Pessoa pessoaRemovida = pessoaMap.remove(id);

        if (pessoaRemovida == null) {
            throw new PessoaNaoEncontradaException(
                    String.format(
                            "Pessoa com ID %d não encontrada",
                            id
                    )
            );
        }
    }

    public Pessoa atualizarPorId(Long id, Pessoa pessoaAtualizada) {
        Pessoa pessoaExistente = pessoaMap.get(id);

        if (pessoaExistente == null) {
            throw new PessoaNaoEncontradaException(
                    String.format(
                            "Pessoa com ID %d não encontrada",
                            id
                    )
            );
        }

        pessoaAtualizada.setId(id);
        pessoaMap.put(id, pessoaAtualizada);

        return pessoaAtualizada;
    }
}