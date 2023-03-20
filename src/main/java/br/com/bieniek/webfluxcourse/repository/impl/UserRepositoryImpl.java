package br.com.bieniek.webfluxcourse.repository.impl;

import br.com.bieniek.webfluxcourse.entity.User;
import br.com.bieniek.webfluxcourse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<User> save(User user) {
        return mongoTemplate.save(user);
    }
}
