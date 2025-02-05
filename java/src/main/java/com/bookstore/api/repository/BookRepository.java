package com.bookstore.api.repository;

import com.bookstore.api.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE " +
           "(:searchTerm IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:category IS NULL OR LOWER(b.category) = LOWER(:category)) AND " +
           "(:author IS NULL OR LOWER(b.author) = LOWER(:author))")
    Page<Book> findByFilters(
            @Param("searchTerm") String searchTerm,
            @Param("category") String category,
            @Param("author") String author,
            Pageable pageable
    );
}
