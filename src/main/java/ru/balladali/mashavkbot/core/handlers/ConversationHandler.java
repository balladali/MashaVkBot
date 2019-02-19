package ru.balladali.mashavkbot.core.handlers;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Message;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.balladali.mashavkbot.core.entity.BotContext;

import java.util.*;

public class ConversationHandler extends AbstractMessageHandler {

    private final String ANSWER_URL = "http://p-bot.ru/api/getAnswer";
    private final String PUBLIC_API = "public-api";
    private final String USER_NAME = "Masha";
    private final String ANSWER_FIELD = "answer";

    private Map<Integer, BotContext> contextMap = new HashMap<>();

    public ConversationHandler(VkApiClient client, GroupActor groupActor) {
        super(client, groupActor);
    }

    @Override
    public void handle(Message entity) {
        String message = entity.getBody();
        Integer chatId = entity.getChatId() == null ? entity.getUserId() : entity.getChatId();
        message = message.replaceAll("Маша,", "").replaceAll("маша,", "").trim();
        String answer = getAnswer(message, chatId);
        sendAnswer(entity, answer);
    }

    @Override
    public boolean needHandle(String message) {
        return StringUtils.containsIgnoreCase(message, "Маша");
    }

    private String getAnswer(String message, Integer chatId) {
        BotContext botContext = null;
        if (!contextMap.containsKey(chatId)) {
            botContext = BotContext.builder().build();
            contextMap.put(chatId, botContext);
        } else {
            botContext = contextMap.get(chatId);
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        long timestamp = System.currentTimeMillis();
        Random rand = new Random();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("request", message);
        parameters.add("request_1", botContext.getRequest1());
        parameters.add("answer_1", botContext.getAnswer1());
        parameters.add("request_2", botContext.getRequest2());
        parameters.add("answer_2", botContext.getAnswer2());
        parameters.add("request_3", botContext.getRequest3());
        parameters.add("answer_3", botContext.getAnswer3());
        parameters.add("user_name", USER_NAME);
        parameters.add("bot_name", "pBot");
        parameters.add("dialog_lang", "ru");
        parameters.add("a", PUBLIC_API);
        parameters.add("b", String.valueOf(Integer.toUnsignedLong(crc(timestamp + "b"))));
        parameters.add("c", String.valueOf(Integer.toUnsignedLong(getSign(timestamp))));
        parameters.add("d", String.valueOf(Integer.toUnsignedLong(crc(System.currentTimeMillis() + "d"))));
        parameters.add("e", String.format(Locale.ENGLISH, "%.15f", rand.nextFloat()));
        parameters.add("t", String.valueOf(timestamp));
        parameters.add("x", String.format(Locale.ENGLISH, "%.15f", rand.nextFloat() * 0xa));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ANSWER_URL, request, String.class);
        String content = response.getBody();
        JSONObject json = new JSONObject(content);
        String answer = json.getString(ANSWER_FIELD);
        updateContext(botContext, message, answer);
        contextMap.put(chatId, botContext);
        return answer;
    }

    private int crc(String param) {
        List<Integer> b = abc();
        int c = -0x1;
        for (int k = 0; k < param.length(); k++) {
            c = c >>> 0x8 ^ b.get((c ^ param.charAt(k)) & 0xff);
        }
        return ~c;
    }

    private List<Integer> abc() {
        int a;
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            a = i;
            for (int j = 0; j < 8; j++) {
                a = (a & 0x1) == 1 ? 0xedb88320 ^ a >>> 0x1 : a >>> 0x1;
            }
            result.add(a);
        }
        return result;
    }

    private int getSign(long param) {
        return crc("public-api" + param + "WxvttruvF01cvHy8" + "8r5yD8pl8lLcz20G" + "8dY72lsSOvXnJF4T");
    }

    private void updateContext(BotContext context, String request, String answer) {
        context.setRequest3(context.getRequest2());
        context.setRequest2(context.getRequest1());
        context.setRequest1(request);

        context.setAnswer3(context.getAnswer2());
        context.setAnswer2(context.getAnswer1());
        context.setAnswer1(answer);
    }
}
