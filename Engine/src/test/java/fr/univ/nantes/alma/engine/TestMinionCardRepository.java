package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;

import fr.univ.nantes.alma.engine.MinionCard;
import fr.univ.nantes.alma.engine.MinionCardRepository;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class TestMinionCardRepository {

  @Autowired
  private TestEntityManager minionCardManager;

  @Autowired
  private MinionCardRepository minionCardRepository;

  @Test
  public void testFindById() {
    MinionCard minion1 = new MinionCard(6, "common", "Chevaucheur de loup", 3, 2, 
        "Charge", 1, false, true, false, 0);
    MinionCard minion2 = new MinionCard(4, "common", "Sanglier brocheroc", 1, 1, 
        null, 1, false, false, false, 0);

    minionCardManager.persist(minion1);
    minionCardManager.persist(minion2);

    MinionCard found = minionCardRepository.findById(minion2.getId());

    assertThat(found)
    .isNotNull()
      .isEqualToComparingFieldByFieldRecursively(minion2);
  }

  @Test
  public void testFindByType() {
    MinionCard minion1 = new MinionCard(4, "common", "Sanglier brocheroc", 1, 1, 
        null, 1, false, false, false, 0);
    MinionCard minion2 = new MinionCard(5, "common", "Soldat du compté-de-l'or", 1, 1, 
        "Provocation", 2, true, false, false, 0);
    MinionCard minion3 = new MinionCard(9, "paladin", "Champion frisselame", 4, 3, 
        "Charge, Vol de vie", 2, false, true, true, 0); 

    minionCardManager.persist(minion1);
    minionCardManager.persist(minion2);
    minionCardManager.persist(minion3);

    ArrayList<MinionCard> found = minionCardRepository.findByType(minion1.getType());

    assertThat(found)
    .isNotEmpty()
    .hasSize(2)
    .contains(minion1, minion2)
      .doesNotContain(minion3);
  }
}
