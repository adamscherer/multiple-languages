package com.bookstore.api.transformer;

import com.bookstore.api.dto.BookDTO;
import com.bookstore.api.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookTransformer {
    
    public BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }
        
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());
        dto.setPrice(book.getPrice());
        dto.setDescription(book.getDescription());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setIsbn(book.getIsbn());
        return dto;
    }
    
    public Book toEntity(BookDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setCategory(dto.getCategory());
        book.setPrice(dto.getPrice());
        book.setDescription(dto.getDescription());
        book.setCoverImageUrl(dto.getCoverImageUrl());
        book.setPublishedDate(dto.getPublishedDate());
        book.setIsbn(dto.getIsbn());
        return book;
    }
}
