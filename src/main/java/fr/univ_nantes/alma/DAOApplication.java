package fr.univ_nantes.alma;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.univ_nantes.alma.engine.Minion;
import fr.univ_nantes.alma.engine.MinionRepository;

@SpringBootApplication
public class DAOApplication {

    public static void main(String[] args) {
        SpringApplication.run(DAOApplication.class, args);
    }
    
    @Bean
	public CommandLineRunner demo(MinionRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Minion(1, "common", "chevalier", 2, 1, "target", "description", 2, false, false, false, 0));

			for (Minion minion : repository.findAll()) {
				System.out.println(minion.toString());
				System.out.println(minion.getId());
				System.out.println(minion.getDescription());
			}
		};
	}
}
