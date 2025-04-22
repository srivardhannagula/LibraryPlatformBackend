package com.libraryproject.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LibraryUser {
	@Id
	@Column(name="Username")
	private String username;
	@Column(name = "Password")
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
	
}
