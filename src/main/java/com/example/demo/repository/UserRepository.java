package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Role;
import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	
	//User findByUsername(String username);
	//User findByEmail(String email);
	//Optional<User> findByEmail(String email);//ใช้ Optional<User> เพื่อป้องกัน NullPointerException
	Optional<User> findByEmail(String email);
	   boolean existsByUsername(String username);
	    boolean existsByEmail(String email);
	    List<User> findByRole(Role role);
}
