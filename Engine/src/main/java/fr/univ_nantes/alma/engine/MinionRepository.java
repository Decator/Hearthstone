package fr.univ_nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinionRepository extends CrudRepository<Minion, Integer> {
	ArrayList<Minion> findByType(String type);
	Minion findById(int id);
}
