package com.patbaumgartner.openai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
@Profile("simple")
public class SimpleConfiguration {

	@Bean
	CommandLineRunner simpleClr(ChatClient chatClient) {
		return args -> {
			String answer = chatClient.call("Tell me a developer joke.");
			System.out.printf("\nChatGPT answered: \n\n%s\n", answer);
		};
	}

}
