package com.aryan.library;

import com.aryan.library.entity.Book;
import com.aryan.library.entity.Member;
import com.aryan.library.repository.BookRepository;
import com.aryan.library.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner sampleData(BookRepository books, MemberRepository members) {
        return args -> {
            if (books.count() == 0) {
                books.save(new Book("9780132350884", "Clean Code", "Robert C. Martin", "Programming", 4));
                books.save(new Book("9781617294945", "Spring in Action", "Craig Walls", "Programming", 3));
                books.save(new Book("9780073523323", "Database System Concepts", "Abraham Silberschatz", "Database", 2));
            }
            if (members.count() == 0) {
                members.save(new Member("Rahul Sharma", "rahul@example.com", "9876543210"));
                members.save(new Member("Priya Singh", "priya@example.com", "9876501234"));
            }
        };
    }
}
