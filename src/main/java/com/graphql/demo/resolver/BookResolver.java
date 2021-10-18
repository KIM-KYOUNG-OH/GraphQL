package com.graphql.demo.resolver;

import com.graphql.demo.model.Author;
import com.graphql.demo.model.Book;
import com.graphql.demo.repository.AuthorRepository;
import graphql.kickstart.tools.GraphQLResolver;

import java.util.Optional;

public class BookResolver implements GraphQLResolver<Book> {
    private final AuthorRepository authorRepository;

    public BookResolver(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthor(Book book) {
        return Optional.of(authorRepository.findById(book.getAuthor().getId())).get().orElseThrow();
    }
}
