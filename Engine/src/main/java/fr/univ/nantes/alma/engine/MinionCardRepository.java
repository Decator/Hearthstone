package fr.univ.nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinionCardRepository extends CrudRepository<MinionCard, Integer> {
	ArrayList<MinionCard> findByType(String typeMinion);
	MinionCard findById(int idMinion);
}
