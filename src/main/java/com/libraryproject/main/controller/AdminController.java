package com.libraryproject.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libraryproject.main.entity.BookDTO;
import com.libraryproject.main.entity.Books;
import com.libraryproject.main.entity.IssueDTO;
import com.libraryproject.main.entity.IssueRequest;
import com.libraryproject.main.entity.IssuedBooks;
import com.libraryproject.main.entity.LibraryUser;
import com.libraryproject.main.entity.UserBookCount;
import com.libraryproject.main.entity.UserProfileDTO;
import com.libraryproject.main.service.BooksService;
 


@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	private BooksService booksService;
	
	 
	
	@PostMapping("/addBooks")
	public void addBooks(@RequestBody Books books) {
		System.out.println("hello im book");
		booksService.saveBooks(books);
	}
	@GetMapping("/viewBooks")
	public List<Books>  viewBooks() {
		List<Books> booksList=booksService.getAllBooks();
		return booksList;
	}
	@DeleteMapping("/deleteBook/{id}")	
	public ResponseEntity<String> deleteBook(@PathVariable("id") Long id){
		if(booksService.deleteBookById(id)) {
			return ResponseEntity.ok("Books Deleted Successfully");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Books not exists");
	}
	 
	@PutMapping("/increaseBook/{id}")
	public ResponseEntity<BookDTO> increaseBook(@PathVariable("id")Long id){
		 Books upBook=booksService.increaseNoOfBooks(id);
		 BookDTO updatedBook=new BookDTO(upBook.getId(),upBook.getBookTitle(),upBook.getAuthorName(),upBook.getBooksAvailable(),upBook.getCategory());
		return ResponseEntity.ok(updatedBook);
	}
	@PutMapping("/decreaseBook/{id}")
	public ResponseEntity<BookDTO> decreaseBook(@PathVariable("id")Long id){
		Books upBook= booksService.decreaseNoOfBooks(id);
		BookDTO updatedBook=new BookDTO(upBook.getId(),upBook.getBookTitle(),upBook.getAuthorName(),upBook.getBooksAvailable(),upBook.getCategory());
		return ResponseEntity.ok(updatedBook);
	}
	
	@GetMapping("/searchBooks")
	public ResponseEntity<List<Books>>  searchBooks(@RequestParam String query) {
		List<Books> booksList=booksService.searchBooks(query);
		return ResponseEntity.ok(booksList);
	}
	
	@PostMapping("/issueBook")
	public ResponseEntity<?> issueBooks(@RequestBody IssueRequest issueRequest){
		System.out.println(issueRequest.getUsername());
		return booksService.issueBooksService(issueRequest.getUsername(), issueRequest.getBookId());
	}
	@GetMapping("/issueBooks")
	public ResponseEntity<List<IssuedBooks>> issueBooks(){
		 
		return ResponseEntity.ok(booksService.issueAllBooksService());
	}
	@GetMapping("/fetchUsers")
	public ResponseEntity<List<UserBookCount>> getAllUsers(){
		System.out.println(booksService.getUsers());
		return ResponseEntity.ok(booksService.getUsers());
	}
	@PostMapping("/returnBook/{id}")
	public ResponseEntity<?> returnBooksEntity(@PathVariable("id")Long id){
		try {
		IssuedBooks books= booksService.getReturnBooksById(id);
		IssueDTO issueDTO=new IssueDTO(books.getBookId(),books.getUserName(),books.getBookTitle(),books.getTookDate(),books.getDueDate(),books.getReturnedDate(),books.getFineAmount());
		return ResponseEntity.ok(issueDTO);}
		catch (RuntimeException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	@PutMapping("/returnIssuedBooks/{issueId}/{bookId}")
	public ResponseEntity<?> returnBook(@PathVariable Long issueId,@PathVariable Long bookId){
		System.out.println("Hello");
		return booksService.returningBook(issueId,bookId);
	}
	@DeleteMapping("/deleteIssued/{issueId}/{bookId}")
	public ResponseEntity<?> deleteIssuedBook(@PathVariable Long issueId,@PathVariable Long bookId){
			booksService.deleteReturnedIssuedBood(issueId,bookId);
			return ResponseEntity.ok().build();
	}
	
	
	
	
	
	
	
	
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(){
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		String  username=authentication.getName();
		 
//		  System.out.println(username+" controller");
//		 UserBookCount userBookCount= booksService.getUserBookCount(username);
//		 Long bookCount=(userBookCount!=null?userBookCount.getBookCount():0L);
//		 System.out.println(username+" controller"+ bookCount);
		return ResponseEntity.ok(new UserProfileDTO(username,booksService.getUserCount(),booksService.booksIssuedCount()));	
	}
	
	
	
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<LibraryUser>> findAllUsers(){
		return ResponseEntity.ok(booksService.getAllUsers());
	}
}
