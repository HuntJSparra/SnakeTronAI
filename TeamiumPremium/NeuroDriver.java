package TeamiumPremium;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javafx.application.Application;

import ProjectThreeEngine.*;

public class NeuroDriver {
    
    public static void main(String[] args) throws Exception {
        int wins = 0;
        int games = 0;
        int gameLimit = 500;//Integer.parseInt(args[0]);
        int turnLimit = 500;//Integer.parseInt(args[1]);

        Claustrophobium claustrophobium = new Claustrophobium();
        // DeepQiumPlayer premiumAI = new DeepQiumPlayer(gameLimit);
        ConvolutiumPlayer premiumAI = new ConvolutiumPlayer(gameLimit);

        
        while (games < gameLimit) {

            GameState gameState = new GameState("Claustrophobium", "ConvolutiumPlayer");
            // GameState gameState = new GameState("Claustrophobium", "DeepQium");

            claustrophobium.begin(new GameState(gameState), 0);
            premiumAI.begin(new GameState(gameState), 1);

            int turnNum = 0;
           
            int winner = -1;

            while (true) {
                GameState oldState = gameState;

                gameState = nextTurn(gameState, claustrophobium, premiumAI);
                turnNum++;
                
                // Scoring
                GameState newState = gameState;

                int bodiesGanied = newState.getSnake(1).getBody().size() - oldState.getSnake(1).getBody().size(); // aka food eaten
                int winningPoints = (gameState.getWinner() == 1) ? 1000 : 0;
                if (winningPoints > 0) System.out.println("Winner!");

                boolean done = (turnNum >= turnLimit) || gameState.isGameOver();

                premiumAI.scoreTurn(gameState, winningPoints + 10*bodiesGanied, done);
                premiumAI.updateNetwork();

                
                //If we hit the turn limit, the longer snake wins
                if (turnNum >= turnLimit) {
                    int longestSnakeOrRandom = getLongestSnakeOrRandom(gameState);
                    winner = longestSnakeOrRandom;
                    break;
                }

                if (gameState.isGameOver()) {
                    winner = gameState.getWinner();
                    break;
                }
            }
            
            premiumAI.endGame();

            wins += winner == 1 ? 1 : 0;
            double won = winner == 1 ? 1 : -1;
            double finalScoreOfGame = turnNum;
            // double finalScoreOfGame = gameState.getSnake(1).getBody().size() / 100.0;
            //premiumAI.finalScoreOfGame(finalScoreOfGame);

            System.out.println("GAME #" + games);
            System.out.println("\tTurns elapsed: " + turnNum);
            System.out.println("\tRandomness: "+ premiumAI.getChanceOfRandomMove());
            System.out.println("\tWinner: " + winner);
            System.out.println("\tSnake length: " + (gameState.getSnake(1).getBody().size() + 1));
            System.out.println("\tOpponent length: " + (gameState.getSnake(0).getBody().size() + 1));
            // System.out.println("\tScore: "+finalScoreOfGame);

            System.out.println("\n\n\n");

            games++;
        }
        System.out.println("WINS: " + wins);

        // premiumAI.save("trainedNetwork.premium");
        premiumAI.save("trainedConvolution.premium");

        // Application.launch(AIGameApp.class, new String[]{});
    }

    static GameState nextTurn(GameState state, Player p0, Player p1) {
        List<Move> moves = new ArrayList<Move>();
        
        if (!state.isGameOver()) {
            DirType new_dir;
    
            new_dir = p0.getMove(new GameState(state));

            if(new_dir != null) {
                moves.add(new Move(0, new_dir));
            }
    
            new_dir = p1.getMove(new GameState(state));

            if(new_dir != null) {
                moves.add(new Move(1, new_dir));
            }
            
            state = GameRules.makeMoves(state, moves);
        }
        return state;
    }

    static int getLongestSnakeOrRandom(GameState state) {
        int sn0_len = state.getSnake(0).max_len;
        int sn1_len = state.getSnake(1).max_len;
        if (sn0_len > sn1_len) {
            return 0;
        }
        if (sn1_len > sn0_len) {
            return 1;
        }
        Random rand = new Random();
        int x = rand.nextInt(2);
        return x;
    }
}
