package org.example.dto;

import java.util.Set;

import lombok.Data;

@Data
public class LoginResponseDto {
	private String jwt;
	private String username;
	private Set<String> roles;
}
