package api.service;

import api.dto.CartaoDTO;
import api.dto.CartaoRequestDTO;
import api.enums.CartaoStatusEnum;
import api.exception.ModelException;
import api.model.Cartao;
import api.model.Saldo;
import api.model.errors.impl.CartaoErrors;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    private ModelMapper mapper = new ModelMapper();
    
    
    private static String CARTAO_EXCLUIDO = "Cartão excluído com sucesso!";

    public BigDecimal findByNumeroCartao(String numeroCartao ) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao).orElse( null );
        while ( cartao == null ) {
            throw new ModelException(CartaoErrors.INVALID_NUMBER_CARD);
        }
        return cartao.getSaldo().getValor();
    }

    public CartaoDTO findCartaoById( Long id ) {
        Cartao cartao = cartaoRepository.findById( id ).orElse( new Cartao() );
        while ( cartao.getId() == null ) {
            throw new ModelException(CartaoErrors.NOT_FOUND);
        }
        return mapper.map(cartao, CartaoDTO.class);
    }

    public List< CartaoDTO > findAllByOrderByNumeroCartaoAsc() {
        List<Cartao> cartoes = cartaoRepository.findAllByOrderByNumeroCartaoAsc();
        while ( cartoes.isEmpty() ) {
            throw new ModelException(CartaoErrors.NOT_FOUND);
        }
        return cartoes.stream().map(entity -> mapper.map(entity, CartaoDTO.class)).collect(Collectors.toList());
    }

    public List< CartaoDTO > findAllByStatusOrderByNumeroCartaoAsc( CartaoStatusEnum status ) {
        List<Cartao> cartoes = cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc( status );
        while ( cartoes.isEmpty() ) {
            throw new ModelException(CartaoErrors.NOT_FOUND);
        }
        return cartoes.stream().map(entity -> mapper.map(entity, CartaoDTO.class)).collect(Collectors.toList());
    }

    public CartaoDTO save(CartaoRequestDTO criaCartaoModel) {
        Cartao cartaoEntity = mapper.map(criaCartaoModel, Cartao.class);
        while ( isCardExist(cartaoEntity) ) {
            throw new ModelException(CartaoErrors.CARD_EXISTS);
        }
        try {
            Saldo saldoEntity = new Saldo();
            saldoRepository.save(saldoEntity);
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setStatus(CartaoStatusEnum.ATIVO);
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return mapper.map(cartaoEntity, CartaoDTO.class);
        } catch (Exception e) {
            throw new ModelException(CartaoErrors.ERROR_CREATING);
        }
    }

    public CartaoDTO update(Long id, Cartao cartaoEntity) {
        Cartao cartao = cartaoRepository.findById( id ).orElse( new Cartao() );
        while ( cartaoRepository.existsById( id ) ) {
            cartaoEntity.setId(id);
            Saldo saldoEntity = (cartao.getSaldo() != null) ? saldoRepository.findById( cartao.getSaldo().getId() ).orElse( new Saldo() ) : new Saldo();
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setNumeroCartao( cartaoEntity.getNumeroCartao() );
            cartaoEntity.setSenha( cartaoEntity.getSenha() );
            cartaoEntity.setStatus( (cartaoEntity.getStatus() != null) ? cartaoEntity.getStatus() : CartaoStatusEnum.ATIVO );
            cartaoEntity.setCreatedAt(cartao.getCreatedAt());
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return mapper.map(cartaoEntity, CartaoDTO.class);
        }
        throw new ModelException(CartaoErrors.NOT_FOUND);
    }

    public String delete( Long id ) {
        while ( cartaoRepository.existsById( id ) ) {
            cartaoRepository.deleteById( id );
            return CARTAO_EXCLUIDO;
        }
        throw new ModelException(CartaoErrors.NOT_FOUND);
    }

    public boolean isCardExist( Cartao cartaoEntity) {
        return cartaoRepository.findByNumeroCartao( cartaoEntity.getNumeroCartao() ).isPresent();
    }

}