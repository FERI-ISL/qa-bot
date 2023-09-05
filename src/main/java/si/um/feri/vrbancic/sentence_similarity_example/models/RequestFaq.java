package si.um.feri.vrbancic.sentence_similarity_example.models;

public class RequestFaq {

    private String question;
    private int topK;

    public RequestFaq() {
    }

    public RequestFaq(String question, int topK) {
        this.question = question;
        this.topK = topK;
    }

    public String getQuestion() {
        return question;
    }

    public int getTopK() {
        return topK;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }

    @Override
    public String toString() {
        return "RequestFaq [question=" + question + ", topK=" + topK + "]";
    }
    
}
