package com.seremba.betterreadsdataloader.book;



import org.springframework.data.cassandra.repository.CassandraRepository;

import com.seremba.betterreadsdataloader.author.Author;

public interface BookRepository extends CassandraRepository<Author, String> {
	
}
