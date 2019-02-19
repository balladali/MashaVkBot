package ru.balladali.mashavkbot.handler;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Message;
import lombok.extern.slf4j.Slf4j;
import ru.balladali.mashavkbot.core.handlers.MessageHandler;

import java.util.List;

@Slf4j
public class BotLongPollHandler extends CallbackApiLongPoll {

    private List<MessageHandler> messageHandlers;

    public BotLongPollHandler(VkApiClient client, GroupActor actor, List<MessageHandler> messageHandlers) {
        super(client, actor);
        this.messageHandlers = messageHandlers;
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
