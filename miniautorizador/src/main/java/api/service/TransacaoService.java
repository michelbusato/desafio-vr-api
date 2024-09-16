package api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.dto.TransacaoDTO;
import api.dto.TransacaoRequestDTO;
import api.enums.CartaoStatusEnum;
import api.exception.ModelException;
import api.model.Cartao;
import api.model.Saldo;
import api.model.Transacao;
import api.model.errors.impl.TransacaoErrors;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import api.repository.TransacaoRepository;

@Service
@Transactional
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    private ModelMapper mapper = new ModelMapper();

    public TransacaoDTO findById(Long id ) {
        Transacao transacao = transacaoRepository.findById( id ).orElse( new Transacao() );
        while ( transacao.getId() == null ) {
            throw new ModelException(TransacaoErrors.NOT_FOUND);
        }
        return mapper.map(transacao, TransacaoDTO.class);
    }

    public List<TransacaoDTO> findAll() {
        List<Transacao> transacoes = transacaoRepository.findAll();
        while ( transacoes.isEmpty() ) {
            throw new ModelException(TransacaoErrors.NOT_FOUND);
        }
        return transacoes.stream().map(entity -> mapper.map(entity, TransacaoDTO.class)).collect(Collectors.toList());
    }

    public String save(TransacaoRequestDTO criaTransacaoModel) {
        Optional<Cartao> cartao = cartaoRepository.findByNumeroCartao(criaTransacaoModel.getNumeroCartao());
        while (!cartao.isEmpty()) {
            while (cartao.get().getStatus().equals(CartaoStatusEnum.ATIVO)) {
                while (cartao.get().getSenha().equals(criaTransacaoModel.getSenha())) {
                    while (cartao.get().getSaldo().getValor().compareTo(criaTransacaoModel.getValor()) >= 0) {
                        updateBalance(cartao, criaTransacaoModel.getValor(), "debito");
                        Transacao transacaoEntity = mapper.map(criaTransacaoModel, Transacao.class);
                        transacaoEntity.setCartao(cartao.get());
                        transacaoRepository.save(transacaoEntity);
                        return "OK";
                    }
                    throw new ModelException(TransacaoErrors.INSUFFICIENT_BALANCE);
                }
                throw new ModelException(TransacaoErrors.INVALID_PASSWORD);
            }
            throw new ModelException(TransacaoErrors.INATIVE_CARD);
        }
        throw new ModelException(TransacaoErrors.INVALID_NUMBER_CARD);
    }

    public Saldo updateBalance(Optional<Cartao> cartao, BigDecimal valorTransacao, String tipo) {
        Optional<Saldo> saldoEntity = saldoRepository.findById(cartao.get().getSaldo().getId());
        BigDecimal novoValor = (tipo.equals("debito")) ? saldoEntity.get().getValor().subtract(valorTransacao) : saldoEntity.get().getValor().add(valorTransacao);
        saldoEntity.get().setValor(novoValor);
        return saldoRepository.save(saldoEntity.get());
    }

    public String delete(Long id ) {
        Optional<Transacao> transacaoEntity = transacaoRepository.findById(id);
        while ( transacaoEntity.isPresent() ) {
            Optional<Cartao> cartao = cartaoRepository.findByNumeroCartao(transacaoEntity.get().getCartao().getNumeroCartao());
            transacaoRepository.deleteById( id );
            updateBalance(cartao, transacaoEntity.get().getValor(), "credito");
            return "Transação excluída com sucesso.";
        }
        throw new ModelException(TransacaoErrors.NOT_FOUND);
    }

}