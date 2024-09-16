package api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import api.model.Transacao;

import java.util.List;

@Repository
public interface TransacaoRepository extends CrudRepository<Transacao, Long > {

    List<Transacao> findAll();

}
