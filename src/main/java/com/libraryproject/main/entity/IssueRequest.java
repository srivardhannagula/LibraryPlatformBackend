package com.libraryproject.main.entity;

import lombok.Data;

@Data
public class IssueRequest {
	private Long bookId;
	private String username;
}
