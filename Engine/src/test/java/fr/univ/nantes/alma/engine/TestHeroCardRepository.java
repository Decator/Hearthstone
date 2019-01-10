package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;

import fr.univ.nantes.alma.engine.HeroCard;
import fr.univ.nantes.alma.engine.HeroCardRepository;

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
public class TestHeroCardRepository {

  @Autowired
  private TestEntityManager heroCardManager;

  @Autowired
  private HeroCardRepository heroCardRepository;

  @Test
  public void testFindAll() {
    HeroCard hero1 = new HeroCard(1, "mage", "Jaina", 1, 0, 0, 0, 
        "Boule de feu : Inflige 1 point de dégâts.", "all_1_all");
    HeroCard hero2 = new HeroCard(2, "paladin", "Uther", 0, 1, 12, 0, 
        "Renfort : Invoque une recrue de la Main d'argent 1/1.", null);
    HeroCard hero3 = new HeroCard(3, "warrior", "Garrosh", 0, 0, 0, 2, 
        "Gain d'armure ! : Confère 2 points d'armure.", null);

    heroCardManager.persist(hero1);
    heroCardManager.persist(hero2);
    heroCardManager.persist(hero3);

    ArrayList<HeroCard> found = heroCardRepository.findAll();

    assertThat(found)
    .isNotEmpty()
    .hasSize(3)
      .contains(hero1, hero2, hero3);
  }

  @Test
  public void testFindById() {
    HeroCard hero1 = new HeroCard(1, "mage", "Jaina", 1, 0, 0, 0, 
        "Boule de feu : Inflige 1 point de dégâts.", "all_1_all");
    HeroCard hero2 = new HeroCard(2, "paladin", "Uther", 0, 1, 12, 0, 
        "Renfort : Invoque une recrue de la Main d'argent 1/1.", null);

    heroCardManager.persist(hero1);
    heroCardManager.persist(hero2);

    HeroCard found = heroCardRepository.findById(hero2.getId());

    assertThat(found)
    .isNotNull()
      .isEqualToComparingFieldByFieldRecursively(hero2);
  }

  @Test
  public void testFindByType() {
    HeroCard hero1 = new HeroCard(1, "mage", "Jaina", 1, 0, 0, 0, 
        "Boule de feu : Inflige 1 point de dégâts.", "all_1_all");
    HeroCard hero2 = new HeroCard(2, "paladin", "Uther", 0, 1, 12, 0, 
        "Renfort : Invoque une recrue de la Main d'argent 1/1.", null);
    HeroCard hero3 = new HeroCard(3, "warrior", "Garrosh", 0, 0, 0, 2, 
        "Gain d'armure ! : Confère 2 points d'armure.", null);

    heroCardManager.persist(hero1);
    heroCardManager.persist(hero2);
    heroCardManager.persist(hero3);

    ArrayList<HeroCard> found = heroCardRepository.findByType(hero1.getType());
 
    assertThat(found)
    .isNotEmpty()
    .hasSize(1)
    .contains(hero1)
      .doesNotContain(hero2, hero3);
  }
}