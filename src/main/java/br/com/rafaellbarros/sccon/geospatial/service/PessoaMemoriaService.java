package br.com.rafaellbarros.sccon.geospatial.service;

import br.com.rafaellbarros.sccon.geospatial.domain.model.Pessoa;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class PessoaMemoriaService {

    private final Map<Long, Pessoa> pessoaMap = new ConcurrentHashMap<>();
    private Long currentId = 1L;
    
    @PostConstruct
    public void init() {
        inicializarPessoas();
    }
    
    private void inicializarPessoas() {

        Pessoa pessoa1 = new Pessoa(
            "João Silva",
            LocalDate.of(1990, 5, 15),
            LocalDate.of(2015, 3, 10)
        );

        Pessoa pessoa2 = new Pessoa(
            "Maria Oliveira",
            LocalDate.of(1985, 8, 22),
            LocalDate.of(2010, 7, 1)
        );
        

        Pessoa pessoa3 = new Pessoa(
            "Carlos Santos",
            LocalDate.of(1995, 12, 3),
            LocalDate.of(2018, 1, 15)
        );

        salvarPessoa(pessoa1);
        salvarPessoa(pessoa2);
        salvarPessoa(pessoa3);
        
        log.info("=== MAPA DE PESSOAS INICIALIZADO ===");
        log.info("Total de pessoas carregadas: {}", pessoaMap.size());
        listarTodasPessoas();
    }
    

    public Pessoa salvarPessoa(Pessoa pessoa) {
        if (pessoa.getId() == null) {
            pessoa.setId(currentId++);
        }
        pessoaMap.put(pessoa.getId(), pessoa);
        return pessoa;
    }

    public List<Pessoa> buscarTodas() {
        log.info("buscarTodas()");
        return pessoaMap.values().stream()
                .sorted(Comparator.comparing(Pessoa::getNome, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private void listarTodasPessoas() {
        pessoaMap.forEach((id, pessoa) -> {
            log.info("ID: {} | Nome: {} | Nascimento: {} | Admissão: {}",
                id,
                pessoa.getNome(),
                pessoa.getDataNascimento(),
                pessoa.getDataAdmissao()
            );
        });
    }
}