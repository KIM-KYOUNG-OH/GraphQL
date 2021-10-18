package com.graphql.demo.resolver;

import com.graphql.demo.model.Author;
import com.graphql.demo.model.Book;
import com.graphql.demo.repository.AuthorRepository;
import com.graphql.demo.repository.BookRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;

public class Query implements GraphQLQueryResolver {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Query(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Iterable<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Iterable<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public long countAuthors() {
        return authorRepository.count();
    }

}
