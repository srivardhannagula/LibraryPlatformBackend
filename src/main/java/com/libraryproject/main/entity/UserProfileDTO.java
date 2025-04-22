package com.libraryproject.main.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
	public UserProfileDTO(String username,Long numberOfBooksBorrowed) {
		this.username=username;
		this.numberOfBooksBorrowed=numberOfBooksBorrowed;
	}
	private String username;
	 
	private Long numberOfLibraryUsers;
	private Long numberOfBooksBorrowed;
}
