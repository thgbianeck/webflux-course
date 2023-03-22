package br.com.bieniek.webfluxcourse.service;

import br.com.bieniek.webfluxcourse.entity.User;
import br.com.bieniek.webfluxcourse.mapper.UserMapper;
import br.com.bieniek.webfluxcourse.model.request.UserRequest;
import br.com.bieniek.webfluxcourse.repository.UserRepository;
import br.com.bieniek.webfluxcourse.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should save a user")
    void shouldSaveAUser() {
        UserRequest request = new UserRequest("Thiago", "thiagobianeck@gmail.com", "123456");
        User entity =  User.builder().build();

        // Return a User entity when the mapper is called
        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        // Return a Mono<User> with a non-null value when the repository is called
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = userService.save(request);

        StepVerifier.create(result) // Create a StepVerifier
                .expectNextMatches(Objects::nonNull) // Expect a non-null value
                .expectComplete() // Expect the stream to complete
                .verify(); // Verify the stream

        // Verify that the userRepository.save() method was called once
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }
}