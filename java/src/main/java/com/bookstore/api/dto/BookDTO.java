package com.bookstore.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String category;
    private BigDecimal price;
    private String description;
    private String coverImageUrl;
    private LocalDate publishedDate;
    private String isbn;
}
