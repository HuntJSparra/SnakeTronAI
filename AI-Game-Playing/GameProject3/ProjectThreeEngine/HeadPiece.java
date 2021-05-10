package ProjectThreeEngine;

public class HeadPiece implements GamePiece {
    int x;
    int y;
    int play_num;
    DirType dir;
    
    public int getX(){
	return x;
    }

    public int getY(){
	return y;
    }

    public int getNum(){
	return play_num;
    }

    public DirType getDir(){
	return dir;
    }

    public GamePiece copy(){
	return new HeadPiece(x, y, play_num, dir);
    }
    
    public HeadPiece(int the_x, int the_y, int the_num, DirType the_dir){
	x = the_x;
	y = the_y;
	play_num = the_num;
	dir = the_dir;
    }
}
