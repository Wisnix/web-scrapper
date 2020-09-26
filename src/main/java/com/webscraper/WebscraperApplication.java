package com.webscraper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.webscraper.menu.Menu;
import com.webscraper.repository.GameRepository;
import com.webscraper.service.GameScraperService;

@SpringBootApplication
public class WebscraperApplication implements CommandLineRunner {

	@Autowired
	private Menu menu;

	public static void main(String[] args) {
		SpringApplication.run(WebscraperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		menu.openMenu();
	}

}
