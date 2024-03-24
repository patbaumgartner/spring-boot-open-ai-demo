package com.patbaumgartner.openai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@Profile("rag")
public class RagConfiguration {

	@Bean
	VectorStore vectors(EmbeddingClient embedding, @Value("Artificial intelligence - Wikipedia.pdf") Resource pdf) {
		SimpleVectorStore vectors = new SimpleVectorStore(embedding);
		PagePdfDocumentReader reader = new PagePdfDocumentReader(pdf);
		TokenTextSplitter splitter = new TokenTextSplitter();
		List<Document> documents = splitter.apply(reader.get());
		vectors.accept(documents);
		return vectors;
	}

	@Bean
	CommandLineRunner vectorClr(ChatClient chatClient, VectorStore vectors) {
		return args -> {
			String message = "Why is the definition of AI difficult?";
			List<Document> documents = vectors.similaritySearch(message);
			String inlined = documents.stream()
				.map(Document::getContent)
				.collect(Collectors.joining(System.lineSeparator()));

			String prompt = """
					You are a virtual assistant.
					You answer questions with data provided in the DOCUMENTS section.
					You are only allowed to use information from the DOCUMENTS paragraph and no other information.
					If you are not sure or don't know, honestly say you don't know.

					DOCUMENTS:
					{documents}
					""";

			Message system = new SystemPromptTemplate(prompt).createMessage(Map.of("documents", inlined));
			UserMessage user = new UserMessage(message);

			Prompt combinedPrompt = new Prompt(List.of(system, user));
			String answer = chatClient.call(combinedPrompt).getResult().getOutput().getContent();
			System.out.printf("\nChatGPT answered: \n\n%s\n", answer);
		};
	}

}
