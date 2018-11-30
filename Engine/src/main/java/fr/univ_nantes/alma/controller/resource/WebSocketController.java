package fr.univ_nantes.alma.controller.resource;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import fr.univ_nantes.alma.Application;
import fr.univ_nantes.alma.engine.Game;

@Controller
public class WebSocketController {
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@MessageMapping("/getHeros")
    public void getHeros(String message) {
    	this.template.convertAndSend("/greeting", Application.engine.getHeros());
    }

    @MessageMapping("/createPlayer")
    public void createPlayer() {
    	this.template.convertAndSend("/greeting", Application.engine.createPlayer(1, "Bob"));
    }
    
    @MessageMapping("/createGame")
    public void createGame(UUID uuidPlayer) {
    	Game game = Application.engine.createGame(uuidPlayer);
    	if(game == null) {
        	this.template.convertAndSend("/game", "Wait for second Player");
    	} else {
        	this.template.convertAndSend("/game", Application.engine.createGame(uuidPlayer));
    	}
    }
    
    @MessageMapping("/endTurn")
    public void endTurn(String message) {
    	this.template.convertAndSend("/greeting", Application.engine.createPlayer(1, "Bob"));
    }
    
    @MessageMapping("/playCard")
    public void playCard(String message) {
    	this.template.convertAndSend("/greeting", Application.engine.createPlayer(1, "Bob"));
    }
    
    @MessageMapping("/heroPower")
    public void heroPower(String message) {
    	this.template.convertAndSend("/greeting", Application.engine.createPlayer(1, "Bob"));
    }
    
    @MessageMapping("/attack")
    public void attack(String message) {
    	this.template.convertAndSend("/greeting", Application.engine.createPlayer(1, "Bob"));
    }
}
