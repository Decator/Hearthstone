package fr.univ.nantes.alma.engine;


import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import fr.univ.nantes.alma.engine.MinionCard;
import fr.univ.nantes.alma.engine.MinionCardRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestMinionCardRepository {
	
	@Autowired
	private TestEntityManager minionCardManager;
	
	@Autowired
	private MinionCardRepository minionCardRepository;
	
	
	@Test
	public void testFindById() {
		MinionCard minion1 = new MinionCard(6, "common", "Chevaucheur de loup", 3, 2, "Charge", 1, false, true, false, 0);
		MinionCard minion2 = new MinionCard(4, "common", "Sanglier brocheroc", 1, 1, null, 1, false, false, false, 0);
		
		minionCardManager.persist(minion1);
		minionCardManager.persist(minion2);
		
		MinionCard found = minionCardRepository.findById(minion2.getId());
		
		assertThat(found.getId()).isNotNull();
		assertThat(found.getId()).isEqualTo(minion2.getId());
		assertThat(found.getId()).isNotEqualTo(minion1.getId());
		assertThat(found).isEqualTo(minion2);
		assertThat(found).isNotEqualTo(minion1);
	}
	
	@Test
	public void testFindByType() {
		MinionCard minion1 = new MinionCard(4, "common", "Sanglier brocheroc", 1, 1, null, 1, false, false, false, 0);
		MinionCard minion2 = new MinionCard(5, "common", "Soldat du compté-de-l'or", 1, 1, "Provocation", 2, true, false, false, 0);
		MinionCard minion3 = new MinionCard(9, "paladin", "Champion frisselame", 4, 3, "Charge, Vol de vie", 2, false, true, true, 0); 
		
		minionCardManager.persist(minion1);
		minionCardManager.persist(minion2);
		minionCardManager.persist(minion3);
		
		ArrayList<MinionCard> found = minionCardRepository.findByType(minion1.getType());
		
		assertThat(found).isNotEmpty();
		assertThat(found).hasSize(2);
		assertThat(found).contains(minion1, minion2);
		assertThat(found).doesNotContain(minion3);
		
	}


}
