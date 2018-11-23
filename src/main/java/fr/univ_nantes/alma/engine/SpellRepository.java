package fr.univ_nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

public interface SpellRepository extends CrudRepository<Spell, Integer> {
	ArrayList<Spell> findAll();
}
