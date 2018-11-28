package fr.univ_nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

public interface HeroRepository extends CrudRepository<Hero, Integer> {
	ArrayList<Hero> findAll();
	Hero findById(int id);
}
