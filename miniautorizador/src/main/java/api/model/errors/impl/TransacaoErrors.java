package api.model.errors.impl;

import org.springframework.http.HttpStatus;

import api.dto.errors.ErrorModel;

public class TransacaoErrors {

    public static final ErrorModel NOT_FOUND = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404001", "Nenhuma transação encontrada.");
    public static final ErrorModel INSUFFICIENT_BALANCE = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422001", "Saldo insuficiente para realizar a transação.");
    public static final ErrorModel INVALID_PASSWORD = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422001", "Senha inválida. Tente novamente.");
    public static final ErrorModel INVALID_NUMBER_CARD = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422001", "Número do cartão não encontrado. Tente novamente.");
    public static final ErrorModel INATIVE_CARD = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422001", "Cartão inativo.");

}