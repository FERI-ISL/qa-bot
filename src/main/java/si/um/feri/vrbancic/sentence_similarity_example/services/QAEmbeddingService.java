package si.um.feri.vrbancic.sentence_similarity_example.services;

import org.springframework.stereotype.Service;

@Service
public class QAEmbeddingService {
    
    // TODO: naložite model za vstavljanje (embedding) vprašanj in odgovorov
    // https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2

    public float[] embedQuestion(String question) {
        // TODO: implementirajte vstavljanje (embedding) vprašanja ter vrnite vektor
        return null;
    }

}
