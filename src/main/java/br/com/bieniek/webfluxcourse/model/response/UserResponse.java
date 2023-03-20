package br.com.bieniek.webfluxcourse.model.response;

public record UserResponse(
        String id,
        String name,
        String email,
        String password
) {}
