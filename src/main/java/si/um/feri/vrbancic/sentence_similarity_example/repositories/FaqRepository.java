package si.um.feri.vrbancic.sentence_similarity_example.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.vrbancic.sentence_similarity_example.models.Faq;

public interface FaqRepository extends MongoRepository<Faq, String> {
    
}
