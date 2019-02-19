package ru.balladali.mashavkbot.core.handlers;

import com.google.common.base.Strings;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Message;
import org.apache.commons.lang3.StringUtils;
import ru.balladali.mashavkbot.core.entity.YouTubeVideoEntity;
import ru.balladali.mashavkbot.core.services.YouTubeService;

import java.io.IOException;
import java.util.List;

public class YouTubeHandler extends AbstractMessageHandler {
    private YouTubeService youTubeService;

    public YouTubeHandler(YouTubeService youTubeService, VkApiClient client, GroupActor groupActor) {
        super(client, groupActor);
        this.youTubeService = youTubeService;
    }

    @Override
    public void handle(Message entity) {
        String message = entity.getBody();
        if (needHandle(message)) {
            String searchQuery = message.toLowerCase().replace("маша, найди", "").trim();
            if (Strings.isNullOrEmpty(searchQuery)) {
                sendAnswer(entity, "Ну и чего искать-то?");
            } else {
                try {
                    List<YouTubeVideoEntity> foundVideos = youTubeService.search(searchQuery);
                    sendAnswer(entity, foundVideos.get(0).getLink());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean needHandle(String message) {
        return StringUtils.containsIgnoreCase(message, "Маша, найди");
    }

}
