package br.com.bieniek.webfluxcourse.service.impl;

import br.com.bieniek.webfluxcourse.entity.User;
import br.com.bieniek.webfluxcourse.mapper.UserMapper;
import br.com.bieniek.webfluxcourse.model.request.UserRequest;
import br.com.bieniek.webfluxcourse.repository.UserRepository;
import br.com.bieniek.webfluxcourse.service.UserService;
import br.com.bieniek.webfluxcourse.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public Mono<User> save(UserRequest request) {
        return userRepository.save(mapper.toEntity(request));
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ObjectNotFoundException(id, User.class))
                );
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> update(String id, UserRequest request) {
        return findById(id)
                .map(entity -> mapper.toEntity(request, entity))
                .flatMap(userRepository::save);
    }
}
