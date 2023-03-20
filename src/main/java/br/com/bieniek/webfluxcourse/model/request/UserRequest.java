package br.com.bieniek.webfluxcourse.model.request;

public record UserRequest(
        String name,
        String email,
        String password
) {}
