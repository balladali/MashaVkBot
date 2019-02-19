package ru.balladali.mashavkbot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.balladali.mashavkbot.handler.BotLongPollHandler;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan
public class MashaVkBotApplication {

	@Autowired
	private VkApiClient client;

	@Autowired
	private GroupActor actor;

	public static void main(String[] args) {
		SpringApplication.run(MashaVkBotApplication.class, args);
	}

	@Configuration
	@Profile("longpoll")
	public class Longpoll {

		@Autowired
		private BotLongPollHandler botLongPollHandler;

		@PostConstruct
		private void init() throws ClientException, ApiException {
			botLongPollHandler.run();
		}
	}
}

