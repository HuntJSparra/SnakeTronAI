package ProjectThreeEngine;

public class SnakePiece implements GamePiece {
    int x;
    int y;
    int play_num;
    
    public int getX(){
	return x;
    }

    public int getY(){
	return y;
    }

    public int getNum(){
	return play_num;
    }

    public GamePiece copy(){
	return new SnakePiece(x, y, play_num);
    }
    
    public SnakePiece(int the_x, int the_y, int the_num){
	x = the_x;
	y = the_y;
	play_num = the_num;
    }
}
