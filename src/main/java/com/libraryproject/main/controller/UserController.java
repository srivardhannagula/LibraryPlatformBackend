package com.libraryproject.main.controller;


import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libraryproject.main.entity.Books;
import com.libraryproject.main.entity.CategoryRequest;
import com.libraryproject.main.entity.UserBookCount;
import com.libraryproject.main.entity.UserProfileDTO;
import com.libraryproject.main.service.BooksService;
 

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private BooksService booksService;
	@GetMapping("/viewBooks")
	public List<Books> viewBooksByUser() {
		  
		return  booksService.getAllBooks();
	}
	@PostMapping("/viewBooksByCategory")
	public List<Books> viewBooksByCategory(@RequestBody CategoryRequest categoryRequest) {
		
		return  booksService.getByCategory(categoryRequest.getCategory());
	}
	@GetMapping("/searchBooks")
	public ResponseEntity<List<Books>>  searchBooks(@RequestParam String query) {
		List<Books> booksList=booksService.searchBooks(query);
		return ResponseEntity.ok(booksList);
	}
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(){
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		String  username=authentication.getName();
		  System.out.println(username+" controller");
		 UserBookCount userBookCount= booksService.getUserBookCount(username);
		 Long bookCount=(userBookCount!=null?userBookCount.getBookCount():0L);
		 System.out.println(username+" controller"+ bookCount);
			return ResponseEntity.ok(new UserProfileDTO(username,bookCount));	
	}
}
