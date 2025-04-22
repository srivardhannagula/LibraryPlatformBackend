package com.libraryproject.main.repositories;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.misc.TestRig;
import org.springframework.data.jpa.repository.JpaRepository;

import com.libraryproject.main.entity.Books;

public interface BooksRepository extends JpaRepository<Books, Long> {
	public List<Books> findByCategory(String category);
	public List<Books>   findByBookTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(String title,String author);
	
}
