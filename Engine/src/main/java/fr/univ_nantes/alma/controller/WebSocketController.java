package fr.univ_nantes.alma.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
	
	private UUID uuid1 = null;
	private UUID uuid2 = null;
	
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
        	this.template.convertAndSend("/game/" + game.getCurrentPlayer().getUUID(), game);
        	this.template.convertAndSend("/game/" + game.getOtherPlayer().getUUID(), game);
    	}
    }
    
    @MessageMapping("/endTurn")
    public void endTurn(String uuidGame) {
    	this.template.convertAndSend("/game/" + uuidGame, Application.engine.endTurn(UUID.fromString(uuidGame)));
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
    
    @MessageMapping("/heroPower")
    public void heroPower(String data) {
    	String[] dataSplit = data.split("_");
    	try {
			this.template.convertAndSend("/game/" + dataSplit[0], Application.engine.heroPower(UUID.fromString(dataSplit[0]), UUID.fromString(dataSplit[1]), Integer.parseInt(dataSplit[2])));
		} catch (EngineException e) {
			this.template.convertAndSend("/game/" + dataSplit[0], e.getMessage());
		}
    }
    
    @MessageMapping("/attack")
    public void attack(String data) {
    	String[] dataSplit = data.split("_");
    	try {
    		this.template.convertAndSend("/game/" + dataSplit[0], Application.engine.attack(UUID.fromString(dataSplit[0]), Integer.parseInt(dataSplit[1]), Integer.parseInt(dataSplit[2])));
    	} catch (EngineException e) {
			this.template.convertAndSend("/game/" + dataSplit[0], e.getMessage());
		}
    }
    
    @MessageMapping("/connectionTest") 
    public void connectionTest(String data) {
		System.out.println(data);
    	if(this.uuid1 == null && this.uuid2 != UUID.fromString(data)) {
    		this.uuid1 = UUID.fromString(data);
    	} else if(this.uuid2 == null && this.uuid1 != UUID.fromString(data)) {
    		this.uuid2 = UUID.fromString(data);
    	}
    	//VOIR WebSocketSession
    	/*if(this.uuid1 == UUID.fromString(data)) {
    		try {
				TimeUnit.SECONDS.sleep(6);
				System.out.println("test");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	} else if(this.uuid2 == UUID.fromString(data)) {
    		
    	}*/
    	
    }
}
