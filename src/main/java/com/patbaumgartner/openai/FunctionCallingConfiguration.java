package com.patbaumgartner.openai;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
@Profile("function")
public class FunctionCallingConfiguration {

	@Bean
	public Function<WeatherService.Request, WeatherService.Response> weatherFunction() {
		return new WeatherService();
	}

	@Bean
	CommandLineRunner functionClr(ChatModel chatModel) {

		return args -> {
			UserMessage userMessage = new UserMessage("What's the weather like in Munich, Zurich, and New York?");

			ChatResponse response = chatModel.call(new Prompt(List.of(userMessage),
					OpenAiChatOptions.builder().withFunction("weatherFunction").build()));

			String answer = response.getResult().getOutput().getContent();
			System.out.printf("\nChatGPT answered: \n\n%s\n", answer);
		};
	}

}
