package si.um.feri.vrbancic.sentence_similarity_example;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import ai.djl.ModelException;
import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.huggingface.translator.TextEmbeddingTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
// import ai.onnxruntime.OnnxTensor;
// import ai.onnxruntime.OnnxValue;
// import ai.onnxruntime.OrtEnvironment;
// import ai.onnxruntime.OrtSession;

public class ONNXSimple {

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String[] text = {"Hello world"};

        Criteria<String, float[]> criteria =
                Criteria.builder()
                        .setTypes(String.class, float[].class)
                        .optModelUrls("djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2")
                        //.optModelPath(Paths.get("src/main/resources/all-MiniLM-L6-v2"))
                        //.optEngine("OnnxRuntime")
                        .optTranslatorFactory(new TextEmbeddingTranslatorFactory())
                        .optProgress(new ProgressBar())
                        .build();

        try (ZooModel<String, float[]> model = criteria.loadModel(); Predictor<String, float[]> predictor = model.newPredictor()) {
            float[] res = predictor.predict(text[0]);
            System.out.println("Embedding: " + Arrays.toString(res));

            System.out.println("Similarity: " + calculateCosineSimilarity(res, res));
        }
    }

    // Calculate cosine similarity between two vectors
    private static double calculateCosineSimilarity(float[] vector1, float[] vector2) {
      // Convert float[] to double[]
      double[] doubleVector1 = new double[vector1.length];
      double[] doubleVector2 = new double[vector2.length];
      for (int i = 0; i < vector1.length; i++) {
        doubleVector1[i] = vector1[i];
        doubleVector2[i] = vector2[i];
      }

      // Create RealVector instances using the Apache Commons Math library
      RealVector realVector1 = new ArrayRealVector(doubleVector1);
      RealVector realVector2 = new ArrayRealVector(doubleVector2);

      // Calculate cosine similarity using dot product and norms
      double dotProduct = realVector1.dotProduct(realVector2);
      double norm1 = realVector1.getNorm();
      double norm2 = realVector2.getNorm();

      // Handle division by zero
      if (norm1 == 0 || norm2 == 0) {
        return 0.0; // Return 0 similarity if any of the vectors has zero magnitude
      }

      return dotProduct / (norm1 * norm2);
    }

  //   public static void main(String[] args) throws Exception {

  //   String[] sentences = new String[]{"Hello world"};

  //   // https://docs.djl.ai/extensions/tokenizers/index.html
  //   HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance(Paths.get("src/main/resources/all-MiniLM-L6-v2/tokenizer.json"));
  //   Encoding[] encodings = tokenizer.batchEncode(sentences);

  //   // https://onnxruntime.ai/docs/get-started/with-java.html
  //   OrtEnvironment environment = OrtEnvironment.getEnvironment();
  //   OrtSession session = environment.createSession("src/main/resources/all-MiniLM-L6-v2/model.onnx");

  //   long[][] input_ids0 = new long[encodings.length][];
  //   long[][] attention_mask0 = new long[encodings.length][];
  //   long[][] typeIds0 = new long[encodings.length][];

  //   for (int i = 0; i < encodings.length; i++) {
  //     input_ids0[i] = encodings[i].getIds();
  //     attention_mask0[i] = encodings[i].getAttentionMask();
  //     typeIds0[i] = encodings[i].getTypeIds();
  //   }

  //   OnnxTensor inputIds = OnnxTensor.createTensor(environment, input_ids0);
  //   OnnxTensor attentionMask = OnnxTensor.createTensor(environment, attention_mask0);
  //   OnnxTensor types = OnnxTensor.createTensor(environment, typeIds0);

  //   Map<String,OnnxTensor> inputs = new HashMap<>();
  //   inputs.put("input_ids", inputIds);
  //   inputs.put("attention_mask", attentionMask);
  //   inputs.put("token_type_ids", types);

  //   try (OrtSession.Result results = session.run(inputs)) {
  //     OnnxValue lastHiddenState = results.get(0);

  //     float[][][] tokenEmbeddings = (float[][][]) lastHiddenState.getValue();

  //     System.out.println(tokenEmbeddings[0][0][0]);

  //     System.out.println(tokenEmbeddings[0][0][0]);
  //     System.out.println(tokenEmbeddings[0][1][0]);
  //     System.out.println(tokenEmbeddings[0][2][0]);
  //     System.out.println(tokenEmbeddings[0][3][0]);
  //   }
  // }
}