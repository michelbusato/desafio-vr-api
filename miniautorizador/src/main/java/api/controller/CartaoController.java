package api.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.CartaoDTO;
import api.dto.CartaoRequestDTO;
import api.enums.CartaoStatusEnum;
import api.model.Cartao;
import api.service.CartaoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping( "/cartoes" )
@Tag( name = "Cartões", description = "Cadastro de Cartões" )
@Validated
public class CartaoController {


    private CartaoService cartaoService;

    
    public CartaoController(CartaoService cartaoService) {

    	this.cartaoService = cartaoService;
    
    }
    
    @GetMapping( value = "/{numeroCartao}" )
    public ResponseEntity< BigDecimal > findByNumeroCartao(@PathVariable(name = "numeroCartao") String numeroCartao) {
        return new ResponseEntity< >(cartaoService.findByNumeroCartao(numeroCartao), HttpStatus.OK);
    }

    @GetMapping( value = "/{id}" )
    public ResponseEntity< CartaoDTO > findCartaoById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity< >(cartaoService.findCartaoById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity< List< CartaoDTO > > findAll() {
        return new ResponseEntity< >( cartaoService.findAllByOrderByNumeroCartaoAsc(), HttpStatus.OK );
    }

    @GetMapping( value = "/{status}" )
    public ResponseEntity< List< CartaoDTO > > findAllByStatusOrderByNumeroCartaoAsc( @PathVariable( "status" ) String status ) {
        return new ResponseEntity< >( cartaoService.findAllByStatusOrderByNumeroCartaoAsc( CartaoStatusEnum.fromValue( status ) ), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity< CartaoDTO > create( @Valid @RequestBody CartaoRequestDTO criaCartaoModel) {
        return new ResponseEntity< >(cartaoService.save(criaCartaoModel), HttpStatus.CREATED );
    }

    @PutMapping( value = "/{id}" )
    public ResponseEntity< CartaoDTO > update( @PathVariable( "id" ) Long id, @Valid @RequestBody Cartao cartaoEntity) {
        return new ResponseEntity< >(cartaoService.update( id, cartaoEntity), HttpStatus.OK );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity< String > delete( @PathVariable( "id" ) Long id ) {
        return new ResponseEntity< >( cartaoService.delete( id ), HttpStatus.OK );
    }

}