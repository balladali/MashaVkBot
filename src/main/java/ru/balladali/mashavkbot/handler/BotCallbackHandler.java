package ru.balladali.mashavkbot.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.messages.Message;
import ru.balladali.mashavkbot.core.handlers.MessageHandler;

import java.util.List;

public class BotCallbackHandler extends CallbackApi {

    private List<MessageHandler> messageHandlers;
    private String confirmationCode;

    private Gson gson = new Gson();

    public BotCallbackHandler(List<MessageHandler> messageHandlers, String confirmationCode) {
        this.messageHandlers = messageHandlers;
        this.confirmationCode = confirmationCode;
    }

    public String handle(JsonObject vkRequest) {
        String type = vkRequest.get("type").getAsString();
        Integer groupId = vkRequest.get("group_id").getAsInt();
        switch (type) {
            case "confirmation":
                return confirmationCode;
            case "message_new":
                JsonObject object = vkRequest.getAsJsonObject("object");
                Message message = gson.fromJson(object, Message.class);
                messageNew(groupId, message);
                break;
        }
        return "ok";
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
