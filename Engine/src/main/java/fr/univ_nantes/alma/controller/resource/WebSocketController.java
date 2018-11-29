package fr.univ_nantes.alma.controller.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import fr.univ_nantes.alma.Application;

@Controller
public class WebSocketController {
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@MessageMapping("/getHeros")
    public void getHeros(String message) {
    	System.out.println(message);
    	this.template.convertAndSend("/greeting", Application.engine.getHeros());
    }

    @MessageMapping("/createPlayer")
    public void createPlayer(String message) {
    	System.out.println(message);
    	this.template.convertAndSend("/greeting", Application.engine.createPlayer(1, "Bob"));
    }
}
