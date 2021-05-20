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
import java.io.File;

import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.conf.*;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.primitives.Pair;
import org.nd4j.linalg.api.ndarray.*;
import org.deeplearning4j.datasets.iterator.*;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.deeplearning4j.nn.conf.inputs.InputType;

public class ConvolutiumPlayer implements Player {

    MultiLayerNetwork nn;

    int my_num;
    int gameLimit;
    double chanceOfRandomMove;

    int lastAction;
    double[] lastState;
    CircularArrayList<Observation> obs;
    int replayBufferSize = 2500;

    Claustrophobium claustrophobium;

    private class Observation {

        public double[] state;
        public int moveChosen;
        public double reward;
        public GameState nextState;
        public boolean done;

        public Observation(double[] state, int moveChosen, double reward, GameState nextState, boolean done) {
            this.state = state;
            this.moveChosen = moveChosen;
            this.reward = reward;
            this.nextState = nextState;
            this.done = done;
        }
    }

    public ConvolutiumPlayer(int gameLimit) {
        // Memory
        this.gameLimit = gameLimit;
        obs = new CircularArrayList<Observation>();
        chanceOfRandomMove = 1.0;
        claustrophobium = new Claustrophobium();

        // DL4J
        int numOut = 4;

        int spaceEncodingSize = 6;
        int kernelSize = 6;

        // .backprop is deprecated?
        // new ConvolutionLayer.Builder(Array[Int](7,7), Array[Int](2,2), Array[Int](3,3))
                //.nIn(inputShape(0))
                //.nOut(64)
                //.cudnnAlgoMode(ConvolutionLayer.AlgoMode.NO_WORKSPACE)
        this.nn = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                //.seed(12345)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(0.00025))
                .list(
                        new ConvolutionLayer.Builder().kernelSize(kernelSize, kernelSize).stride(1, 1).nOut(10).activation(Activation.IDENTITY).build(),
                        new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.AVG).kernelSize(2, 2).stride(2, 2).build(),
                        new DenseLayer.Builder().nIn(250).nOut(4).activation(Activation.LEAKYRELU).build(),
                        new OutputLayer.Builder(LossFunction.MSE).activation(Activation.SOFTMAX).nIn(4).nOut(numOut).build()
                )
                .setInputType(InputType.convolutionalFlat(16, 16, spaceEncodingSize))
                .build());

        this.nn.init();
    }

    // public DeepQiumPlayer(String filePath) {
    //     // If you try to train it... bad stuff will happen (other variables aren't initialized)
    //     try {
    //         this.nn = MultiLayerNetwork.load(new File(filePath), false);
    //         this.chanceOfRandomMove = 0.0;
    //         this.gameLimit = 0;
    //         this.claustrophobium = new Claustrophobium();
    //     } catch (Exception e) {
    //         System.err.println("Error loading network:");
    //         e.printStackTrace();
    //     }
    // }

    @Override
    public void begin(GameState init_state, int play_num) {
        this.my_num = play_num;
        claustrophobium.begin(init_state, play_num);
    }

    public INDArray predict(double[] input) {
        return this.nn.output(new NDArray(new double[][]{input})).getRow(0);
    }

    public INDArray mask(INDArray values, GameState state) {
        ArrayList<Integer> badDirections = new ArrayList<Integer>();
        DirType[] possibleDirections = DirType.values();
        for (int i=0; i<possibleDirections.length; i++) {
            DirType dir = possibleDirections[i];
            int x = state.getSnake(this.my_num).head.getX();
            int y = state.getSnake(this.my_num).head.getY();
            switch(dir) {
                case North:
                    y--;
                    break;
                case East:
                    x++;
                    break;
                case South:
                    y++;
                    break;
                case West:
                    x--;
                    break;
            }
            if (isPositionDeath(state, x, y)) badDirections.add(i);
        }

        for (int dt : badDirections) {
            values.put(0, dt, Double.NEGATIVE_INFINITY);
        }

        return values;
    }

    @Override
    public DirType getMove(GameState state) {
        double[] input = NeuroSnakeUtilities.gameStateToNeuralInput(state, my_num);
        int index = -1;
        if (Math.random() >= chanceOfRandomMove) {
            INDArray prediction = mask(predict(input), state);
            index = prediction.argMax(1).getInt(0);
        } else {
            // System.out.println("Chance of Randomness: " + chanceOfRandomMove);
            DirType d = claustrophobium.getMove(state);
            for (int i = 0; i < DirType.values().length; i++) {
                if (d == DirType.values()[i]) {
                    index = i;
                    break;
                }
            }
            // index = (int)(Math.random() * 4);
        }
        // System.out.println(prediction);

        DirType decision = DirType.values()[index];

        this.lastAction = index;
        this.lastState = input;
        return decision;
    }

    public void scoreTurn(GameState nextState, double score, boolean done) {
        Observation o = new Observation(lastState, lastAction, score, nextState, done);
        obs.addFirst(o);
        if(obs.size() >= replayBufferSize) obs.removeLast();
        lastState = null;
        lastAction = -1;
    }

    public void updateNetwork() {
        int batchSize = Math.min(obs.size(), 500);
        //if(obs.size() < batchSize) return;
        // Not going to execute if obs not large enough
        
        
        // WE ARE HERE
        // Add Random Sampling with Deque ( or other structure )
        
        // Sample
        List<Integer> indicies = new ArrayList<>();
        for(int i = 0; i < obs.size(); i++) {
            indicies.add(i);
        }
        Collections.shuffle(indicies);
        
        // Create dataset
        List<Pair<double[], double[]>> trainingSet = new ArrayList<>();
        for(int i = 0; i < batchSize; i++) {
            Observation observation = this.obs.get(indicies.get(i));
            Integer pastDecision = observation.moveChosen;
            double[] pastState = observation.state;
            double[] desiredOutput = predict(pastState).toDoubleVector();
            double[] nextState = NeuroSnakeUtilities.gameStateToNeuralInput(observation.nextState, my_num);
            double score = observation.done ? 0.0 : mask(predict(nextState), observation.nextState).maxNumber().doubleValue();
            desiredOutput[pastDecision] += 0.95*score;
            trainingSet.add(Pair.create(pastState, desiredOutput));
        }

        // Train
        this.nn.fit(new DoublesDataSetIterator(trainingSet, trainingSet.size()));
        this.chanceOfRandomMove = Math.max(0.01, this.chanceOfRandomMove*0.9995);      
    }

    public void endGame() {
        // this.chanceOfRandomMove = Math.max(0.01, this.chanceOfRandomMove*0.995);
        //this.chanceOfRandomMove -= 1f / gameLimit;
    }
    
    // public void finalScoreOfGame(double score) {

    //     List<Pair<double[], double[]>> trainingSet = new ArrayList<>();

    //     for (Observation obs : this.obs) {
    //         Integer pastDecision = obs.moveChosen;
    //         // System.out.println(DirType.values()[pastDecision]);
    //         double[] pastState = obs.state;
    //         double[] desiredOutput = predict(pastState).toDoubleVector();
    //         desiredOutput[pastDecision] = score;
    //         // System.out.println(Arrays.toString(desiredOutput));
    //         trainingSet.add(Pair.create(pastState, desiredOutput));
    //     }

    //     //System.out.println(Arrays.toString(predict(obs.get(0).state).toDoubleVector()));
    //     this.nn.fit(new DoublesDataSetIterator(trainingSet, trainingSet.size()));
    //     //System.out.println(Arrays.toString(predict(obs.get(0).state).toDoubleVector()));

    //     this.obs.clear();
    //     this.chanceOfRandomMove -= 1f / gameLimit;
    // }

    public double getChanceOfRandomMove() {
        return chanceOfRandomMove;
    }

    @Override
    public String getPlayName() {
        return "ConvolutiumPlayer";
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

    public void save(String path) {
        try {
            this.nn.save(new File(path));
        } catch (Exception e) {
            System.err.println("Unable to save network:");
            e.printStackTrace();
        }
    }
}
