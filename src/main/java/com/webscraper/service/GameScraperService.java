package com.webscraper.service;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import com.webscraper.entity.Game;

public interface GameScraperService extends Callable<HashSet<Game>> {
	
	HashSet<Game> scrape();
}
