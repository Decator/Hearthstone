package fr.univ_nantes.alma;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.univ_nantes.alma.engine.Minion;
import fr.univ_nantes.alma.engine.Card;
import fr.univ_nantes.alma.engine.CardRepository;

@SpringBootApplication
public class DAOApplication {

    public static void main(String[] args) {
        SpringApplication.run(DAOApplication.class, args);
    }
    
    @Bean
	public CommandLineRunner demo(CardRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Minion(1, "common", "chevalier", 2, 1, "target", "description", 2, false, false, false, 0));

			for (Card card : repository.findAll()) {
				System.out.println(card.toString());
				System.out.println(card.getId());
				System.out.println(card.getDescription());
			}
			
			

			/*// fetch an individual customer by ID
			repository.findById(1L)
				.ifPresent(customer -> {
					log.info("Customer found with findById(1L):");
					log.info("--------------------------------");
					log.info(customer.toString());
					log.info("");
				});

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repository.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
			// for (Customer bauer : repository.findByLastName("Bauer")) {
			// 	log.info(bauer.toString());
			// }
			log.info("");*/
		};
	}
}
