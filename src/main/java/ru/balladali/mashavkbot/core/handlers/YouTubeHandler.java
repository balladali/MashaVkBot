package ru.balladali.mashavkbot.core.handlers;

import com.google.common.base.Strings;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.balladali.mashavkbot.core.entity.YouTubeVideoEntity;
import ru.balladali.mashavkbot.core.services.YouTubeService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class YouTubeHandler extends AbstractMessageHandler {
    private YouTubeService youTubeService;

    public YouTubeHandler(YouTubeService youTubeService, VkApiClient client, GroupActor groupActor) {
        super(client, groupActor);
        this.youTubeService = youTubeService;
    }

    @Override
    public void handle(Message message) {
        String searchQuery = message.getBody().toLowerCase().replace("маша, найди", "").trim();
        if (Strings.isNullOrEmpty(searchQuery)) {
            sendAnswer(message, "Ну и чего искать-то?");
        } else {
            try {
                List<YouTubeVideoEntity> foundVideos = youTubeService.search(searchQuery);
                sendAnswer(message, foundVideos.get(0).getLink());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lastMessageId = message.getId();
    }

    @Override
    public boolean needHandle(Message message) {
        log.info("Message ID = {}, last message ID = {}, handler = {}",
                message.getId(), lastMessageId, this.getClass());
        return !isAnswerSent(message)
                && StringUtils.containsIgnoreCase(message.getBody(), "Маша, найди");
    }

}
