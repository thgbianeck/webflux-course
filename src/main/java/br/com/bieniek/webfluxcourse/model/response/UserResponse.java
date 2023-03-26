package br.com.bieniek.webfluxcourse.model.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String name,
        String email,
        String password
) {}
