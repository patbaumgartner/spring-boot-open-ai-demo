package com.patbaumgartner.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
@Profile("simple")
public class SimpleConfiguration {

	@Bean
	CommandLineRunner simpleClr(ChatClient.Builder chatClientBuilder) {
		ChatClient chatClient = chatClientBuilder.build();

		return args -> {
			String answer = chatClient.prompt().user("Tell me a developer joke.").call().content();
			System.out.printf("\nChatGPT answered: \n\n%s\n", answer);
		};
	}

}
