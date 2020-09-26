package com.webscraper.menu;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.webscraper.entity.Game;
import com.webscraper.repository.GameRepository;
import com.webscraper.service.GameScraperService;

import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice.Exit;

@Component
@AllArgsConstructor
public class Menu {

	private Collection<GameScraperService> scrappers;

	private GameRepository gameRepository;

	private HashSet<Game> savedGames;

	private HashSet<Game> scrappedGames;

	private HashSet<Game> newGames;

	private Map<Object, List<Game>> gamesBySource;

	private Scanner in;

	public void openMenu() {
		start();
		navigate();
	}

	private void start() {
		System.out.println("Scrapping websites, please wait...");
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			Future<HashSet<Game>> savedGamesFuture = Executors.newSingleThreadExecutor()
					.submit(() -> new HashSet<Game>((List<Game>) gameRepository.findAll()));
			List<Future<HashSet<Game>>> scrappedGameFutures = executor.invokeAll(scrappers);
			scrappedGames = scrappedGameFutures.stream().<HashSet<Game>>reduce(new HashSet<Game>(),
					(HashSet<Game> set, Future<HashSet<Game>> future) -> {
						try {
							set.addAll(future.get());
						} catch (ExecutionException | InterruptedException e) {
							e.printStackTrace();
						}
						return set;
					}, (set1, set2) -> {
						set1.addAll(set2);
						return set1;
					});
			savedGames = savedGamesFuture.get();
			System.out.println("Finished scrapping websites");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		updateDatabase(scrappedGames, savedGames);
	}

	private void navigate() {
		int option;
		showMenuOptions();
		do {
			option = in.nextInt();
			switch (option) {
			case 1:
				newGames.forEach(System.out::println);
				break;
			case 2:
				scrappedGames.forEach(System.out::println);
				break;
			case 3: {
				int platform = in.nextInt();
				printByPlatform(platform);
				break;
			}
			case 4:
				scrappedGames.forEach(game -> {
					if (game.getNewPrice() == 0)
						System.out.println(game);
				});
				break;
			case 5:showMenuOptions();
				break;
			case 6:System.out.println("ByeBye");
				System.exit(0);
			}			
		}while(option!=6);
	}

	private void printByPlatform(int platform) {
		String source = platform == 1 ? "GOG" : platform == 2 ? "Epic" : platform == 3 ? "Uplay" : "";
		if (gamesBySource != null && !gamesBySource.isEmpty()) {
			gamesBySource.get(source).forEach(System.out::println);
		} else {
			gamesBySource = savedGames.stream().peek(game -> {
				if (game.getSource().equalsIgnoreCase(source))
					System.out.println(game);
			}).collect(Collectors.groupingBy(game -> game.getSource()));

		}
	}

	private void showMenuOptions() {
		System.out.println("********** MENU **********");
		System.out.println("Use numbers to navigate through options");
		System.out.println("1. Show new discounted games");
		System.out.println("2. Show all discounted games");
		System.out.println("3. Show discounted games by platform (1-GOG, 2-EpicGames, 3-Uplay)");
		System.out.println("4. Show free games only");
		System.out.println("5. Show menu again");
		System.out.println("6. Exit");
	}

	private void updateDatabase(HashSet<Game> scrappedGames, HashSet<Game> savedGames) {
		newGames = (HashSet<Game>) scrappedGames.clone();
		newGames.removeAll(savedGames);
		if (!newGames.isEmpty()) {
			System.out.println("These are new games:");
			newGames.forEach(game -> System.out.println(game.getTitle()));
			gameRepository.saveAll(newGames);
		} else {
			System.out.println("No new discounted/free games were found since last run.");
		}
		HashSet<Game> noLongerDiscounted = (HashSet<Game>) savedGames.clone();
		noLongerDiscounted.removeAll(scrappedGames);
		if (!noLongerDiscounted.isEmpty()) {
			System.out.println("These games are no longer discounted:");
			noLongerDiscounted.forEach(game -> System.out.println(game.getTitle()));
			gameRepository.deleteAll(noLongerDiscounted);
		}
	}

}
