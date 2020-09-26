package com.webscraper.service;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.webscraper.entity.Game;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GogScraperService implements GameScraperService {
	
	private static final String SOURCE="GOG";

	private static final String URL = "https://www.gog.com/games?page=%s&sort=popularity&price=discounted";
	
	private final ChromeDriver driver;

	@Override
	public HashSet<Game> scrape() {
		driver.get(String.format(URL, 1));
		final WebElement pagesElement = driver.findElementByClassName("paginator-container");
		int pages = Integer.parseInt(pagesElement.findElement(By.className("page-indicator--last")).getText());
		WebElement games = driver.findElementByClassName("catalog__games-list");
		List<WebElement> gamesElementList = games.findElements(By.xpath(
				"//div[not(contains(@class,'ng-hide')) and @ng-show]/div/div/a[contains(@class,'product-tile__content')]"));
		HashSet<Game> gamesList = new HashSet<>();
		extractGames(gamesElementList, gamesList);
		for (int i = 2; i <= pages; i++) {
			driver.get(String.format(URL, i));
			games = driver.findElementByClassName("catalog__games-list");
			gamesElementList = games.findElements(By.xpath(
					"//div[not(contains(@class,'ng-hide')) and @ng-show]/div/div/a[contains(@class,'product-tile__content')]"));
			extractGames(gamesElementList, gamesList);
		}
		driver.quit();
		return gamesList;
	}

	private void extractGames(List<WebElement> gamesElementList, HashSet<Game> gamesList) {
		for (WebElement e : gamesElementList) {
			if (e.findElement(By.className("product-tile__title")).getText().isEmpty())
				continue;
			try {
				gamesList.add(new Game(e.findElement(By.className("product-tile__title")).getText(),
						e.findElement(By.cssSelector("span.product-tile__price._price")).getText(),
						e.findElement(By.cssSelector("span.product-tile__price-discounted._price")).getText(),
						e.findElement(By.cssSelector("span.product-tile__discount")).getText(),SOURCE)
						);
			} catch (Exception ex) {
			}
		}
	}

	@Override
	public HashSet<Game> call() throws Exception {
		return scrape();
	}

}
