package br.com.bieniek.webfluxcourse.service;

import br.com.bieniek.webfluxcourse.entity.User;
import br.com.bieniek.webfluxcourse.model.request.UserRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> save(final UserRequest request);
    Mono<User> findById(final String id);
    Flux<User> findAll();
    Mono<User> update(final String id, final UserRequest request);

}
