package TeamiumPremium;

import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Player;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

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
// + Replay Buffer
//      List Of Fixed Length (Deque or Queue)
//      Random Samples from it
// + New Target Value
//      Reward + Predicted Value of Best Action for next State
public class DeepQiumPlayer implements Player {

    MultiLayerNetwork nn;

    int my_num;
    int gameLimit;
    double chanceOfRandomMove;

    int lastAction;
    double[] lastState;
    ArrayDeque<Observation> obs;
    int replayBufferSize = 1000;

    Claustrophobium claustrophobium;

    private class Observation {

        public double[] state;
        public int moveChosen;
        public double reward;
        public double[] nextState;

        public Observation(double[] state, int moveChosen, double reward, double[] nextState) {
            this.state = state;
            this.moveChosen = moveChosen;
            this.reward = reward;
            this.nextState = nextState;
        }
    }

    public DeepQiumPlayer(int gameLimit) {
        // Memory
        this.gameLimit = gameLimit;
        obs = new ArrayDeque<>();
        chanceOfRandomMove = 1.0;
        claustrophobium = new Claustrophobium();

        // DL4J
        int numIn = 1536;
        int numHid1 = 512;
        int numHid2 = 256;
        int numOut = 4;

        // .backprop is deprecated?
        this.nn = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                //.seed(12345)
                .weightInit(WeightInit.XAVIER)
                .updater(new AdaGrad(1e-4))
                .list(
                        new DenseLayer.Builder().nIn(numIn).nOut(numHid2).activation(Activation.LEAKYRELU).build(),
                        //new DenseLayer.Builder().nIn(numHid1).nOut(numHid2).activation(Activation.LEAKYRELU).build(),
                        new OutputLayer.Builder(LossFunction.MSE).activation(Activation.LEAKYRELU).nIn(numHid2).nOut(numOut).build()
                ).build());

        this.nn.init();
    }

    @Override
    public void begin(GameState init_state, int play_num) {
        this.my_num = play_num;
        claustrophobium.begin(init_state, play_num);
    }

    public INDArray predict(double[] input) {
        return this.nn.output(new NDArray(new double[][]{input})).getRow(0);
    }

    @Override
    public DirType getMove(GameState state) {
        double[] input = NeuroSnakeUtilities.gameStateToNeuralInput(state, my_num);
        int index = -1;
        if (Math.random() >= chanceOfRandomMove) {
            INDArray prediction = predict(input);
            index = prediction.argMax(1).getInt(0);
        } else {
            //System.out.println("Chance of Randomness: " + chanceOfRandomMove);
            DirType d = claustrophobium.getMove(state);
            for (int i = 0; i < DirType.values().length; i++) {
                if (d == DirType.values()[i]) {
                    index = i;
                    break;
                }
            }
        }
        // System.out.println(prediction);

        DirType decision = DirType.values()[index];

        this.lastAction = index;
        this.lastState = input;
        return decision;
    }

    public void scoreTurn(GameState nextState, double score) {
        Observation o = new Observation(lastState, lastAction, score, NeuroSnakeUtilities.gameStateToNeuralInput(nextState, my_num));
        obs.addFirst(o);
        if(obs.size() >= replayBufferSize) obs.removeLast();
        lastState = null;
        lastAction = -1;
    }

    public void updateNetwork() {
        int batchSize = 32;
        if(obs.size() < batchSize) return;
        // Not going to execute if obs not large enough
        
        
        // WE ARE HERE
        // Add Random Sampling with Deque ( or other structure )
        
        // Sample
        List<Integer> indicies = new ArrayList<>();
        for(int i = 0; i < obs.size(); i++) {
            indicies.add(i);
        }
        Collections.shuffle(indicies);
        
        for(int i = 0; i < batchSize; i++) {

        }
        
        
        List<Pair<double[], double[]>> trainingSet = new ArrayList<>();
        for (Observation obs : this.obs) {
            double score = 0;
            Integer pastDecision = obs.moveChosen;
            double[] pastState = obs.state;
            double[] desiredOutput = predict(pastState).toDoubleVector();
            score = predict(obs.nextState).maxNumber().doubleValue();
            desiredOutput[pastDecision] = score;
            trainingSet.add(Pair.create(pastState, desiredOutput));
        }

        this.nn.fit(new DoublesDataSetIterator(trainingSet, trainingSet.size()));        
    }
    
    public void finalScoreOfGame(double score) {

        List<Pair<double[], double[]>> trainingSet = new ArrayList<>();

        for (Observation obs : this.obs) {
            Integer pastDecision = obs.moveChosen;
            // System.out.println(DirType.values()[pastDecision]);
            double[] pastState = obs.state;
            double[] desiredOutput = predict(pastState).toDoubleVector();
            desiredOutput[pastDecision] = score;
            // System.out.println(Arrays.toString(desiredOutput));
            trainingSet.add(Pair.create(pastState, desiredOutput));
        }

        //System.out.println(Arrays.toString(predict(obs.get(0).state).toDoubleVector()));
        this.nn.fit(new DoublesDataSetIterator(trainingSet, trainingSet.size()));
        //System.out.println(Arrays.toString(predict(obs.get(0).state).toDoubleVector()));

        this.obs.clear();
        this.chanceOfRandomMove -= 1f / gameLimit;
    }

    @Override
    public String getPlayName() {
        return "DeepQiumPlayer";
    }

    private boolean isPositionDeath(GameState state, int x, int y) {
        if (state.getPiece(x, y) == null) {
            return !isPieceInBounds(x, y);
        }
        return true;
    }

    private boolean isPieceInBounds(int x, int y) {
        return ((x < 15 && x >= 0)
                && (y < 15 && y >= 0));
    }
}
