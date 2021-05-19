package TeamiumPremium;

import ProjectThreeEngine.*;
import java.util.*;

/**
 *
 * @author Jan Fic
 */
public class NeuroSnakeUtilities {
    static double[] gameStateToNeuralInput(GameState gameState, int playerNumber) {
        
        ArrayList<Double> dataRow = new ArrayList<>();
        
        // Optimize if needed

        // Input Size = 1536
        
        // 0 = empty
        // 1 = enemy body
        // 2 = our body
        // 3 = enemy head
        // 4 = our head
        // 5 = food
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                GamePiece piece = gameState.getPiece(x, y);
                int index = -1;
                
                if(piece == null) {
                    index = 0;
                }
                if(piece instanceof HeadPiece) {
                    if(((HeadPiece)piece).getNum() != playerNumber) {
                        index = 1;
                    }
                    else {
                        index = 2;
                    }
                }
                if(piece instanceof SnakePiece) {
                    if(((SnakePiece)piece).getNum() != playerNumber) {
                        index = 3;
                    }
                    else {
                        index = 4;
                    }
                }
                if(piece instanceof FoodPiece){
                    index = 5;
                }
                
                ArrayList<Double> encoding = new ArrayList<>(Collections.nCopies(6, 0.0));
                encoding.set(index, 1d);
                
                dataRow.addAll(encoding);
            }
        }
        double[] r = dataRow.stream().mapToDouble(i->i).toArray();
        return r;
    }
}
