package si.um.feri.vrbancic.sentence_similarity_example.models;

import java.util.Arrays;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document   
public class Faq {

    @Indexed
    private String id;
    private String question;
    private String answer;
    private String language;
    private float[] questionEmbeddings;
    private float[] answerEmbeddings;

    public Faq() {
    }

    public Faq(String id, String question, String answer, String language, float[] questionEmbeddings, float[] answerEmbeddings) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.language = language;
        this.questionEmbeddings = questionEmbeddings;
        this.answerEmbeddings = answerEmbeddings;
    }

    public Faq(String question, String answer, String language, float[] questionEmbeddings, float[] answerEmbeddings) {
        this.question = question;
        this.answer = answer;
        this.language = language;
        this.questionEmbeddings = questionEmbeddings;
        this.answerEmbeddings = answerEmbeddings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float[] getQuestionEmbeddings() {
        return questionEmbeddings;
    }

    public void setQuestionEmbeddings(float[] questionEmbeddings) {
        this.questionEmbeddings = questionEmbeddings;
    }

    public float[] getAnswerEmbeddings() {
        return answerEmbeddings;
    }

    public void setAnswerEmbeddings(float[] answerEmbeddings) {
        this.answerEmbeddings = answerEmbeddings;
    }

    @Override
    public String toString() {
        return "Faq [id=" + id + ", question=" + question + ", answer=" + answer + ", language=" + language
                + ", questionEmbeddings=" + Arrays.toString(questionEmbeddings) + ", answerEmbeddings="
                + Arrays.toString(answerEmbeddings) + "]";
    }

}
