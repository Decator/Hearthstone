package fr.univ_nantes.alma.controller.resource;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import fr.univ_nantes.alma.controller.model.UserResponse;
import fr.univ_nantes.alma.controller.model.User;

@Controller
public class UserController {


    @MessageMapping("/game")
    @SendTo("/topic/greetings")
    public UserResponse getUser(User user) {
        return new UserResponse("Hi " + user.getName());
    }
}
