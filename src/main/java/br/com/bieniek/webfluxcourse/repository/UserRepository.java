package br.com.bieniek.webfluxcourse.repository;

import br.com.bieniek.webfluxcourse.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(final User user);

    Mono<User> findById(String id);

    Flux<User> findAll();

    Mono<User> findAndRemove(String id);
}
