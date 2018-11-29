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
import fr.univ_nantes.alma.engine.SpellRepository;

@SpringBootApplication
public class Application {
	public static EngineBridge engine;
	
	public static void main(String[] args) {
		System.out.println("Run SpringApplication");
        SpringApplication.run(Application.class, args);
        System.out.println(engine);
    }
	
	@Bean
	public String engine(MinionRepository minionRepository, SpellRepository spellRepository, HeroRepository heroRepository) {
		heroRepository.save(new Hero(1, "mage", "name", 1, 0, 0, 0, "target", "description"));
		minionRepository.save(new Minion(1, "common", "name", 1, 1, "description", 1, false, false, false, 0));
		Application.engine = new Engine(minionRepository, spellRepository, heroRepository);
		return "engine has been created";
	}
}
