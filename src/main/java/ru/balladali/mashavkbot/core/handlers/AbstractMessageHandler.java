package ru.balladali.mashavkbot.core.handlers;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class AbstractMessageHandler implements MessageHandler {

    private VkApiClient client;
    private GroupActor groupActor;

    protected volatile Integer lastMessageId = -1;

    public AbstractMessageHandler(VkApiClient client, GroupActor groupActor) {
        this.client = client;
        this.groupActor = groupActor;
    }

    @Override
    public void sendAnswer(Message messageEntity, String answer) {
        if (messageEntity.getUserId() != null) {
            try {
                client.messages().send(groupActor).userId(messageEntity.getUserId()).message(answer).execute();
            } catch (ApiException | ClientException e) {
                log.error("Error during message sending", e);
            }
        }
    }

    protected synchronized boolean isAnswerSent(Message message) {
        return Objects.equals(message.getId(), lastMessageId);
    }
}
