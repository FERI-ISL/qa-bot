package si.um.feri.vrbancic.sentence_similarity_example.services;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import ai.djl.huggingface.translator.TextEmbeddingTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

@Service
public class QAEmbeddingService {
    
    private Criteria<String, float[]> criteria =
                Criteria.builder()
                        .setTypes(String.class, float[].class)
                        .optModelUrls("djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2")
                        //.optModelPath(Paths.get("path to model"))
                        //.optEngine("OnnxRuntime")
                        .optTranslatorFactory(new TextEmbeddingTranslatorFactory())
                        .optProgress(new ProgressBar())
                        .build();

    
    public float[] embedQuestion(String question) {
        try (ZooModel<String, float[]> model = criteria.loadModel(); Predictor<String, float[]> predictor = model.newPredictor()) {
            float[] embeddedQuestion = predictor.predict(question);
            return embeddedQuestion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
