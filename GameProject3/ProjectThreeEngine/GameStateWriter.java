package ProjectThreeEngine;

import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameStateWriter {

    String filename;
    BufferedWriter file_out;

    public GameStateWriter(String name) throws IOException{
	filename = name;
	file_out = new BufferedWriter(new FileWriter( filename));
    }
    
    //This assumes that the final (winning/losing state is last in the list)
    public void WriteGame(List<GameState> states) throws IOException{

	GameState end_state = states.get( states.size() - 1);

	if( ! end_state.isGameOver() ){
	    System.out.println("ERROR: GAME NOT OVER");
	}
	else{
	    String name0 = end_state.getPlayName(0);
	    String name1 = end_state.getPlayName(1);
	    int winner = end_state.getWinner();

	    file_out.write(name0 + " : Player 0");
	    file_out.newLine();
	    file_out.write(name1 + " : Player 1");
	    file_out.newLine();
	    file_out.write(String.valueOf(winner) + " : Winner");
	    file_out.newLine();
	    file_out.write(String.valueOf(end_state.max_x + ", "));
	    file_out.write(String.valueOf(end_state.max_y + ", "));
	    file_out.write(String.valueOf(end_state.start_len + ", "));
	    file_out.write(String.valueOf(end_state.max_food));
	    file_out.newLine();
	}

	for(GameState s: states){
	    file_out.write("----");
	    file_out.newLine();
	    writeSnake(s.getSnake(0), 0);
	    writeSnake(s.getSnake(1), 1);
	    writeFood(s.getFoods());
	}

	file_out.close();
    }

    void writePiece(GamePiece piece) throws IOException{
	int x = piece.getX();
	int y = piece.getY();
	file_out.write("( "+String.valueOf(x) + " , " + String.valueOf(y) + " )");
	file_out.newLine();
    }
    
    public void writeSnake( Snake sn, int play_num) throws IOException{
	file_out.write("Snake " + String.valueOf(play_num) + " : ");
	file_out.newLine();
	HeadPiece h = sn.head;
	file_out.write(h.getDir().name());
	file_out.newLine();
	writePiece(h);
	for(GamePiece p : sn.body){
	    writePiece(p);
	}
    }

    public void writeFood( List<FoodPiece> foods ) throws IOException{
	file_out.write("Foods : ");
	file_out.newLine();
	for(GamePiece f : foods){
	    writePiece(f);
	}
    }
	
}	
	

	  
