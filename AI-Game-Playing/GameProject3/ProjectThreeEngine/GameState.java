package ProjectThreeEngine;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

// Note: Y = 0 Is the North (Top) part of the board
// Note: X = 0 is the West (Left) part of the board
// Board positions are null unless there is a Piece present

// This requires some significant changes to add a third player
// ... there are hard-coded 2's

public class GameState {
    List<FoodPiece> foods;
    Snake[] snakes;
 
    boolean game_over;
    int game_winner;

    String[] names;

    public final int max_x = 15;
    public final int max_y = 15;
    public final int start_len = 3;
    public final int max_food = 5;
    public final int num_players = 2;
    
    static public GameState concedeState(GameState old, int play_num){
	GameState state = new GameState(old);

	state.game_winner = 1 - play_num;
	
	state.game_over = true;
	
	return state;
    }

    public String getPlayName(int play_num){
	return names[play_num];
    }

    public int getNumPlayers(){
	return num_players;
    }
    
    public boolean isGameOver(){
	return game_over;
    }

    void makeGameOver(){
	game_over = true;
    }

    //Returns -1 if the game is not over
    public int getWinner(){
	return game_winner;
    }

    public GamePiece getPiece(int x, int y){
	for(FoodPiece f : foods){
	    if( f.getX() == x && f.getY() == y){
		return f;
	    }
	}
	for(int i = 0; i<num_players; i++){
	    if(snakes[i].head.getX() == x && snakes[i].head.getY() == y){
		return snakes[i].head;
	    }
	    for(SnakePiece p : snakes[i].body){
		if( p.getX() == x && p.getY() == y){
		    return p;
		}
	    }
	}
	return null;
    }

    public Snake getSnake(int play_num){
	return snakes[play_num];
    }

    public List<FoodPiece> getFoods(){
	return foods;
    }

    public boolean isFood(int x, int y){
	for( FoodPiece f: foods){
	    if( f.getX() == x && f.getY() == y){
		return true;
	    }
	}
	return false;
    }

    public void removeFood(int x, int y){
	List<FoodPiece> new_foods = new ArrayList<FoodPiece>();
	
	for( FoodPiece f: foods){
	    if( f.getX() != x || f.getY() != y){
		new_foods.add(f);
	    }
	}
	foods = new_foods;
	    
	while( foods.size() < max_food ){
	    addRandFood();
	}
    }
    
    void setSnake(int play_num, Snake sn){
	snakes[play_num] = sn;
    }

    void addFood(FoodPiece f){
	foods.add(f);
    }


    void addRandFood(){
	Random rand = new Random();
	int x,y;
	
	x = rand.nextInt(max_x);
	y = rand.nextInt(max_y);
	while( getPiece(x,y) != null){
	    x = rand.nextInt(max_x);
	    y = rand.nextInt(max_y);
	}
	foods.add(new FoodPiece(x, y));
    }
    
    public GameState(String name0, String name1){
	game_over = false;
	game_winner = -1;

	names = new String[2];
	names[0] = name0;
	names[1] = name1;

	snakes = new Snake[2];
	snakes[0] = new Snake(0, start_len, new HeadPiece(3, 0, 0, DirType.South));
	snakes[1] = new Snake(0, start_len, new HeadPiece(max_x - 4, max_y - 1, 1, DirType.North));	

	foods = new ArrayList<FoodPiece>();
	for(int i=0; i<max_food; i++){
	    addRandFood();
	}
       
	
    }
    
    //Deep Copy Constructor
    public GameState(GameState old){
	game_over = old.game_over;
	game_winner = old.game_winner;

	names = new String[2];
	names[0] = old.getPlayName(0);
	names[1] = old.getPlayName(1);

	snakes = new Snake[2];
	snakes[0] = new Snake( old.getSnake(0) );
	snakes[1] = new Snake( old.getSnake(1) );

	foods = new ArrayList<FoodPiece>();
	for(FoodPiece f : old.getFoods() ){
	    foods.add( (FoodPiece) f.copy() );
	}

    }
				
}
