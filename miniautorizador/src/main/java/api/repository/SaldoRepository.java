package api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import api.model.Saldo;

@Repository
public interface SaldoRepository extends CrudRepository<Saldo, Long > {

}
