package ru.balladali.mashavkbot.core.config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.balladali.mashavkbot.core.handlers.MessageHandler;
import ru.balladali.mashavkbot.handler.BotCallbackHandler;
import ru.balladali.mashavkbot.handler.BotLongPollHandler;

import java.util.List;

@Configuration
public class BotConfig {

    @Bean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        return new VkApiClient(transportClient);
    }

    @Bean
    public GroupActor groupActor(@Value("${credential.vk.group-id}") String groupId,
                                 @Value("${credential.vk.key}") String accessToken) {
        return new GroupActor(Integer.parseInt(groupId), accessToken);
    }

    @Bean
    @Profile("longpoll")
    public BotLongPollHandler mashaLongpollHandler(List<MessageHandler> messageHandlers, GroupActor groupActor) {
        return new BotLongPollHandler(vkApiClient(), groupActor, messageHandlers);
    }

    @Bean
    @Profile("callback")
    public BotCallbackHandler mashaCallbackHandler(List<MessageHandler> messageHandlers) {
        return new BotCallbackHandler(messageHandlers);
    }
}
