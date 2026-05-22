package com.eventhub.dto.auth;

import com.eventhub.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "Request para registro de usuÃ¡rio")
public record RegisterRequestDTO(
        @Schema(example = "John Doe")
        @NotBlank @Size(max = 120) String name,
        
        @Schema(example = "john.doe@email.com")
        @NotBlank @Email @Size(max = 150) String email,
        
        @Schema(example = "StrongPass@123")
        @NotBlank @Size(min = 8, max = 100) String password,
        
        @Schema(description = "PapÃ©is do usuÃ¡rio (opcional)", example = "[\"user\", \"admin\"]")
        Set<Role> roles
) {}
