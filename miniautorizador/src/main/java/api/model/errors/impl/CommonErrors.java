package api.model.errors.impl;

import org.springframework.http.HttpStatus;

import api.dto.errors.ErrorModel;

public class CommonErrors {

    public static final ErrorModel INVALID_PARAMETERS = new ErrorModel(HttpStatus.BAD_REQUEST.value(), "400000", "Parâmetro inválido");

    public static final ErrorModel UNAUTHORIZED = new ErrorModel(HttpStatus.UNAUTHORIZED.value(), "401000", "Não autorizado");

    public static final ErrorModel UNEXPECTED_ERROR = new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500000", "Erro inesperado");

}
