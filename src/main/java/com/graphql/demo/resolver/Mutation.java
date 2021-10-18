package com.graphql.demo.resolver;

import com.graphql.demo.exceptions.BookNotFoundException;
import com.graphql.demo.model.Author;
import com.graphql.demo.model.Book;
import com.graphql.demo.repository.AuthorRepository;
import com.graphql.demo.repository.BookRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;

import java.util.Optional;

public class Mutation implements GraphQLMutationResolver {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Mutation(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Author addNewAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        authorRepository.save(author);

        return author;
    }

    public Book addNewBook(String title, Integer pageCount, Long authorId) {
        Book book = new Book();
        book.setAuthor(new Author(authorId));
        book.setTitle(title);
        book.setPageCount(pageCount != null ? pageCount : 0);

        bookRepository.save(book);

        return book;
    }

    public boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return true;
    }

    public Book updateBookPageCount(Integer pageCount, Long id) {
        Book book = Optional.of(bookRepository.findById(id)).get().orElseThrow(BookNotFoundException::new);

        book.setPageCount(pageCount);

        bookRepository.save(book);

        return book;
    }

}
