package fr.univ_nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpellRepository extends CrudRepository<Spell, Integer> {
	ArrayList<Spell> findByType(String type);
	Spell findById(int id);
}
