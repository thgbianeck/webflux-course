package br.com.bieniek.webfluxcourse.service.exception;

import br.com.bieniek.webfluxcourse.entity.User;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public <T> ObjectNotFoundException(String id, Class<T> clazz) {
        super(String.format("Object not found: Id: %s, Type: %s", id, clazz.getSimpleName()));
    }

}
