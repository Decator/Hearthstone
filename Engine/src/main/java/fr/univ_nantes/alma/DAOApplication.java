package fr.univ_nantes.alma;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.univ_nantes.alma.engine.Hero;
import fr.univ_nantes.alma.engine.HeroRepository;
import fr.univ_nantes.alma.engine.Minion;
import fr.univ_nantes.alma.engine.MinionRepository;
import fr.univ_nantes.alma.engine.Spell;
import fr.univ_nantes.alma.engine.SpellRepository;

@SpringBootApplication
public class DAOApplication {

    public static void main(String[] args) {
        SpringApplication.run(DAOApplication.class, args);
    }
    
    @Bean
	public CommandLineRunner retrieveMinions(MinionRepository repository) {
		return (args) -> {
			repository.save(new Minion(1, "common", "chevalier", 2, 1, "target", "minion", 2, false, false, false, 0));
			repository.save(new Minion(2, "mage", "sanglier", 3, 2, "target2", "minion2", 3, false, true, false, 1));
			repository.save(new Minion(3, "paladin", "truc", 2, 2, "muche", "minion3", 2, true, false, false, 0));
			
			for (Minion minion : repository.findByType("mage")) {
				System.out.println(minion.toString());
			}
		};
	}
    
    @Bean
	public CommandLineRunner retrieveSpells(SpellRepository repository) {
		return (args) -> {
			repository.save(new Spell(1, "paladin", "feu", 2, 1, "target", "spell", 0, 0, 0, 0, 0, false));
			repository.save(new Spell(2, "mage", "truc", 2, 2, "target", "spell", 0, 0, 0, 0, 0, false));
			repository.save(new Spell(3, "mage", "chose", 1, 2, "target", "spell", 0, 0, 0, 0, 0, false));
			
			for (Spell spell : repository.findByType("mage")) {
				System.out.println(spell.toString());
			}
		};
	}
    
    @Bean
	public CommandLineRunner retrieveHeros(HeroRepository repository) {
		return (args) -> {
			repository.save(new Hero(1, "mage", "Pierre", 2, 1, 0, 0, "target", "hero"));
			
			for (Hero hero : repository.findAll()) {
				System.out.println(hero.toString());
			}
		};
	}
}
