package com.libraryproject.main.repositories;

 

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.libraryproject.main.entity.LibraryUser;
import com.libraryproject.main.entity.Role;



public interface UserRepository extends JpaRepository<LibraryUser,String> {
		   Optional<LibraryUser> findByUsername(String username);
		   List<LibraryUser> findByRole(Role role);
		   @Query("SELECT COUNT(u) FROM LibraryUser u WHERE u.role= :role")
		   Long countByRole(@Param("role")Role role);
}
