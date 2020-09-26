package com.webscraper.config;

import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class WebscraperConfiguration {

	@PostConstruct
	void postConstruct() {
		System.setProperty("webdriver.chrome.driver", "/home/bartosz/chromedriver_linux64/chromedriver");
	}

	@Bean
	@Scope("prototype")
	public ChromeDriver driver() {
		final ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments(("--headless"));
		return new ChromeDriver(chromeOptions);
	}
	
	@Bean
	public Scanner in() {
		return new Scanner(System.in);
	}

}
