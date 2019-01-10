package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;

import fr.univ.nantes.alma.engine.SpellCard;
import fr.univ.nantes.alma.engine.SpellCardRepository;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class TestSpellCardRepository {
  @Autowired
  private TestEntityManager spellCardManager;

  @Autowired
  private SpellCardRepository spellCardRepository;
  
  @Test
  public void testFindById() {
    SpellCard spell1 = new SpellCard(14, "mage", "Image miroir", 1, 0, 
        "Invoque deux serviteurs 0/2 avec provocation.", 2, 11, 0, 0, 0, false, null);
    SpellCard spell2 = new SpellCard(15, "mage", "Explosion des arcanes", 2, 1, 
        "Inflige 1 point de dégâts à tous les serviteurs adverses.", 0, 0, 0, 0, 0, false, 
        "minion_all_enemy");

    spellCardManager.persist(spell1);
    spellCardManager.persist(spell2);

    SpellCard found = spellCardRepository.findById(spell1.getId());

    assertThat(found)
    .isNotNull()
      .isEqualToComparingFieldByFieldRecursively(spell1);
  }

  @Test
  public void testFindByType() {

    SpellCard spell1 = new SpellCard(14, "mage", "Image miroir", 1, 0, 
        "Invoque deux serviteurs 0/2 avec provocation.", 2, 11, 0, 0, 0, false, null);
    SpellCard spell2 = new SpellCard(15, "mage", "Explosion des arcanes", 2, 1, 
        "Inflige 1 point de dégâts à tous les serviteurs adverses.", 0, 0, 0, 0, 0, false, 
        "minion_all_enemy");
    SpellCard spell3 = new SpellCard(19, "warrior", "Tourbillon", 1, 1, 
        "Inflige 1 point de dégâts à TOUS les serviteurs.", 0, 0, 0, 0, 0, false, "minion_all_all");

    spellCardManager.persist(spell1);
    spellCardManager.persist(spell2);
    spellCardManager.persist(spell3);

    ArrayList<SpellCard> found = spellCardRepository.findByType(spell1.getType());

    assertThat(found)
    .isNotEmpty()
    .hasSize(2)
    .contains(spell1, spell2)
      .doesNotContain(spell3);
  }
}
