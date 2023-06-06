package io.javabrains.betterreads.book;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookController {
	
	@Autowired
	BookRepository bookRepository;
	
	@GetMapping("/book/{bookId}")
	public String getBook(@PathVariable String bookId, Model model) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		
		if (optionalBook.isPresent()) {
			Book book = optionalBook.get();
			model.addAttribute("book", book);
			return "book";
			
		}
		
		return "book-not-found";
	}
}
