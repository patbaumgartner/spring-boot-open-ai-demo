package com.patbaumgartner.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@Profile("context")
public class ContextConfiguration {

	@Bean
	CommandLineRunner contextClr(ChatClient.Builder chatClientBuilder) {
		ChatClient chatClient = chatClientBuilder.build();

		return args -> {
			String message = "What is today's temperature?";

			Prompt prompt = new Prompt(List.of(new SystemMessage("""
					Current Weather
					7:02 PM
					3°C
					Mostly clear
					RealFeel -3°
					Wind Gusts 29 km/h
					Humidity 78%
					Indoor Humidity 39% (Slightly Dry)
					Dew Point -1° C
					Pressure 1011 mb
					Cloud Cover 26%
					Visibility 24 km
					Cloud Ceiling 12200 m"""), new UserMessage(message)));

			String answer = chatClient.prompt(prompt).call().content();
			System.out.printf("\nChatGPT answered: \n\n%s\n", answer);
		};
	}

}
