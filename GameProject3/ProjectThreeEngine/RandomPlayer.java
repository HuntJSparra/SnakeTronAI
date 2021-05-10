package ProjectThreeEngine;

import java.util.Random;

public class RandomPlayer implements Player{
    int my_num;
    
    //This function is called when the game starts
    public void begin(GameState init_state, int play_num){
	my_num = play_num;
    }

    //You can return null to just keep going straight
    public DirType getMove(GameState state){	
	Random rand = new Random();
	DirType cur_dir = state.getSnake(my_num).head.getDir();
	int r = rand.nextInt(6);
	if( cur_dir == DirType.North || cur_dir == DirType.South ){
	    if( r == 0){
		return DirType.East;
	    }
	    if( r == 1){
		return DirType.West;
	    }
	    return null;
	}
	if( cur_dir == DirType.East || cur_dir == DirType.West ){
	    if( r == 0){
		return DirType.North;
	    }
	    if( r == 1){
		return DirType.South;
	    }
	    return null;
	}
	
	return null; 
    }

	

    public String getPlayName(){
	return "Random Player";
    }
}

   
