package com.seremba.betterreadsdataloader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seremba.betterreadsdataloader.author.Author;
import com.seremba.betterreadsdataloader.author.AuthorRepository;

@Component
public class DataInitialization {
	
	@Autowired
	AuthorRepository authorRepository;
	
	@PostConstruct
	public void start() {
		Author author = new Author();
		author.setId("id");
		author.setName("Name");
		author.setPersonalName("personal_name");
		authorRepository.save(author);
		System.out.println("Application started");
	}
}
