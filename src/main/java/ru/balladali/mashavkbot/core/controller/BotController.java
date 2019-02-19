package ru.balladali.mashavkbot.core.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.balladali.mashavkbot.handler.BotCallbackHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Reader;

@RestController
@Profile("callback")
public class BotController {

    private BotCallbackHandler botCallbackHandler;
    private Gson gson = new Gson();

    public BotController(BotCallbackHandler botCallbackHandler) {
        this.botCallbackHandler = botCallbackHandler;
    }

    @PostMapping(path = "/callback")
    public ResponseEntity<String> callback(HttpServletRequest request) throws IOException {
        Reader reader = request.getReader();
        JsonObject vkRequest = gson.fromJson(reader, JsonObject.class);
        String body = botCallbackHandler.handle(vkRequest);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
