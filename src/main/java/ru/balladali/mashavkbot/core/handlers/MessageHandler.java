package ru.balladali.mashavkbot.core.handlers;

import com.vk.api.sdk.objects.messages.Message;

public interface MessageHandler {

    void handle(Message entity);

    boolean needHandle(Message message);

    void sendAnswer(Message messageEntity, String answer);
}
