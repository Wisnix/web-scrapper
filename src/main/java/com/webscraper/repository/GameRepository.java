package com.webscraper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webscraper.entity.Game;

@Repository
public interface GameRepository extends CrudRepository<Game,Long>{

}
