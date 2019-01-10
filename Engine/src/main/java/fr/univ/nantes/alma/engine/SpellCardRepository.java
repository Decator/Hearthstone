package fr.univ.nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpellCardRepository extends CrudRepository<SpellCard, Integer> {
  ArrayList<SpellCard> findByType(String typeSpell);
  
  SpellCard findById(int idSpell);
}
