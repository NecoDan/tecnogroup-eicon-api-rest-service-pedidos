package br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceStatusNotFoundException extends RuntimeException {
    public ResourceStatusNotFoundException(String s) {
        super("Resource {NOT FOUND - NÃO ENCONTRADO} - ".concat(s));
    }
}
