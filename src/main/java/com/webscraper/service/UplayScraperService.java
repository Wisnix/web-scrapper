package com.webscraper.service;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import com.webscraper.entity.Game;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UplayScraperService implements GameScraperService{

	private static final String URL = "https://store.ubi.com/uk/deals";

	private static final String SOURCE = "Uplay";

	private final ChromeDriver driver;

	@Override
	public HashSet<Game> scrape() {
		driver.get(URL);
		List<WebElement> gamesElements = driver.findElementsByXPath("//*[@id=\"search-result-items\"]/li");
		HashSet<Game> games = new HashSet<>();
		for (WebElement game : gamesElements) {
			games.add(new Game(
					game.findElement(By.cssSelector("div.card-title h2.prod-title")).getText() + " "
							+ game.findElement(By.cssSelector("div.card-subtitle h3")).getText(),
					game.findElement(By.cssSelector("span.price-standard span.price-item")).getText(),
					game.findElement(By.cssSelector("span.price-sales.standard-price")).getText(),
					game.findElement(By.cssSelector("div.deal-percentage.card-label.card-deal")).getText(), SOURCE));
		}
		return games;
	}

	@Override
	public HashSet<Game> call() throws Exception {
		return scrape();
	}

}
