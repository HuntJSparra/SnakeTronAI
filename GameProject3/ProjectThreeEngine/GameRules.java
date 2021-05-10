package ProjectThreeEngine;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GameRules{

    public static GameState makeMoves(GameState state, List<Move> moves){
	GameState new_state = new GameState(state);

	if(new_state.isGameOver()){
	    return new_state;
	}
	
	for(Move mv : moves){
	    int play_num = mv.getPlayNum();
	    DirType new_dir = mv.getDir();
	    new_state.getSnake(play_num).turnHead(new_dir);
	}

	for(int i=0; i< new_state.getNumPlayers(); i++){
	    Snake sn = new_state.getSnake(i);
	    sn.move();
	    if( new_state.isFood( sn.head.getX(), sn.head.getY() )){
		sn.incMaxLen();
		new_state.removeFood( sn.head.getX(), sn.head.getY() );
	    }
	}

	new_state = handleCollide(new_state);
	    
	return new_state;
    }

    //Winner is Random if both collide
    static GameState handleCollide(GameState state){
	int num_collide = 0;

	for(int i=0; i< state.getNumPlayers(); i++){
	    boolean collide = false;
	    Snake sn_i = state.getSnake(i);
	    for(int j=0; j < state.getNumPlayers(); j++){
		Snake sn_j = state.getSnake(j);

		if( sn_j.isPresent( sn_i.head.getX(), sn_i.head.getY()) ){
		    if( ! (i == j) ){
			collide = true;
		    }

		}
	    }


	    // Checks I's body to see if it ran itself over
	    for (SnakePiece b : sn_i.body) {
		if (b.getX() == sn_i.head.getX() && b.getY() == sn_i.head.getY()) {
		    collide = true;
		    state.game_over = true;
		    state.game_winner = 1-i;
		}
	    }
	    
	    if( sn_i.head.getX() < 0 || sn_i.head.getX() == state.max_x){
		collide = true;
	    }
	    if( sn_i.head.getY() < 0 || sn_i.head.getY() == state.max_y){
		collide = true;
	    }
	    if(collide){
		num_collide = num_collide + 1;
		state.game_over = true;
		state.game_winner = 1-i;
	    }
	}

	if(num_collide > 1){
	    Random rand = new Random();
	    state.game_winner = rand.nextInt(2);
	}

	return state;
    }
}
