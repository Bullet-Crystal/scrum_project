package org.example.sprint.dto;

import org.example.sprint.Status;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
        @NotNull(message = "Status is required") Status status) {
}
