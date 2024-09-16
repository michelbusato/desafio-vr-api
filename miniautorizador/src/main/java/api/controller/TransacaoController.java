package api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.TransacaoDTO;
import api.dto.TransacaoRequestDTO;
import api.service.TransacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping( "/transacoes" )
@Tag( name = "Transações", description = "Cadastro de Transações" )
@Validated
public class TransacaoController {

    
	TransacaoService transacaoService;
	
	public TransacaoController(TransacaoService transacaoService) {
		this.transacaoService = transacaoService; 
	}

    @GetMapping( value = "/{id}" )
    public ResponseEntity< TransacaoDTO > findById(@PathVariable(name = "id", required = true) Long id) {
        return new ResponseEntity< >(transacaoService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity< List< TransacaoDTO > > findAll() {
        return new ResponseEntity< >( transacaoService.findAll(), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity< String > create(@Valid @RequestBody TransacaoRequestDTO criaTransacaoModel) {
        return new ResponseEntity< >(transacaoService.save(criaTransacaoModel), HttpStatus.CREATED );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity< String > delete( @PathVariable( "id" ) Long id ) {
        return new ResponseEntity< >( transacaoService.delete( id ), HttpStatus.OK );
    }

}