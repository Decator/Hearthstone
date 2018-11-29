package fr.univ_nantes.alma;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.univ_nantes.alma.engine.HeroRepository;
import fr.univ_nantes.alma.engine.MinionRepository;
import fr.univ_nantes.alma.engine.SpellRepository;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		System.out.println("Run SpringApplication");
        SpringApplication.run(Application.class, args);
    }
	
	@Bean
	public CommandLineRunner retrieveMinionRepository(MinionRepository repository) {
		return (args) -> {
			System.out.println("MinionRepository saved");
		};
	}
	
	@Bean
	public CommandLineRunner retrieveSpellRepository(SpellRepository repository) {
		return (args) -> {
			System.out.println("SpellRepository saved");
		};
	}
    
    @Bean
	public CommandLineRunner retrieveHeoRepository(HeroRepository repository) {
    	return (args) -> {
        	System.out.println("HeroRepository saved");
		};
	}
}
