package com.aryan.library.repository;
import com.aryan.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
