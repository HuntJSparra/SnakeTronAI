package TeamiumPremium;

import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Player;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 */
public class NeuroPhremiumPlayer implements Player {

    NeuralNetwork neuralNetwork;
   
    List<Integer> pastDecisions;
    List<double[]> pastStates;
    
    int my_num;
    
    public NeuroPhremiumPlayer() {
        List<Integer> neuronsInLayers = new ArrayList<>();
        neuronsInLayers.add(1536);
        neuronsInLayers.add(512);
        neuronsInLayers.add(4);
        neuralNetwork = new MultiLayerPerceptron(neuronsInLayers, TransferFunctionType.RECTIFIED);
        pastDecisions = new LinkedList<>();
        pastStates = new LinkedList<>();
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
        
        
        
        
        neuralNetwork.learn(trainingSet);
    }

    @Override
    public String getPlayName() {
        return "Neuro Phremium Player";
    }
    
}
