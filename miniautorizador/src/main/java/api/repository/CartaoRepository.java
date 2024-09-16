package api.repository;

import api.enums.CartaoStatusEnum;
import api.model.Cartao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartaoRepository extends CrudRepository<Cartao, Long > {

    Optional<Cartao> findByNumeroCartao(String numeroCartao );

    List<Cartao> findAllByOrderByNumeroCartaoAsc();

    List<Cartao> findAllByStatusOrderByNumeroCartaoAsc(CartaoStatusEnum status );

}
