package com.patbaumgartner.openai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
@Profile("function")
public class FunctionCallingConfiguration {

	@Bean
	@Description("Get the weather in location")
	public Function<WeatherService.Request, WeatherService.Response> weatherFunction() {
		return new WeatherService();
	}

	@Bean
	CommandLineRunner functionClr(ChatClient chatClient) {
		return args -> {
			UserMessage userMessage = new UserMessage("What's the weather like in Munich, Zurich, and New York?");

			ChatResponse response = chatClient.call(new Prompt(List.of(userMessage),
					OpenAiChatOptions.builder().withFunction("weatherFunction").build()));

			String answer = response.getResult().getOutput().getContent();
			System.out.printf("\nChatGPT answered: \n\n%s\n", answer);
		};
	}

}
