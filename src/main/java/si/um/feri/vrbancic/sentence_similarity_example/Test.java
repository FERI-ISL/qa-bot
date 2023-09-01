package si.um.feri.vrbancic.sentence_similarity_example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.nd4j.linalg.api.ndarray.BaseNDArray;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.distances.CosineSimilarity;
import org.nd4j.linalg.factory.BaseNDArrayFactory;
import org.nd4j.linalg.factory.NDArrayFactory;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OnnxValue;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

public class Test {

    public static double cosine_similarity(float[] tokenEmbeddings1, float[] tokenEmbeddings2) {
        double cosim = vector_dot(tokenEmbeddings1, tokenEmbeddings2) / (vector_norm(tokenEmbeddings1) * vector_norm(tokenEmbeddings2));
        return cosim;
    }

    public static double vector_dot(float[] tokenEmbeddings1, float[] tokenEmbeddings2) {
        double sum = 0;
        for (int i = 0; i < tokenEmbeddings1.length && i < tokenEmbeddings2.length; i++)
            sum += tokenEmbeddings1[i] * tokenEmbeddings2[i];
        return sum;
    }

    public static double vector_norm(float[] tokenEmbeddings1) {
        double sum = 0;
        for (double v : tokenEmbeddings1)
            sum += v * v;
        return Math.sqrt(sum);
    }

    // generate main method
    public static void main(String[] args) {
        
        String[] sentences1 = new String[]{
            "The cat sits outside",
            "A man is playing guitar",
            "The new movie is awesome"
        };

        String[] sentences2 = new String[]{
            "The dog plays in the garden",
            "A woman watches TV",
            "The new movie is so great"
        };

        // String[] sentences1 = new String[]{
        //     "Hello"
        // };

        // String[] sentences2 = new String[]{
        //     "World"
        // };

        try {
            // Create ONNX environment and session
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            String modelPath = "src/main/resources/all-MiniLM-L6-v2/model.onnx";

            OrtSession session = env.createSession(modelPath, new OrtSession.SessionOptions());

            System.out.println("model input names" + session.getInputNames());
            System.out.println("model input info: " + session.getInputInfo());
            System.out.println("model output names" + session.getOutputNames());
            System.out.println("model output info: " + session.getOutputInfo());

            Tokenizer tokenizer = new Tokenizer("src/main/resources/all-MiniLM-L6-v2/tokenizer.json");
            tokenizer.encode(sentences1);
            
            long[][] inputIds1 = tokenizer.getIds();
            long[][] attentionMask1 = tokenizer.getAttentionMask();
            long[][] typeIds1 = tokenizer.getTypeIds();

            OnnxTensor ids1 = OnnxTensor.createTensor(env, inputIds1);
            OnnxTensor masks1 = OnnxTensor.createTensor(env, attentionMask1);
            OnnxTensor types1 = OnnxTensor.createTensor(env, typeIds1);

            Map<String,OnnxTensor> inputs = new HashMap<>();
            inputs.put("input_ids", ids1);
            inputs.put("attention_mask", masks1);
            inputs.put("token_type_ids", types1);

            float[][][] tokenEmbeddings1;

            try (OrtSession.Result results = session.run(inputs)) {
                OnnxValue lastHiddenState = results.get(0);
                tokenEmbeddings1 = (float[][][]) lastHiddenState.getValue();
                //tokenEmbeddings1 = (BaseNDArray[]) lastHiddenState.getValue();
          
                // System.out.println(tokenEmbeddings1[0][0][0]);
                // System.out.println(tokenEmbeddings1[0][1][0]);
                // System.out.println(tokenEmbeddings1[0][2][0]);
                // System.out.println(tokenEmbeddings1[0][3][0]);
            }

            //System.out.println("Token embeddings 1: " + Arrays.deepToString(tokenEmbeddings1));

            tokenizer.encode(sentences2);
            
            long[][] inputIds2 = tokenizer.getIds();
            long[][] attentionMask2 = tokenizer.getAttentionMask();
            long[][] typeIds2 = tokenizer.getTypeIds();

            OnnxTensor ids2 = OnnxTensor.createTensor(env, inputIds2);
            OnnxTensor masks2 = OnnxTensor.createTensor(env, attentionMask2);
            OnnxTensor types2 = OnnxTensor.createTensor(env, typeIds2);

            inputs = new HashMap<>();
            inputs.put("input_ids", ids2);
            inputs.put("attention_mask", masks2);
            inputs.put("token_type_ids", types2);

            float[][][] tokenEmbeddings2;

            try (OrtSession.Result results = session.run(inputs)) {
                OnnxValue lastHiddenState = results.get(0);
          
                tokenEmbeddings2  = (float[][][]) lastHiddenState.getValue();
          
                System.out.println(tokenEmbeddings1[0][0][0]);
                System.out.println(tokenEmbeddings1[0][1][0]);
                System.out.println(tokenEmbeddings1[0][2][0]);
                System.out.println(tokenEmbeddings1[0][3][0]);
            }

            // calculate similarity score of given sentences
            // for (int i = 0; i < sentences1.length; i++) {
            //     //double similarity = cosine_similarity(tokenEmbeddings1[i], tokenEmbeddings2[i][0]);
            //     CosineSimilarity cosineSimilarity = new CosineSimilarity(new BaseNDArrayFactory().create(tokenEmbeddings1[i]), new BaseNDArrayFactory.create(tokenEmbeddings1[i]));
                
            //     System.out.println(sentences1[i] + "\t" + sentences2[i] + "\t Similarity score: " + cosineSimilarity.currentResult());
            // }

            //double similarity = cosine_similarity(tokenEmbeddings1, tokenEmbeddings2);
            //System.out.println("Similarity score: " + similarity);

            // Result result = session.run(inputs);
            // OnnxValue value = result.get(0);
            // System.out.println("Output: " + value.getValue());

            // calculate cosine similarity score
            // double similarity = cosine_similarity((double[]) result.get(0).getValue(), (double[]) result.get(1).getValue());
            // System.out.println("Similarity score: " + similarity);
            //float[][] scores = (float[][]) value.getValue();

            // print scores
            // System.out.println("Scores:");
            // for (int i = 0; i < scores.length; i++) {
            //     System.out.println(Arrays.toString(scores[i]));
            // }
            

    
        } catch (OrtException | IOException e) {
            e.printStackTrace();
        }
        
    }

}