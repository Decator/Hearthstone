package fr.univ.nantes.alma.controller;

import fr.univ.nantes.alma.Application;
import fr.univ.nantes.alma.engine.EngineException;
import fr.univ.nantes.alma.engine.GameMethods;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
 
  @Autowired
  private SimpMessagingTemplate template;

  private UUID uuid1 = null;
  private UUID uuid2 = null;

  @MessageMapping("/getHeros")
  public void getHeros() {
    this.template.convertAndSend("/heros", Application.engineBridge.getHeros());
  }

  @MessageMapping("/createPlayer")
  public void createPlayer(String data) {
    String[] dataSplit = data.split("_");
    this.template.convertAndSend("/player/" + dataSplit[0], 
        Application.engineBridge.createPlayer(Integer.parseInt(dataSplit[1]), dataSplit[0]));
  }
    
  @MessageMapping("/createGame")
  public void createGame(String uuidPlayer) {
    GameMethods game = Application.engineBridge.createGame(UUID.fromString(uuidPlayer));
    if (game == null) {
      this.template.convertAndSend("/game/" + uuidPlayer, "Waiting for another Player");
    } else {
      this.template.convertAndSend("/game/" + game.getCurrentPlayer().getUUID(), game);
      this.template.convertAndSend("/game/" + game.getOtherPlayer().getUUID(), game);
    }
  }
    
  @MessageMapping("/endTurn")
  public void endTurn(String uuidGame) {
    this.template.convertAndSend("/game/" + uuidGame, 
        Application.engineBridge.endTurn(UUID.fromString(uuidGame)));
  }
    
  @MessageMapping("/playCard")
  public void playCard(String data) {
    String[] dataSplit = data.split("_");
    try {
      this.template.convertAndSend("/game/" + dataSplit[0], 
          Application.engineBridge.playCard(UUID.fromString(dataSplit[0]), 
              Integer.parseInt(dataSplit[1]), UUID.fromString(dataSplit[2]), 
              Integer.parseInt(dataSplit[3])));
    } catch (EngineException e) {
      this.template.convertAndSend("/game/" + dataSplit[0], e.getMessage());
    }
  }
    
  @MessageMapping("/heroPower")
  public void heroPower(String data) {
    String[] dataSplit = data.split("_");
    try {
      this.template.convertAndSend("/game/" + dataSplit[0], 
          Application.engineBridge.heroPower(UUID.fromString(dataSplit[0]), 
              UUID.fromString(dataSplit[1]), Integer.parseInt(dataSplit[2])));
    } catch (EngineException e) {
      this.template.convertAndSend("/game/" + dataSplit[0], e.getMessage());
    }
  }
    
  @MessageMapping("/attack")
  public void attack(String data) {
    String[] dataSplit = data.split("_");
    try {
      this.template.convertAndSend("/game/" + dataSplit[0], 
          Application.engineBridge.attack(UUID.fromString(dataSplit[0]), 
              Integer.parseInt(dataSplit[1]), Integer.parseInt(dataSplit[2])));
    } catch (EngineException e) {
      this.template.convertAndSend("/game/" + dataSplit[0], e.getMessage());
    }
  }
    
  @MessageMapping("/connectionTest") 
  public void connectionTest(String data) {
    System.out.println(data);
    if (this.uuid1 == null && this.uuid2 != UUID.fromString(data)) {
      this.uuid1 = UUID.fromString(data);
    } else if (this.uuid2 == null && this.uuid1 != UUID.fromString(data)) {
      this.uuid2 = UUID.fromString(data);
    }
    //VOIR WebSocketSession
    /* if(this.uuid1 == UUID.fromString(data)) {
     * try {
     * TimeUnit.SECONDS.sleep(6);
     * System.out.println("test");
     * } catch (InterruptedException e) {
     * e.printStackTrace();
     * }
     * } else if(this.uuid2 == UUID.fromString(data)) {
     * }*/
  }
}
