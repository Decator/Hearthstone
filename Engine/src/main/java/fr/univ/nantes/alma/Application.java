package fr.univ.nantes.alma;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.univ.nantes.alma.engine.Engine;
import fr.univ.nantes.alma.engine.EngineBridge;
import fr.univ.nantes.alma.engine.HeroCardRepository;
import fr.univ.nantes.alma.engine.MinionCardRepository;
import fr.univ.nantes.alma.engine.SpellCardRepository;

@SpringBootApplication
public class Application {
  public static EngineBridge engineBridge;
  
  @Autowired
  DataSource dataSource;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public String engine(MinionCardRepository minionRepository, 
      SpellCardRepository spellRepository, HeroCardRepository heroRepository) {
    Application.engineBridge = new Engine(minionRepository, spellRepository, heroRepository);
    return "engine has been created";
  }
}
