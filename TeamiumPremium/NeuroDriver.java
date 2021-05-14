package TeamiumPremium;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import ProjectThreeEngine.*;

public class NeuroDriver {
    public static void main(String[] args) {
        Claustrophobium claustrophobium = new Claustrophobium();
        NeuroPhremiumPlayer neuroPhremiumPlayer = new NeuroPhremiumPlayer();

        int games = Integer.parseInt(args[0]);
        int turnLimit = Integer.parseInt(args[1]);

        while (games > 0) {

            GameState gameState = new GameState("Claustrophobium", "NeuroPhremiumPlayer");

            claustrophobium.begin(new GameState(gameState), 0);
            neuroPhremiumPlayer.begin(new GameState(gameState), 1);

            int turnNum = 0;
            
            int winner = -1;

            while (true) {
                gameState = nextTurn(gameState, claustrophobium, neuroPhremiumPlayer);
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

            int won = winner == 1 ? 1 : -1;
            double finalScoreOfGame = (won / turnNum);
            neuroPhremiumPlayer.finalScoreOfGame(finalScoreOfGame);

            System.out.println("Turns elapsed: " + turnNum);
            System.out.println("Winner: " + winner);
            System.out.println("Snake length: " + (gameState.getSnake(1).getBody().size() + 1));

            games--;
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
