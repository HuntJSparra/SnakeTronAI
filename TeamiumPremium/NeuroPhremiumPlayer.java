package TeamiumPremium;

import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.*;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 */
public class NeuroPhremiumPlayer implements Player {

    NeuralNetwork<BackPropagation> neuralNetwork;
   
    List<Integer> pastDecisions;
    List<double[]> pastStates;
    
    int my_num;
    
    public NeuroPhremiumPlayer() {
        List<Integer> neuronsInLayers = new ArrayList<>();
        neuronsInLayers.add(1536);
        neuronsInLayers.add(512);
        neuronsInLayers.add(4);

        /*NeuronProperties neuronProperties = new NeuronProperties();
        neuronProperties.setProperty("useBias", true);
        neuronProperties.setProperty("transferFunction", LeakyRectified.class);*/

        neuralNetwork = new MultiLayerPerceptron(neuronsInLayers, TransferFunctionType.TANH);
        neuralNetwork.getLearningRule().setMaxIterations(1);

        pastDecisions = new ArrayList<Integer>();
        pastStates = new ArrayList<double[]>();
    }
    
    @Override
    public void begin(GameState init_state, int play_num) {
        this.my_num = play_num;
    }

    @Override
    public DirType getMove(GameState state) {
        
        double[] input = NeuroSnakeUtilities.gameStateToNeuralInput(state, my_num);
        double[] prediction = predict(input);
        
        int index = -1;
        double currentHighest = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < prediction.length; i++) {
            if(prediction[i] > currentHighest) {
                index = i;
                currentHighest = prediction[i];
            }
        }
        DirType decision = DirType.values()[index];
        
        pastDecisions.add(index);
        pastStates.add(input);

        
        return decision;
    }
    
    private double[] predict(double[] input) {
        neuralNetwork.setInput(input);
        neuralNetwork.calculate();
        return neuralNetwork.getOutput();
    }
    
    public void finalScoreOfGame(double score) {
        
        DataSet trainingSet = new DataSet(1536, 4);

        if (pastDecisions.size() != pastStates.size()) {
            System.err.println("Past decisions and past states sizes are not equal before adjusting.");
        }
        
        for (int i = 0; i < pastDecisions.size(); i++) {
            Integer pastDecision = pastDecisions.get(i);
            double[] pastState = pastStates.get(i);
            double[] desiredOutput = predict(pastState);
            desiredOutput[pastDecision] = score;
            trainingSet.add(new DataSetRow(pastState, desiredOutput));
        }
        
        //neuralNetwork.getLearningRule().doOneLearningIteration(trainingSet);
        neuralNetwork.learn(trainingSet);
        
        this.pastDecisions.clear();
        this.pastStates.clear();
    }

    @Override
    public String getPlayName() {
        return "Neuro Phremium Player";
    }
    
}
