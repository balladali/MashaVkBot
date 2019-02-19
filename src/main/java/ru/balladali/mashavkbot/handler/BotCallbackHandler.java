package ru.balladali.mashavkbot.handler;

import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.balladali.mashavkbot.core.entity.VkRequest;
import ru.balladali.mashavkbot.core.handlers.MessageHandler;

import java.util.List;

public class BotCallbackHandler extends CallbackApi {

    private List<MessageHandler> messageHandlers;
    private String confirmationCode;

    public BotCallbackHandler(List<MessageHandler> messageHandlers, String confirmationCode) {
        this.messageHandlers = messageHandlers;
        this.confirmationCode = confirmationCode;
    }

    public ResponseEntity<String> handle(VkRequest vkRequest) {
        switch (vkRequest.getType()) {
            case "confirmation":
                return new ResponseEntity<>(confirmationCode, HttpStatus.OK);
            case "message_new":
                messageNew(vkRequest.getGroupId(), (Message) vkRequest.getObject());
            default:
                return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public void messageNew(Integer groupId, Message message) {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.needHandle(message.getBody())) {
                messageHandler.handle(message);
                return;
            }
        }
    }

}
