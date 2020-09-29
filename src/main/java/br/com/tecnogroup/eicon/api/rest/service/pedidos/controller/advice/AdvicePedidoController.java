package br.com.tecnogroup.eicon.api.rest.service.pedidos.controller.advice;


import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.FormatterUtil;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ResourceStatusNotFoundException;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class AdvicePedidoController extends ResponseEntityExceptionHandler {

    private static final String CHAVE_MAP_TIMESTAMP = "timestamp";

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handlerNotFoundFromServiceExceptions(ServiceException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handlerInternalServerErrorFromExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<Object> handlerPedidoNotFoundException(PedidoNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceStatusNotFoundException.class)
    public ResponseEntity<Object> handlerNoDataFoundException(ResourceStatusNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(CHAVE_MAP_TIMESTAMP, FormatterUtil.toStringLocalDateTimeFormatada(LocalDateTime.now()));
        body.put("status_code", status.value());
        body.put("status", status.toString());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getBody(String message, HttpStatus httpStatus) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put(CHAVE_MAP_TIMESTAMP, FormatterUtil.toStringLocalDateTimeFormatada(LocalDateTime.now()));
        body.put("message", message);
        body.put("status_code", httpStatus.value());
        body.put("status", httpStatus.toString());

        return body;
    }
}
