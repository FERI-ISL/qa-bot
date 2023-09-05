package si.um.feri.vrbancic.sentence_similarity_example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SentenceSimilarityExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentenceSimilarityExampleApplication.class, args);
	}

}
