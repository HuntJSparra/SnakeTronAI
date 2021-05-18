package TeamiumPremium;

import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.conf.*;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.AdaGrad;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.primitives.Pair;
import org.nd4j.linalg.api.ndarray.*;
import org.deeplearning4j.datasets.iterator.*;
import org.nd4j.linalg.cpu.nativecpu.NDArray;

// TODO: Learning rate?
public class DeepQiumPlayer implements Player {
    MultiLayerNetwork nn;

    int my_num;

    List<Observation> obs;
    private class Observation {
        public double[] state;
        public int moveChosen;
        // public double reward;
        // public double[] nextState;

        public Observation(double[] state, int moveChosen) {
            this.state = state;
            this.moveChosen = moveChosen;
        }
    }
    
    public DeepQiumPlayer() {
        // Memory
        obs = new ArrayList<Observation>();

        // DL4J
        int numIn  = 1536;
        int numHid = 512;
        int numOut = 4;

        // .backprop is deprecated?
        this.nn = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
            .seed(12345)
            .weightInit(WeightInit.ONES)
            .updater(new AdaGrad(1e-6))
            .list(
                new DenseLayer.Builder().nIn(numIn).nOut(numHid).activation(Activation.LEAKYRELU).build(),
                new OutputLayer.Builder(LossFunction.MSE).activation(Activation.SOFTMAX).nIn(numHid).nOut(numOut).build()
            ).build());

        this.nn.init();
    }
    
    @Override
    public void begin(GameState init_state, int play_num) {
        this.my_num = play_num;
    }

    public INDArray predict(double[] input) {
        return this.nn.output( new NDArray(new double[][]{ input })).getRow(0);
    }

    @Override
    public DirType getMove(GameState state) {
        double[] input = NeuroSnakeUtilities.gameStateToNeuralInput(state, my_num);
        INDArray prediction = predict(input);
        // System.out.println(prediction);
        
        int index = prediction.argMax(1).getInt(0);
        DirType decision = DirType.values()[ index ];
        
        obs.add( new Observation(input, index) );
        return decision;
    }

    public void finalScoreOfGame(double score) {
        
        List<Pair<double[], double[]>> trainingSet = new ArrayList<Pair<double[], double[]>>();
        
        for (Observation obs : this.obs) {
            Integer pastDecision = obs.moveChosen;
            // System.out.println(DirType.values()[pastDecision]);
            double[] pastState = obs.state;
            double[] desiredOutput = predict(pastState).toDoubleVector();
            desiredOutput[pastDecision] = score;
            // System.out.println(Arrays.toString(desiredOutput));
            trainingSet.add(Pair.create(pastState, desiredOutput));
        }
        
        System.out.println(Arrays.toString(predict(obs.get(0).state).toDoubleVector()));
        this.nn.fit(new DoublesDataSetIterator(trainingSet, trainingSet.size()));
        System.out.println(Arrays.toString(predict(obs.get(0).state).toDoubleVector()));
        
        this.obs.clear();
    }

    @Override
    public String getPlayName() {
        return "DeepQiumPlayer";
    }
}
