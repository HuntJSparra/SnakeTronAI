import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

public class TestClass {

    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);
        
        // create training set
        DataSet trainingSet = new DataSet(2, 1);
        // add training data to training set (logical OR function)
        //trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainingSet.add(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainingSet.add(new DataSetRow(new double[]{1, 0}, new double[]{0}));
        trainingSet.add(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainingSet.add(new DataSetRow(new double[]{1, 1}, new double[]{0}));
        // learn the training set
        
        System.out.println("- Before Learn -");
        for (DataSetRow row : trainingSet.getRows()) {
            System.out.println("\tTarget: \t\t" + Arrays.toString(row.getDesiredOutput()));
            neuralNetwork.setInput(row.getInput());
            neuralNetwork.calculate();
            System.out.println("\tPredicted: \t\t" + Arrays.toString(neuralNetwork.getOutput()));
        }
        
        neuralNetwork.learn(trainingSet);
        // save the trained network into file
        neuralNetwork.save("or_percepton.nnet");
        
        System.out.println("- After Learn -");
        for (DataSetRow row : trainingSet.getRows()) {
            System.out.println("\tTarget: \t\t" + Arrays.toString(row.getDesiredOutput()));
            neuralNetwork.setInput(row.getInput());
            neuralNetwork.calculate();
            System.out.println("\tPredicted: \t\t" + Arrays.toString(neuralNetwork.getOutput()));
        }
    }
}
