package com.libraryproject.main.service;
 
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.aspectj.weaver.loadtime.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.libraryproject.main.entity.Books;
import com.libraryproject.main.entity.IssuedBooks;
import com.libraryproject.main.entity.LibraryUser;
import com.libraryproject.main.entity.Role;
import com.libraryproject.main.entity.UserBookCount;
import com.libraryproject.main.repositories.BooksRepository;
import com.libraryproject.main.repositories.IssuedBooksRepository;
import com.libraryproject.main.repositories.UserRepository;

@Service
public class BooksService {
	@Autowired
	private BooksRepository booksRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private IssuedBooksRepository issuedBooksRepository;

	public void saveBooks(Books books) {
		booksRepository.save(books);
		
	}


	public List<Books> getAllBooks() {
		// TODO Auto-generated method stub
		return booksRepository.findAll();
	}
	public List<Books> getByCategory(String category){
		return booksRepository.findByCategory(category);
	}
	public boolean deleteBookById(Long id) {
		if(booksRepository.existsById(id)) {
			booksRepository.deleteById(id);
			return true;
		}
		return false;
	}
	public Books increaseNoOfBooks(Long id) {
		Books books=booksRepository.getById(id);
		System.out.println(books.getAuthorName()+" sesrvice");
		int n=books.getBooksAvailable();
		books.setBooksAvailable(n+1);
		booksRepository.save(books);
		return books;
		
	}
	public Books decreaseNoOfBooks(Long id) {
		Books books=booksRepository.getById(id);
		if(books.getBooksAvailable()==0) {
			return books;
		}
		 books.setBooksAvailable(books.getBooksAvailable()-1);
		 booksRepository.save(books);
		return books ;
	}


	public List<Books> getBooks( ) {
		  
		return booksRepository.findAll();
	}


	public List<Books> searchBooks(String query) {
		 
		return booksRepository.findByBookTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(query, query);
	}
	public ResponseEntity<?> issueBooksService(String usernname,Long bookId)
	{	
		try {
		Optional<LibraryUser> userOptional=userRepository.findByUsername(usernname.toUpperCase());
		Optional<Books> bookOptional=booksRepository.findById(bookId);
		if(userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}

		if(bookOptional.isEmpty()||bookOptional.get().getBooksAvailable()<=0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Books not found");
		}
		Optional<IssuedBooks> existingIssue=issuedBooksRepository.findByIdAndUserName(bookId,usernname);
		System.out.println("hello "+existingIssue.isPresent());
		if(existingIssue.isPresent()) {
			System.out.println("hello "+existingIssue.isPresent());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already this book issued");
		}
		Books book=bookOptional.get();
		book.setBooksAvailable(book.getBooksAvailable()-1);
		booksRepository.save(book);
		IssuedBooks issuedBooks=new IssuedBooks();
		issuedBooks.setBookId(bookId);
		issuedBooks.setUserName(usernname.toUpperCase());
		issuedBooks.setBookTitle(book.getBookTitle());
		issuedBooks.setTookDate(LocalDate.now());
		 
		issuedBooks.setDueDate(LocalDate.now().plusDays(14));
		issuedBooksRepository.save(issuedBooks);
		return ResponseEntity.ok("Book issued Succesfully");
		}
		catch (DataIntegrityViolationException e) {
			if(e.getMessage().contains("Duplicate entry")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Already this book issued");}
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
		catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured");
		}
	}
		


	public List<IssuedBooks> issueAllBooksService() {
		 
		return issuedBooksRepository.findAll();
		
	}


	public  List<UserBookCount> getUsers() {
		 
		return  issuedBooksRepository.findNumberOfIssuedBooksForUser();
	}
	public  UserBookCount getUserBookCount(String userName) {
		 
		return  issuedBooksRepository.findBookCountByUserName(userName);
	}
	 

	public IssuedBooks getReturnBooksById(Long id) {
		Optional<IssuedBooks> optionalBook=issuedBooksRepository.findById(id);
		if(optionalBook.isEmpty()) {
			throw new RuntimeException("No Book Issued with Given Book Id");
		}
		IssuedBooks book=optionalBook.get();
		LocalDate returnDate=LocalDate.now();
		book.setReturnedDate(returnDate);
		Long fine=calculateFine(book.getDueDate(), returnDate);
		book.setFineAmount(fine);
		issuedBooksRepository.save(book);
		return book;
	}
	public Long calculateFine(LocalDate dueDate,LocalDate returnedDate) {
		if(returnedDate.isAfter(dueDate)) {
			long daysLate=ChronoUnit.DAYS.between(dueDate, returnedDate);
			Long finePerDay=(long) 10;
			return daysLate*finePerDay;
		}
		return (long)0;
	}


	public ResponseEntity<?> returningBook(Long issueId, Long bookId) {
		 IssuedBooks issuedBooks=issuedBooksRepository.findById(issueId).
				 orElseThrow(()->new RuntimeException("Issued record not found"));
		 LocalDate returnedDate=LocalDate.now();
		 if(issuedBooks.getReturnedDate()!=null) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book already returned");
		 }
		 issuedBooks.setReturnedDate(returnedDate);
		 Long fine=calculateFine(issuedBooks.getDueDate(), returnedDate);
		 issuedBooks.setFineAmount(fine);
		 issuedBooksRepository.save(issuedBooks);
		 System.out.println(issuedBooks.getReturnedDate());
		 
		return ResponseEntity.ok(issuedBooksRepository.findAll());
	}


	public void deleteReturnedIssuedBood(Long issueId,Long bookId) {
		// TODO Auto-generated method stub
		issuedBooksRepository.deleteById(issueId);
		 System.out.println("hello man issue");
		 Books books=booksRepository.findById(bookId)
				 .orElseThrow(()->new RuntimeException("Book not found"));
		 books.setBooksAvailable(books.getBooksAvailable()+1);
		booksRepository.save(books);
	}


	public Long getUserCount(){
		// TODO Auto-generated method stub
		return userRepository.countByRole(Role.ROLE_USER);
	}
	public Long booksIssuedCount() {
		return (long) issuedBooksRepository.findAll().size();
	}


	public List<LibraryUser> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findByRole(Role.ROLE_USER);
	}
	
}
