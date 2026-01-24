package org.example.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RegisterRequestDto {
	private String username;
	private String password;
	private List<String> roles;
}
