package com.libraryproject.main.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTO {
	private Long bookId;
	private String userName;
	private String bookTitle;
	private LocalDate tookDate;
	private LocalDate dueDate;
	private LocalDate returnedDate;
	private Long fineAmount;
}
