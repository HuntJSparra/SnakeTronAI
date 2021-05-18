package TeamiumPremium;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import ProjectThreeEngine.*;

public class NeuroDriver {
    public static void main(String[] args) {
        Claustrophobium claustrophobium = new Claustrophobium();
        DeepQiumPlayer premiumAI = new DeepQiumPlayer();

        int games = 0;
        int gameLimit = Integer.parseInt(args[0]);
        int turnLimit = Integer.parseInt(args[1]);

        while (games < gameLimit) {

            GameState gameState = new GameState("Claustrophobium", "DeepQiumPlayer");

            claustrophobium.begin(new GameState(gameState), 0);
            premiumAI.begin(new GameState(gameState), 1);

            int turnNum = 0;
            
            int winner = -1;

            while (true) {
                gameState = nextTurn(gameState, claustrophobium, premiumAI);
                turnNum++;

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

            double won = winner == 1 ? 1 : -1;
            double finalScoreOfGame = ((won / turnNum) + 1) / 2;
            // double finalScoreOfGame = gameState.getSnake(1).getBody().size() / 100.0;
            premiumAI.finalScoreOfGame(finalScoreOfGame);

            System.out.println("GAME #" + games);
            System.out.println("\tTurns elapsed: " + turnNum);
            System.out.println("\tWinner: " + winner);
            System.out.println("\tSnake length: " + (gameState.getSnake(1).getBody().size() + 1));
            System.out.println("\tScore: "+finalScoreOfGame);

            System.out.println("\n\n\n");

            games++;
        }
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
