package br.com.bieniek.webfluxcourse.repository;

import br.com.bieniek.webfluxcourse.entity.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(final User user);
}