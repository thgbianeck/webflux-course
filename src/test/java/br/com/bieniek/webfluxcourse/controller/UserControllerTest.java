package br.com.bieniek.webfluxcourse.controller;

import br.com.bieniek.webfluxcourse.entity.User;
import br.com.bieniek.webfluxcourse.mapper.UserMapper;
import br.com.bieniek.webfluxcourse.model.request.UserRequest;
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
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Test endpoint save with success")
    void testSaveWithSuccess() {
        final var request = UserRequest.builder()
                .name("Thiago")
                .email("thiagobianeck@gmail.com")
                .password("123456")
                .build();

        when(userService.save(any(UserRequest.class)))
                .thenReturn(Mono.just(User.builder().build()));

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
                .name(" Thiago")
                .email("thiagobianeck@gmail.com")
                .password("123456")
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
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}