// To play with your AI player, you need to do two things:
// 1) Add an import statement to import your AI player
// 2) Change the lines inside start that say:
//      Player_0 =
//      Player_1 =

package ProjectThreeEngine;

import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class AIGameText{
    public static final int MAX_TURN = 500;
    public static int turn_num;
    
    static Player Player_0;
    static Player Player_1;
    
    static GameState state;
    static GameStateWriter gs_writer;

    public static void main(String[] args) throws IOException{
	run_game("transcript.txt");
    }	


    public static void run_game(String filename) throws IOException{
	List<GameState> the_states = new ArrayList<GameState>();
	gs_writer = new GameStateWriter(filename);
	turn_num = 0;
	
	//IMPORTANT : Change these lines to change who is playing!
	Player_0 = new RandomPlayer();
	Player_1 =  new RandomPlayer();


	//Set up the names in the state object
	String name_0;
	String name_1;
	name_0 = Player_0.getPlayName();
	name_1 = Player_1.getPlayName();


	state = new GameState(name_0, name_1 );
	Player_0.begin(new GameState(state), 0 );
	Player_1.begin(new GameState(state), 1 );

	the_states.add(state);

	while( !state.isGameOver() ){
	    state = nextTurn();
	    turn_num = turn_num + 1;

	    //If we hit the turn limit, the longer snake wins
	    if (turn_num >= MAX_TURN){
		int sn0_len = state.getSnake(0).max_len;
		int sn1_len = state.getSnake(1).max_len;
		state.makeGameOver();
		if(sn0_len > sn1_len){
		    state.game_winner = 0;
		}
		if(sn1_len > sn0_len){
		    state.game_winner = 1;
		}
		if(sn0_len == sn1_len){
		    Random rand = new Random();
		    int x = rand.nextInt(2);
		    state.game_winner = x;
		}
	    }
	    
	    the_states.add(state);
	}

	gs_writer.WriteGame(the_states);
	
    }


    static GameState nextTurn(){
	List<Move> moves = new ArrayList<Move>();
	
	if (! state.isGameOver() ){
	    DirType new_dir;

	    new_dir = Player_0.getMove( new GameState(state) );
	    if(new_dir != null){
		moves.add(new Move(0, new_dir));
	    }

	    new_dir = Player_1.getMove( new GameState(state) );
	    if(new_dir != null){
		moves.add(new Move(1, new_dir));
	    }
	    
	    state = GameRules.makeMoves(state, moves);
	}
	return state;
    }
}

	


	
