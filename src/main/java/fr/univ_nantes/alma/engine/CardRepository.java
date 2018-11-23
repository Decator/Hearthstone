package fr.univ_nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Minion, Integer> {
	ArrayList<Minion> retrieveMinions(String type);
	ArrayList<Spell> retrieveSpells(String type);
	ArrayList<Minion> retrieveInvocations();
	ArrayList<Hero> retrieveHeroes();
}
