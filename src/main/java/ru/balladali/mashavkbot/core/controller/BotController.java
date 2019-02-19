package ru.balladali.mashavkbot.core.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.balladali.mashavkbot.core.entity.VkRequest;
import ru.balladali.mashavkbot.handler.BotCallbackHandler;

@RestController
@Profile("callback")
public class BotController {

    private BotCallbackHandler botCallbackHandler;

    public BotController(BotCallbackHandler botCallbackHandler) {
        this.botCallbackHandler = botCallbackHandler;
    }

    @PostMapping(path = "/callback")
    public ResponseEntity callback(@RequestBody VkRequest vkRequest) {
        botCallbackHandler.handle(vkRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

}
