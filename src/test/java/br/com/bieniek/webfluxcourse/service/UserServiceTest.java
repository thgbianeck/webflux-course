package br.com.bieniek.webfluxcourse.service;

import br.com.bieniek.webfluxcourse.entity.User;
import br.com.bieniek.webfluxcourse.mapper.UserMapper;
import br.com.bieniek.webfluxcourse.model.request.UserRequest;
import br.com.bieniek.webfluxcourse.repository.UserRepository;
import br.com.bieniek.webfluxcourse.service.exception.ObjectNotFoundException;
import br.com.bieniek.webfluxcourse.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = userService.save(request);

        StepVerifier.create(result) // Create a StepVerifier
                .expectNextMatches(user -> user.getClass().equals(User.class)) // Expect a User class
                .expectComplete() // Expect the stream to complete
                .verify(); // Verify the stream

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should find a user by id")
    void shouldFindById() {
        final String id = "1";

        when(userRepository.findById(anyString())).thenReturn(Mono.just(User.builder().id(id).build()));

        Mono<User> result = userService.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(user -> Objects.nonNull(user)
                        && user.getClass().equals(User.class)
                        && user.getId().equals(id))
                .expectComplete()
                .verify();

        verify(userRepository, times(1)).findById(anyString());

    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAll() {
        final String id = "1";

        when(userRepository.findAll()).thenReturn(Flux.just(User.builder().id(id).build()));

        Flux<User> result = userService.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> Objects.nonNull(user)
                        && user.getClass().equals(User.class))
                .expectComplete()
                .verify();

        verify(userRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Should update a user")
    void shouldUpdateAUser() {
        UserRequest request = new UserRequest("Thiago", "thiagobianeck@gmail.com", "123456");
        User entity =  User.builder().build();

        when(mapper.toEntity(any(UserRequest.class), any(User.class))).thenReturn(entity);
        when(userRepository.findById(anyString())).thenReturn(Mono.just(entity));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(entity));

        Mono<User> result = userService.update("123", request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass().equals(User.class))
                .expectComplete()
                .verify();

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete a user")
    void shouldDeleteAUser() {
        final String id = "1";

        User entity = User.builder().id(id).build();
        when(userRepository.findAndRemove(anyString())).thenReturn(Mono.just(entity));

        Mono<User> result = userService.delete(id);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass().equals(User.class))
                .expectComplete()
                .verify();

        verify(userRepository, times(1)).findAndRemove(anyString());
    }

    @Test
    @DisplayName("Should handle not found")
    void testHandleNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());

        try{
            userService.findById("123").block();
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Object not found: Id: 123, Type: User", e.getMessage());
        }
    }
}