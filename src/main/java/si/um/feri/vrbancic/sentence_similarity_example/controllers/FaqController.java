package si.um.feri.vrbancic.sentence_similarity_example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import si.um.feri.vrbancic.sentence_similarity_example.models.Faq;
import si.um.feri.vrbancic.sentence_similarity_example.models.RequestFaq;
import si.um.feri.vrbancic.sentence_similarity_example.services.FaqService;

@RestController
public class FaqController {

    @Autowired
    private FaqService faqService;

    // get all FAQ
    @GetMapping("/api/faq")
    public List<Faq> getAllFaq() {
        return faqService.getAllFaq();
    }

    // post question and get most similar faq
    @PostMapping("/api/faq")
    public List<Faq> getMostSimilarFaq(@RequestBody RequestFaq requestFaq) {
        if (requestFaq.getTopK() == 0) {
            requestFaq.setTopK(1);
        }
        return faqService.getMostSimilarFaqs(requestFaq);
    }
    
}
