package si.um.feri.vrbancic.sentence_similarity_example;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;

import java.io.IOException;
import java.nio.file.Paths;

public class Tokenizer {

    /*
     * Class for Tokenization and encoding
     * */
    private Encoding[] encodings;
    private long[][] ids;
    private long[][] attentionMasks;
    private long[][] typeIds;
    private final HuggingFaceTokenizer tokenizer;

    public Tokenizer(String tokenizerJsonPath) throws IOException {
        this.tokenizer = HuggingFaceTokenizer.newInstance(Paths.get(tokenizerJsonPath));
    }

    public void encode(String[] inputTexts) {
        this.encodings = tokenizer.batchEncode(inputTexts);
        
        // get ids and attention masks
        this.ids = new long[this.encodings.length][];
        this.attentionMasks = new long[this.encodings.length][];
        this.typeIds = new long[this.encodings.length][];

        for (int i = 0; i < encodings.length; i++) {
            this.ids[i] = this.encodings[i].getIds();
            this.attentionMasks[i] = this.encodings[i].getAttentionMask();
            this.typeIds[i] = this.encodings[i].getTypeIds();
        }
    }

    public long[][] getIds() {
        return this.ids;
    }

    public long[][] getAttentionMask() {
        return this.attentionMasks;
    }

    public long[][] getTypeIds() {
        return this.typeIds;
    }

}
