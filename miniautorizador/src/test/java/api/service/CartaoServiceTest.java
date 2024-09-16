package api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import api.ApplicationTests;
import api.dto.CartaoDTO;
import api.dto.CartaoRequestDTO;
import api.enums.CartaoStatusEnum;
import api.model.Cartao;
import api.model.Saldo;
import api.repository.CartaoRepository;

public class CartaoServiceTest extends ApplicationTests {

    @Autowired
    private CartaoService cartaoService;

    @MockBean
    private CartaoRepository cartaoRepository;

    private Saldo saldo = new Saldo();

    private ModelMapper mapper = new ModelMapper();

    @Test
    @DisplayName("Cria o Cartão com sucesso")
    void testSaveCartao() {
        CartaoRequestDTO mockCartaoModel = CartaoRequestDTO.builder()
                .numeroCartao("1111111111")
                .senha(null)
                .build();

        Cartao cartaoEntity = mapper.map(mockCartaoModel, Cartao.class);

        when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartaoEntity);

        CartaoDTO saveCartaoModel = cartaoService.save(mockCartaoModel);
        CartaoRequestDTO criaCartaoModel = mapper.map(saveCartaoModel, CartaoRequestDTO.class);

        Assertions.assertNotNull(cartaoEntity);
        assertEquals(mockCartaoModel, criaCartaoModel);
    }

    @Test
    @DisplayName("Localiza todos os Cartões por Status com sucesso")
    void testFindAllByStatus() {
        List<Cartao> mockListCartoesEntities = Stream.of(
                        Cartao.builder()
                                .id(1L)
                                .numeroCartao("1111111111")
                                .saldo(saldo)
                                .status(CartaoStatusEnum.ATIVO)
                                .build(),
                        Cartao.builder()
                                .id(2L)
                                .numeroCartao("2222222222")
                                .saldo(saldo)
                                .status(CartaoStatusEnum.ATIVO)
                                .build())
                .collect(Collectors.toList());

        when(cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatusEnum.ATIVO))
                .thenReturn(mockListCartoesEntities);

        List<CartaoDTO> cartoes = cartaoService.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatusEnum.ATIVO);
        List< Cartao > listaCartoes = cartoes.stream().map(entity -> mapper.map(entity, Cartao.class)).collect(Collectors.toList());

        Assertions.assertNotNull(listaCartoes);
        assertEquals(mockListCartoesEntities, listaCartoes);
    }

    @Test
    @DisplayName("Localiza todos os Cartões por Status inválido")
    void testFindAllByStatusInvalid() {
        List<Cartao> mockListCartoesEntities = Stream.of(
                        Cartao.builder()
                                .id(1L)
                                .numeroCartao("1111111111")
                                .saldo(saldo)
                                .status(CartaoStatusEnum.ATIVO)
                                .build(),
                        Cartao.builder()
                                .id(2L)
                                .numeroCartao("2222222222")
                                .saldo(saldo)
                                .status(CartaoStatusEnum.ATIVO)
                                .build())
                .collect(Collectors.toList());

        when(cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatusEnum.ATIVO))
                .thenReturn(mockListCartoesEntities);

        try {
            List<CartaoDTO> cartoes = cartaoService.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatusEnum.INATIVO);
            List< Cartao > listaCartoes = cartoes.stream().map(entity -> mapper.map(entity, Cartao.class)).collect(Collectors.toList());
            assertNotEquals(mockListCartoesEntities, listaCartoes);
        } catch (Exception e) {
            assertEquals("Nenhum cartão encontrado.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Localiza o Cartão por ID com sucesso")
    void testFindCartaoById() {
        Cartao mockCartaoEntity = Cartao.builder()
                .id(1L)
                .numeroCartao("1111111111")
                .saldo(saldo)
                .status(CartaoStatusEnum.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        CartaoDTO findCartao = cartaoService.findCartaoById(1L);
        Cartao cartaoEntity = mapper.map(findCartao, Cartao.class);

        Assertions.assertNotNull(cartaoEntity);
        assertEquals(mockCartaoEntity, cartaoEntity);
    }

    @Test
    @DisplayName("Localiza o Cartão por ID inválido")
    void testFindCartaoByIdInvalid() {
        Cartao mockCartaoEntity = Cartao.builder()
                .id(1L)
                .numeroCartao("1111111111")
                .saldo(saldo)
                .status(CartaoStatusEnum.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        try {
            cartaoService.findCartaoById(2L);
        } catch (Exception e) {
            assertEquals("Nenhum cartão encontrado.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Localiza o Cartão por Número do Cartão com sucesso")
    void testFindCartaoByNumber() {
        Cartao mockCartaoEntity = Cartao.builder()
                .numeroCartao("1111111111")
                .saldo(saldo)
                .status(CartaoStatusEnum.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("1111111111")).thenReturn(Optional.of(mockCartaoEntity));

        BigDecimal findCartao = cartaoService.findByNumeroCartao("1111111111");

        Assertions.assertNotNull(findCartao);
        assertEquals(mockCartaoEntity.getSaldo().getValor(), findCartao);
    }

    @Test
    @DisplayName("Localiza o Cartão por Número do Cartão inválido")
    void testFindCartaoByNumberInvalid() {
        Cartao mockCartaoEntity = Cartao.builder()
                .numeroCartao("1111111111")
                .saldo(saldo)
                .status(CartaoStatusEnum.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("1111111111")).thenReturn(Optional.of(mockCartaoEntity));

        try {
            cartaoService.findByNumeroCartao("555555555");
        } catch (Exception e) {
            assertEquals("", e.getMessage());
        }
    }

    @Test
    @DisplayName("Altera o Cartão por ID com sucesso")
    void testUpdateCartao() {
        Cartao mockCartaoEntity = Cartao.builder()
                .numeroCartao("1111111111")
                .senha("xxxxxxxx")
                .saldo(saldo)
                .status(CartaoStatusEnum.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        mockCartaoEntity.setNumeroCartao("3333333333");
        when(cartaoRepository.save(any(Cartao.class))).thenReturn(mockCartaoEntity);

        CartaoDTO updateCartaoModel = cartaoService.update(1L, mockCartaoEntity);
        Cartao cartaoEntity = mapper.map(updateCartaoModel, Cartao.class);
        cartaoEntity.setSaldo(mockCartaoEntity.getSaldo());
        cartaoEntity.setSenha(mockCartaoEntity.getSenha());

        Assertions.assertNotNull(cartaoEntity);
        assertEquals(mockCartaoEntity, cartaoEntity);
    }

    @Test
    @DisplayName("Altera o Cartão por ID inválido")
    void testUpdateCartaoIdInvalid() {
        Cartao mockCartaoEntity = Cartao.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .saldo(saldo)
                .status(CartaoStatusEnum.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        mockCartaoEntity.setNumeroCartao("44444444444");
        when(cartaoRepository.save(any(Cartao.class))).thenReturn(mockCartaoEntity);

        try {
            cartaoService.update(2L, mockCartaoEntity);
        } catch (Exception e) {
            assertEquals("Nenhum cartão encontrado.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Exclui o Cartão por ID com sucesso")
    void testDeleteCartaoById() {
        Cartao mockCartaoEntity = Cartao.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .saldo(saldo)
                .status(CartaoStatusEnum.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        String deleteCartao = cartaoService.delete(1L);

        Assertions.assertNotNull(deleteCartao);
        assertEquals("Cartão excluído com sucesso.", deleteCartao);
    }

    @Test
    @DisplayName("Exclui o Cartão por ID inválido")
    void testDeleteCartaoByIdInvalid() {
        Cartao mockCartaoEntity = Cartao.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .saldo(saldo)
                .status(CartaoStatusEnum.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        try {
            cartaoService.delete(2L);
        } catch (Exception e) {
            assertEquals("Nenhum cartão encontrado.", e.getMessage());
        }
    }

}
