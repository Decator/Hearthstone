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
		heroRepository.save(new Hero(3, "mage", "name", 1, 0, 0, 0, "target", "description"));
		minionRepository.save(new Minion(1, "common", "name", 1, 1, "description", 1, false, false, false, 0));
		spellRepository.save(new Spell(2, "mage", "name", 0, 0, null, 0, 0, 0, 0, 0, false, null));
		System.out.println("engine has been created");
		Application.engine = new Engine(minionRepository, spellRepository, heroRepository);
		return "engine has been created";
	}
}
