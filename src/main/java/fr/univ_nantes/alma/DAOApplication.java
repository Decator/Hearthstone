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

			for (Minion minion : repository.findAll()) {
				System.out.println(minion.toString());
				System.out.println(minion.getId());
				System.out.println(minion.getDescription());
			}
		};
	}
    
    @Bean
	public CommandLineRunner retrieveSpells(SpellRepository repository) {
		return (args) -> {
			
			repository.save(new Spell(1, "common", "feu", 2, 1, "target", "spell", 0, 0, 0, 0, 0, false));
			
			for (Spell spell : repository.findAll()) {
				System.out.println(spell.toString());
				System.out.println(spell.getId());
				System.out.println(spell.getDescription());
			}
		};
	}
    
    @Bean
	public CommandLineRunner retrieveHeros(HeroRepository repository) {
		return (args) -> {
			
			repository.save(new Hero(1, "mage", "Pierre", 2, 1, 0, 0, "target", "hero"));
			
			for (Hero hero : repository.findAll()) {
				System.out.println(hero.toString());
				System.out.println(hero.getId());
				System.out.println(hero.getDescription());
			}
		};
	}
}
