package com.bookstore.api.controller;

import com.bookstore.api.dto.BookDTO;
import com.bookstore.api.model.Book;
import com.bookstore.api.repository.BookRepository;
import com.bookstore.api.transformer.BookTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository bookRepository;
    private final BookTransformer bookTransformer;

    public BookController(BookRepository bookRepository, BookTransformer bookTransformer) {
        this.bookRepository = bookRepository;
        this.bookTransformer = bookTransformer;
    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> listBooks(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, sortBy));
        
        Page<Book> books = bookRepository.findByFilters(searchTerm, category, author, pageRequest);
        Page<BookDTO> bookDTOs = books.map(bookTransformer::toDTO);
        return ResponseEntity.ok(bookDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(bookTransformer::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        Book book = bookTransformer.toEntity(bookDTO);
        book.setId(null); // Ensure we're creating a new book
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(bookTransformer.toDTO(savedBook));
    }
}
