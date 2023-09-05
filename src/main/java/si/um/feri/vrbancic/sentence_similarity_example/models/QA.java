package si.um.feri.vrbancic.sentence_similarity_example.models;

import com.opencsv.bean.CsvBindByName;

public class QA {

    @CsvBindByName(column = "question")
    private String question;
    @CsvBindByName(column = "answer")
    private String answer;
    @CsvBindByName(column = "language")
    private String language;

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

    @Override
    public String toString() {
        return "QA [question=" + question + ", answer=" + answer + ", language=" + language + "]";
    }
    
}
