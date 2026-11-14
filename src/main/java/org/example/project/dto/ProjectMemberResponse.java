package org.example.project.dto;

import org.example.user.RoleType;

public record ProjectMemberResponse(Long id, Long userId, String username, RoleType role) {
}
