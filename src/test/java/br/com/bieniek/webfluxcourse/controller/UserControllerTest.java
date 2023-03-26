package br.com.bieniek.webfluxcourse.controller;

import br.com.bieniek.webfluxcourse.entity.User;
import br.com.bieniek.webfluxcourse.mapper.UserMapper;
import br.com.bieniek.webfluxcourse.model.request.UserRequest;
import br.com.bieniek.webfluxcourse.model.response.UserResponse;
import br.com.bieniek.webfluxcourse.service.UserService;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest {

    public static final String ID = "123456";
    public static final String NAME = "Thiago";
    public static final String EMAIL = "thiagobianeck@gmail.com";
    public static final String PASSWORD = "123456";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;

    @Test
    @DisplayName("Test endpoint save with success")
    void testSaveWithSuccess() {
        final var request = UserRequest.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        when(userService.save(any(UserRequest.class)))
                .thenReturn(just(User.builder().build()));

        webTestClient.post()
                .uri("/users")
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        verify(userService).save(any(UserRequest.class));
    }

    @Test
    @DisplayName("Test endpoint save with bad request")
    void testSaveWithBadRequest() {
        final var request = UserRequest.builder()
                .name(" " + NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        webTestClient.post()
                .uri("/users")
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path")
                    .isEqualTo("/users")
                .jsonPath("$.status")
                    .isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error")
                    .isEqualTo("Validation error")
                .jsonPath("$.message")
                    .isEqualTo("Error on validations attributes")
                .jsonPath("$.errors[0].fieldName")
                    .isEqualTo("name")
                .jsonPath("$.errors[0].message")
                    .isEqualTo("Field cannot have blank spaces at the beginning or end");

    }

    @Test
    @DisplayName("Test endpoint findById with success")
    void testFIndByIdWithSucess() {

        final var userResponse = UserResponse.builder()
                .id(ID).name(NAME).email(EMAIL).password(PASSWORD).build();

        when(userService.findById(any(String.class)))
                .thenReturn(just(User.builder().build()));
        when(mapper.toResponse(any(User.class)))
                .thenReturn(userResponse);

        webTestClient.get().uri("/users/{id}", ID)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);

    }

    @Test
    @DisplayName("Test endpoint findAll with success")
    void testFindAllWithSuccess() {
        final var userResponse = UserResponse.builder()
                .id(ID).name(NAME).email(EMAIL).password(PASSWORD).build();

        when(userService.findAll())
                .thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class)))
                .thenReturn(userResponse);

        webTestClient.get().uri("/users")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(ID)
                .jsonPath("$[0].name").isEqualTo(NAME)
                .jsonPath("$[0].email").isEqualTo(EMAIL)
                .jsonPath("$[0].password").isEqualTo(PASSWORD);
    }

}