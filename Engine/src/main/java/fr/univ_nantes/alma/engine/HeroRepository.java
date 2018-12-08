package fr.univ_nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends CrudRepository<Hero, Integer> {
	ArrayList<Hero> findAll();
	Hero findById(int id);
	ArrayList<Hero> findByType(String type);
}
