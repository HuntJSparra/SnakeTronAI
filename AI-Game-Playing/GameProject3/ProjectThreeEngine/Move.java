package ProjectThreeEngine;

public class Move{
    DirType dir;
    int play_num;
    
    public DirType getDir(){
	return dir;
    }

    public int getPlayNum(){
	return play_num;
    }
    
    public Move(int the_num, DirType the_dir){
	play_num = the_num;
	dir = the_dir;
    }
}
