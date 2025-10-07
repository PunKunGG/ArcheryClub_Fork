package com.example.demo.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements UserDetails{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "email" , length = 255,  unique = true, nullable = false)
	private String email;
	@Column(name = "phone", length = 10, unique = true, nullable = false)
    private String phone;
	@Column(name = "username",length = 255 )
	private String username;
	@Column(name = "password",length = 255, nullable = false )
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role ;
	@Column(name = "studentid",length = 11 )
	private String studentid;
	public String getPhone() {
		return phone;
	}
	//@Column(name = "line_notify_token")
    //private String lineNotifyToken;

	
	/*public String getLineNotifyToken() {
		return lineNotifyToken;
	}

	public void setLineNotifyToken(String lineNotifyToken) {
		this.lineNotifyToken = lineNotifyToken;
	}*/
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Loan> equipLoans;


	public List<Loan> getEquipLoans() {
		return equipLoans;
	}

	public void setEquipLoans(List<Loan> equipLoans) {
		this.equipLoans = equipLoans;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // เมธอดนี้จะแปลงค่า role ของคุณให้ Spring Security รู้จัก
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String user_name) {
		this.username = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

}
