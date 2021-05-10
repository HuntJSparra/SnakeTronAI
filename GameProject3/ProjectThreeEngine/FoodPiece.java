package ProjectThreeEngine;

public class FoodPiece implements GamePiece {
    int x;
    int y;
    
    public int getX(){
	return x;
    }

    public int getY(){
	return y;
    }
    
    public GamePiece copy(){
	return new FoodPiece(x, y);
    }
    
    public FoodPiece(int the_x, int the_y){
	x = the_x;
	y = the_y;
    }
}
