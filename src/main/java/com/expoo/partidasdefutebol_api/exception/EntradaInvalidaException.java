package com.expoo.partidasdefutebol_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class EntradaInvalidaException extends RuntimeException {
  public EntradaInvalidaException(String mensagem) {
    super(mensagem);
  }

  @ExceptionHandler(EntradaInvalidaException.class)
  public ResponseEntity<Object> tratarEntradaInvalidaException(EntradaInvalidaException ex) {
    String mensagemErro = "Entrada inválida: " + ex.getMessage();
    return new ResponseEntity<>(mensagemErro, HttpStatus.BAD_REQUEST);
  }
}
