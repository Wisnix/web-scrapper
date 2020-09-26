package com.webscraper.service;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.webscraper.entity.Game;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EpicScraperService implements GameScraperService {

	private static final String SOURCE = "Epic";

	private static final String URL = "https://www.epicgames.com/store/en-US/";

	private final ChromeDriver driver;

	@Override
	public HashSet<Game> scrape() {
		driver.get(URL);
		List<WebElement> freeGames = driver.findElementsByXPath("/html/body/div[1]/div/div[4]/main/div/div/div/div/div[2]/div/section/div/div");
		HashSet<Game>games=new HashSet<>();
		for(WebElement game:freeGames) {
			String[]lines=game.getText().split("\\n");
			if(lines[0].contains("FREE")) {
				games.add(new Game(lines[1],SOURCE));
			}
		}
		List<WebElement>saleGames=driver.findElementsByXPath("//*[@id=\"dieselReactWrapper\"]/div/div[4]/main/div/div/div/div/span[5]/div/div[2]");
		
		for(WebElement game:saleGames) {
			String[]lines=game.getText().split("\\n");
			String[]prices=lines[4].split("£");
			games.add(new Game(lines[0],prices[1],prices[2],lines[3],SOURCE));
		}		
		return games;
	}

	@Override
	public HashSet<Game> call() throws Exception {
		return scrape();
	}

}
