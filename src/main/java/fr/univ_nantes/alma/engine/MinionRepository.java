package fr.univ_nantes.alma.engine;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

public interface MinionRepository extends CrudRepository<Minion, Integer> {
	ArrayList<Minion> findAll();

}
