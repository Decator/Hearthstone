package fr.univ_nantes.alma.controller.resource;

import java.util.UUID;

import fr.univ_nantes.alma.engine.Player;
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
    	this.template.convertAndSend("/heros", Application.engine.getHeros());
    }

    @MessageMapping("/createPlayer")
    public void createPlayer(String username) {
    	this.template.convertAndSend("/player/" + username, Application.engine.createPlayer(3, username));
    }
    
    @MessageMapping("/createGame")
    public void createGame(String uuidPlayer) {
    	Game game = Application.engine.createGame(UUID.fromString(uuidPlayer));
    	if(game == null) {
        	this.template.convertAndSend("/game/" + uuidPlayer, "Waiting for another Player");
    	} else {
    		Player[] player = game.getPlayers();
        	this.template.convertAndSend("/game/" + player[0].getUUID(), game);
        	this.template.convertAndSend("/game/" + player[1].getUUID(), game);
    	}
    }
    
    @MessageMapping("/endTurn")
    public void endTurn(String uuidGame) {
    	Application.engine.endTurn(UUID.fromString(uuidGame));
    	this.template.convertAndSend("/game/" + uuidGame + "/endTurn", "ok");
    }
    
    @MessageMapping("/playCard")
    public void playCard(String uuidGame, int idCard, String uuidPlayer, int idTarget) {
    	this.template.convertAndSend("/game/" + uuidGame + "/playCard", Application.engine.playCard(UUID.fromString(uuidGame), idCard, UUID.fromString(uuidPlayer), idTarget));
    }
    
    @MessageMapping("/heroPower")
    public void heroPower(String uuidGame, String uuidPlayer, int idTarget) {
    	this.template.convertAndSend("/game/" + uuidGame + "/heroPower", Application.engine.heroPower(UUID.fromString(uuidGame), UUID.fromString(uuidPlayer), idTarget));
    }
    
    @MessageMapping("/attack")
    public void attack(String uuidGame, int idAttack, int idTarget) {
    	this.template.convertAndSend("/game/" + uuidGame + "/attack", Application.engine.attack(UUID.fromString(uuidGame), idAttack, idTarget));
    }
}
