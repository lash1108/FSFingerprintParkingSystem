package com.fingerprint.parkingfpaaccessmanager.controller.webSocket;

import com.fingerprint.parkingfpaaccessmanager.model.webSocket.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"*"})
public class GreetingRestController {

    private final GreetingController greetingController;

    public GreetingRestController(GreetingController greetingController) {
        this.greetingController = greetingController;
    }

    @PostMapping("/api/sendGreeting")
    public void sendGreeting(@RequestBody HelloMessage message) {
        greetingController.sendGreeting(message.getName());
    }

}
