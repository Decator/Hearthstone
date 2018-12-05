package fr.univ_nantes.alma.controller.resource;

import java.util.UUID;

import fr.univ_nantes.alma.engine.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import fr.univ_nantes.alma.Application;
import fr.univ_nantes.alma.engine.EngineException;
import fr.univ_nantes.alma.engine.Game;

@Controller
public class WebSocketController {
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@MessageMapping("/getHeros")
    public void getHeros() {
    	this.template.convertAndSend("/heros", Application.engine.getHeros());
    }

    @MessageMapping("/createPlayer")
    public void createPlayer(String data) {
    	String[] dataSplit = data.split("_");
    	this.template.convertAndSend("/player/" + dataSplit[0], Application.engine.createPlayer(Integer.parseInt(dataSplit[1]), dataSplit[0]));
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
    public void playCard(String data) {
    	String[] dataSplit = data.split("_");
    	try {
    		this.template.convertAndSend("/game/" + dataSplit[0], Application.engine.playCard(UUID.fromString(dataSplit[0]), Integer.parseInt(dataSplit[1]), UUID.fromString(dataSplit[2]), Integer.parseInt(dataSplit[3])));
    	} catch(EngineException e) {
    		this.template.convertAndSend("/game/" + dataSplit[0], e.getMessage());
    	}
    }
    
//    @MessageMapping("/heroPower")
//    public void heroPower(String uuidGame, String uuidPlayer, int idTarget) {
//    	this.template.convertAndSend("/game/" + uuidGame, Application.engine.heroPower(UUID.fromString(uuidGame), UUID.fromString(uuidPlayer), idTarget));
//    }
//    
//    @MessageMapping("/attack")
//    public void attack(String uuidGame, int idAttack, int idTarget) {
//    	this.template.convertAndSend("/game/" + uuidGame, Application.engine.attack(UUID.fromString(uuidGame), idAttack, idTarget));
//    }
}
