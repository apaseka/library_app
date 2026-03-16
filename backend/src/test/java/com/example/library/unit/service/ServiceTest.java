package com.example.library.unit.service;

import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.util.AuthorHelper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    protected AuthorRepository authorRepository;
    @Mock
    protected AuthorHelper authorHelper;

    protected Author createAuthor(Long id, String name) {
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        return author;
    }

    protected Book createBook(Long id, String title, int year, Author author) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setYear(year);
        book.setAuthor(author);
        return book;
    }
}
