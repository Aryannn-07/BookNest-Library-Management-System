package com.aryan.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "members")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required") @Column(nullable = false)
    private String fullName;
    @NotBlank(message = "Email is required") @Email(message = "Enter a valid email") @Column(nullable = false, unique = true)
    private String email;
    private String phone;
    public Member() {}
    public Member(String fullName, String email, String phone) { this.fullName = fullName; this.email = email; this.phone = phone; }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
