package com.libraryproject.main.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.libraryproject.main.entity.IssuedBooks;
import com.libraryproject.main.entity.UserBookCount;

public interface IssuedBooksRepository extends JpaRepository<IssuedBooks, Long> {
	@Query("SELECT b.userName as username,COUNT(b.userName) as bookCount FROM IssuedBooks b GROUP BY b.userName")
	List<UserBookCount> findNumberOfIssuedBooksForUser();
	
	@Query("SELECT  b.userName as username,COUNT(b.userName) as bookCount FROM IssuedBooks b WHERE b.userName=:userName GROUP BY b.userName ")
	UserBookCount findBookCountByUserName(@Param("userName") String userName);
	
	@Query("SELECT i FROM IssuedBooks i WHERE i.bookId=:bookId AND i.userName=:userName")
	Optional<IssuedBooks> findByIdAndUserName(@Param("bookId")Long bookId,@Param("userName") String userName);
	 
}
