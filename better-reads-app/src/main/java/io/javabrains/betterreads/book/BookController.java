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
	
	private final String COVER_IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";
	
	@GetMapping("/book/{bookId}")
	public String getBook(@PathVariable String bookId, Model model) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		
		if (optionalBook.isPresent()) {
			Book book = optionalBook.get();
			String coverImageUrl = "/images/image-not-found.jpg";
			if(book.getCoverIds() != null & book.getCoverIds().size() > 0) {
				 coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-L.jpg";
				}
			model.addAttribute("coverImage", coverImageUrl);
			model.addAttribute("book", book);
			return "book";
			
		}
		
		return "book-not-found";
	}
}
