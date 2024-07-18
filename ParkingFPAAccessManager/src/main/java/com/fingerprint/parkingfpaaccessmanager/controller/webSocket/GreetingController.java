package com.fingerprint.parkingfpaaccessmanager.controller.webSocket;


import com.fingerprint.parkingfpaaccessmanager.model.webSocket.Greeting;
import com.fingerprint.parkingfpaaccessmanager.model.webSocket.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

@Controller
@CrossOrigin(origins = {"*"})
public class GreetingController {

    private final SimpMessagingTemplate messagingTemplate;

    public GreetingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    public void sendGreeting(String name) {
        Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(name) + "!");
        messagingTemplate.convertAndSend("/topic/greetings", greeting);
    }

}
