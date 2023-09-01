package si.um.feri.vrbancic.sentence_similarity_example.services;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import io.micrometer.core.ipc.http.HttpSender.Request;
import jakarta.annotation.PostConstruct;
import si.um.feri.vrbancic.sentence_similarity_example.models.Faq;
import si.um.feri.vrbancic.sentence_similarity_example.models.QA;
import si.um.feri.vrbancic.sentence_similarity_example.models.RequestFaq;
import si.um.feri.vrbancic.sentence_similarity_example.repositories.FaqRepository;

@Service
public class FaqService {

    @Autowired
    private FaqRepository faqRepository;
    @Autowired
    private QAEmbeddingService qaEmbeddingService;

    @PostConstruct
    public void initializeDatabase() {
        if (faqRepository.findAll().isEmpty()) {
            // initialize database with some faqs
            List<QA> csvList = getListOfQA(Path.of("src/main/resources/um_qa_eng.csv"), QA.class);

            for (QA qa : csvList) {
                System.out.println(qa.getQuestion() + "\n\t\t" + qa.getAnswer());
                float[] questionEmbeddings = qaEmbeddingService.embedQuestion(qa.getQuestion());
                float[] answerEmbeddings = qaEmbeddingService.embedQuestion(qa.getAnswer());
                Faq faq = new Faq(qa.getQuestion(), qa.getAnswer(), qa.getLanguage(), questionEmbeddings, answerEmbeddings);
                faqRepository.insert(faq);
            }
        }
    }

    private List<QA> getListOfQA(Path path, Class<? extends QA> clazz) {
        List<QA> csvList = null;

        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<QA> cb = new CsvToBeanBuilder<QA>(reader).withType(clazz).withSeparator(',').build();
            csvList = cb.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return csvList;
    }

    public List<Faq> getAllFaq() {
        return faqRepository.findAll();
    }

    public Faq save(Faq faq) {
        return faqRepository.insert(faq);
    }

    public List<Faq> getMostSimilarFaqs(RequestFaq requestFaq) {
        List<Faq> faqs = faqRepository.findAll();
        HashMap<Faq, Double> faqSimilarityMap = new HashMap<>();

        // embed question
        float[] questionEmbeddings = qaEmbeddingService.embedQuestion(requestFaq.getQuestion());

        // calculate cosine similarity between question and all faqs
        // double highestSimilarity = 0;
        // Faq mostSimilarFaq = null;

        for (Faq faq : faqs) {
            double questionSimilarity = calculateCosineSimilarity(faq.getQuestionEmbeddings(), questionEmbeddings);
            double answerSimilarity = calculateCosineSimilarity(faq.getAnswerEmbeddings(), questionEmbeddings);
            double faqSimilarity = 0;

            if (questionSimilarity > answerSimilarity) {
                faqSimilarity = questionSimilarity;
            } else {
                faqSimilarity = answerSimilarity;
            }

            faqSimilarityMap.put(faq, faqSimilarity);

            // if (faqSimilarity > highestSimilarity) {
            //     highestSimilarity = faqSimilarity;
            //     mostSimilarFaq = faq;
            // }
        }

        List<Faq> mostSimilarFaqs = new ArrayList<>();
        faqSimilarityMap
            .entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue())).limit(requestFaq.getTopK())
            .forEach(k -> mostSimilarFaqs.add(k.getKey()));
        return mostSimilarFaqs;
    }

    private double calculateCosineSimilarity(float[] vector1, float[] vector2) {
        
        // convert float[] to double[
        double[] doubleVector1 = new double[vector1.length];
        double[] doubleVector2 = new double[vector2.length];

        for (int i = 0; i < vector1.length; i++) {
            doubleVector1[i] = vector1[i];
            doubleVector2[i] = vector2[i];
        }

        RealVector realVector1 = new ArrayRealVector(doubleVector1);
        RealVector realVector2 = new ArrayRealVector(doubleVector2);

        // calculate cosine similarity using dot product and norms
        double dotProduct = realVector1.dotProduct(realVector2);
        double norm1 = realVector1.getNorm();
        double norm2 = realVector2.getNorm();

        // Handle division by zero
        if (norm1 == 0 || norm2 == 0) {
            return 0.0; // Return 0 similarity if any of the vectors has zero magnitude
        }

        return dotProduct / (norm1 * norm2);
    }
    
}
