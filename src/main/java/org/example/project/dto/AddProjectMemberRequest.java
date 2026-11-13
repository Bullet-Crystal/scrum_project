package org.example.project.dto;

import org.example.user.RoleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddProjectMemberRequest(
        @NotBlank(message = "Username is required") String username,
        @NotNull(message = "Role is required") RoleType role) {
}
