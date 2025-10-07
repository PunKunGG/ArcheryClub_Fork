package com.example.demo.service;

import java.util.List;

//import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.model.Loan;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.web.ResourceNotFoundException;



@Service
public class UserService {
	
	private final UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/*public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}*/
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	// --- 3. เพิ่มเมธอดใหม่สำหรับ API ---

    public User createUser(CreateUserRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // เข้ารหัสผ่าน
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getPhone());
        newUser.setStudentid(request.getStudentId());
        newUser.setRole(Role.MEMBER); // กำหนด Role พื้นฐาน

        return userRepository.save(newUser);
    }
    
	
	//make for equipLoan
	@Transactional(readOnly = true)
	public List<Loan> getUserLoans(Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    user.getEquipLoans().size(); //โหลดข้อมูลตอนยังอยู่ใน session
	    return user.getEquipLoans();
	}
	
	public void registerUser(String username,String studentid,String phone,String email,String password) {
		User user = new User();
		user.setUsername(username);
		user.setStudentid(studentid);
		user.setPhone(phone);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(Role.MEMBER);
		userRepository.save(user);
		
		 UsernamePasswordAuthenticationToken authToken =
			        new UsernamePasswordAuthenticationToken(
			            user.getUsername(),
			            password,
			            org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_"+user.getRole().name())
			        );
			    SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
	@Transactional
	public User updateRole(Long userId,Role newRole) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("ไม่พบผู้ใช้ ID: " + userId));
		user.setRole(newRole);
		return userRepository.save(user);
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
}
