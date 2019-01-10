package fr.univ.nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroCardRepository extends CrudRepository<HeroCard, Integer> {
	ArrayList<HeroCard> findAll();
	HeroCard findById(int idHero);
	ArrayList<HeroCard> findByType(String typeHero);
}
