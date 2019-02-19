package ru.balladali.mashavkbot.core.config;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.balladali.mashavkbot.core.handlers.ConversationHandler;
import ru.balladali.mashavkbot.core.handlers.MessageHandler;
import ru.balladali.mashavkbot.core.handlers.YouTubeHandler;
import ru.balladali.mashavkbot.core.services.YouTubeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class HandlerConfig {

    @Bean(name = "conversationHandler")
    @Order(Integer.MAX_VALUE)
    public ConversationHandler getConversationHandler(VkApiClient client, GroupActor groupActor) {
        return new ConversationHandler(client, groupActor);
    }

    @Autowired
    @Order(1)
    @Bean(name = "youtubeHandler")
    public YouTubeHandler getYouTubeHandler(YouTubeService youTubeService, VkApiClient client, GroupActor groupActor) {
        return new YouTubeHandler(youTubeService, client, groupActor);
    }

    @Bean
    public List<MessageHandler> messageHandlers(Map<String, MessageHandler> messageHandlers) {
        List<MessageHandler> messageHandlersList = new ArrayList<>();
        messageHandlersList.add(messageHandlers.get("youtubeHandler"));
        messageHandlersList.add(messageHandlers.get("conversationHandler"));
        return messageHandlersList;
    }
}
