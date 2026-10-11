package org.taxi.clientes.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteRequest(
    @NotBlank String nombre,
    @NotBlank @Pattern(regexp = "[0-9]{8}") String dni,
    @NotBlank String telefono,
    @NotBlank @Email String email
) {}
