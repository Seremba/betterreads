package com.seremba.betterreadsdataloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.seremba.betterreadsdataloader.author.Author;
import com.seremba.betterreadsdataloader.author.AuthorRepository;
import com.seremba.betterreadsdataloader.connection.DataStaxAstraProperties;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterreadsDataLoaderApplication {
	
	@Autowired
	AuthorRepository authorRepository;
	
	@Value("${datadump.location.author}")
	private String authorDatadumpLocation;
	
	@Value("${datadump.location.works}")
	private String worksDatadumpLocation;
	
	public static void main(String[] args) {
		SpringApplication.run(BetterreadsDataLoaderApplication.class, args);
	}
	
	private void initAuthor() {
		Path path = Paths.get(authorDatadumpLocation);
		
		try (Stream<String> lines = Files.lines(path)) {
			
			lines.limit(10).forEach(line -> {
				String jsonString = line.substring(line.indexOf("{"));
				try {
					JSONObject jsonObject = new JSONObject(jsonString);
					
					Author author = new Author();
					
					author.setName(jsonObject.optString("name"));
					author.setPersonalName(jsonObject.optString("personal_name"));
					author.setId(jsonObject.optString("key").replace("/authors/", ""));
					
					authorRepository.save(author);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			});
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void initWorks() {
		Path path = Paths.get(worksDatadumpLocation);
		
		try (Stream<String> lines = Files.lines(path)) {
			lines.limit(10).forEach(line -> {
				String jsonString = line.substring(line.indexOf("{"));
				try {
					new JSONObject(jsonString);
					
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
			
				
	}
	
	@PostConstruct
	public void start() {
		initAuthor();
	}
	
	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

}
