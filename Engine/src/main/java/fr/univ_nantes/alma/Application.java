package fr.univ_nantes.alma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.univ_nantes.alma.engine.Engine;
import fr.univ_nantes.alma.engine.EngineBridge;
import fr.univ_nantes.alma.engine.Hero;
import fr.univ_nantes.alma.engine.HeroRepository;
import fr.univ_nantes.alma.engine.Minion;
import fr.univ_nantes.alma.engine.MinionRepository;
import fr.univ_nantes.alma.engine.Spell;
import fr.univ_nantes.alma.engine.SpellRepository;

@SpringBootApplication
public class Application {
	public static EngineBridge engine;
	
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
	@Bean
	public String engine(MinionRepository minionRepository, SpellRepository spellRepository, HeroRepository heroRepository) {
		heroRepository.save(new Hero(1, "mage", "Jaina", 1, 0, 0, 0, "Boule de feu : Inflige 1 point de dégâts."));
		heroRepository.save(new Hero(2, "paladin", "Uther", 0, 1, 12, 0, "Renfort : Invoque une recrue de la Main d'argent 1/1."));
		heroRepository.save(new Hero(3, "warrior", "Garrosh", 0, 0, 0, 2, "Gain d'armure ! : Confère 2 points d'armure."));
		
		minionRepository.save(new Minion(4, "common", "Sanglier brocheroc", 1, 1, null, 1, false, false, false, 0));
		minionRepository.save(new Minion(5, "common", "Soldat du compté-de-l'or", 1, 1, "Provocation", 2, true, false, false, 0));
		minionRepository.save(new Minion(6, "common", "Chevaucheur de loup", 3, 3, "Charge", 1, false, true, false, 0));
		minionRepository.save(new Minion(7, "common", "Chef de raid", 3, 2, "Vos autres serviteurs ont +1 ATQ.", 2, false, false, false, 1));
		minionRepository.save(new Minion(8, "common", "Yéti noroit", 4, 4, null, 5, false, false, false, 0));
		minionRepository.save(new Minion(9, "paladin", "Champion frisselame", 4, 3, "Charge, Vol de vie", 2, false, true, true, 0));
		minionRepository.save(new Minion(10, "warrior", "Avocat commis d'office", 2, 0, "Provocation", 7, true, true, true, 0));
		minionRepository.save(new Minion(11, "invocation", "Image miroir", 0, 0, "Provocation", 2, true, false, false, 0));
		minionRepository.save(new Minion(12, "invocation", "Recrue de la Main d'argent", 1, 1, null, 1, false, false, false, 0));
		minionRepository.save(new Minion(13, "invocation", "Mouton", 1, 1, null, 1, false, false, false, 0));
		
		spellRepository.save(new Spell(14, "mage", "Image miroir", 1, 0, "Invoque deux serviteurs 0/2 avec provocation.", 2, 11, 0, 0, 0, false, null));
		spellRepository.save(new Spell(15, "mage", "Explosion des arcanes", 2, 1, "Inflige 1 point de dégâts à tous les serviteurs adverses.", 0, 0, 0, 0, 0, false, "minion_all_enemy"));
		spellRepository.save(new Spell(16, "mage", "Métamorphose", 4, 0, "Transforme un serviteur en mouton 1/1.", 1, 13, 0, 0, 0, true, "minion_1_enemy"));
		spellRepository.save(new Spell(17, "paladin", "Bénédiction de puissance", 1, 0, "Confère +3 ATQ à un serviteur.", 0, 0, 3, 0, 0, false, "minion_1_ally"));
		spellRepository.save(new Spell(18, "paladin", "Consécration", 4, 2, "Inflige 2 points de dégâts à tous les adversaires.", 0, 0, 3, 0, 0, false, "all_all_enemy"));
		spellRepository.save(new Spell(19, "warrior", "Tourbillon", 1, 1, "minion_all_all", 0, 0, 0, 0, 0, false, "Inflige 1 point de dégâts à TOUS les serviteurs."));
		spellRepository.save(new Spell(20, "warrior", "Maîtrise du blocage", 3, 0, "minion_all_all", 0, 0, 0, 5, 1, false, "Vous gagnez 5 points d'armure et vous piochez une carte."));

		Application.engine = new Engine(minionRepository, spellRepository, heroRepository);
		return "engine has been created";
	}
}
