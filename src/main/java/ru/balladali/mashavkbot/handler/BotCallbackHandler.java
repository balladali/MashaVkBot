package ru.balladali.mashavkbot.handler;

import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.messages.Message;
import ru.balladali.mashavkbot.core.entity.VkRequest;
import ru.balladali.mashavkbot.core.handlers.MessageHandler;

import java.util.List;

public class BotCallbackHandler extends CallbackApi {

    private List<MessageHandler> messageHandlers;

    public BotCallbackHandler(List<MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    public void handle(VkRequest vkRequest) {
        switch (vkRequest.getType()) {
            case "message_new":
                messageNew(vkRequest.getGroupId(), (Message) vkRequest.getObject());
                break;
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
