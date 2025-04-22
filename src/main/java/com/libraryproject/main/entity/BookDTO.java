package com.libraryproject.main.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
	private Long id;
	private String bookTitle;
	private String authorName;
	private Integer booksAvailable;
	private String category;
}
