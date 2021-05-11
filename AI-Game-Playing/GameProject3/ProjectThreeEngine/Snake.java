package ProjectThreeEngine;

import java.util.List;
import java.util.ArrayList;

public class Snake {
    public int play_num;
    public List<SnakePiece> body;
    public HeadPiece head;
    public int max_len;

    public HeadPiece getHead(){
	return head;
    }

    public List<SnakePiece> getBody(){
	return body;
    }
    
    public boolean isPresent(int x, int y){
	boolean found = false;

	if((head.getX() == x) && (head.getY() == y)){
	    found = true;
	}
	
	for(SnakePiece piece: body){
	    if( piece.getX() == x && piece.getY() == y){
		found = true;
	    }
	}
	return found;
    }

    public void incMaxLen(){
	max_len = max_len + 1;
    }
    
    public void move(){
	int x = -1;
	int y = -1;
	DirType cur_dir = head.getDir();
	
	if(cur_dir == DirType.North){
	    x = head.getX();
	    y = head.getY() - 1;
	}
	if(cur_dir == DirType.South){
	    x = head.getX();
	    y = head.getY() + 1;
	}
	if(cur_dir == DirType.East){
	    x = head.getX() + 1;
	    y = head.getY();
	}
	if(cur_dir == DirType.West){
	    x = head.getX() - 1;
	    y = head.getY();
	}

	if (body.size() == max_len){
	    body.remove(0);
	}

	body.add(new SnakePiece(head.getX(), head.getY(), play_num));

	head = new HeadPiece(x,y, play_num, cur_dir);
	    
    }

    public void turnHead(DirType new_dir){
	int x = head.getX();
	int y = head.getY();
	head = new HeadPiece(x, y, play_num, new_dir);
    }
    
    public Snake(int the_num, int the_len, HeadPiece the_head){
        head = the_head;
	body = new ArrayList<SnakePiece>();
	play_num = the_num;
	max_len = the_len;
    }

    public Snake(Snake sn){
	head = (HeadPiece) sn.head.copy();
	body = new ArrayList<SnakePiece>();
	for(SnakePiece piece: sn.body){
	    body.add( (SnakePiece) piece.copy() );
	}
	max_len = sn.max_len;
    }
}
