package ProjectThreeEngine;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.shape.ArcType;
import java.util.List;

class GameDisplayGraphics{
    public static int SIZE_TALL = 840;
    public static int SIZE_WIDE = 1200;
    public static int MARGIN = 0;
    public static int BOX_TALL = 55;
    public static int BOX_WIDE = 55;
    public static double LINE_WIDE = 4;
    static int FONT_SIZE = 30;
    static GraphicsContext gc;


    static void displayState(Canvas can, GameState state){
	int x, y;
	
	gc = can.getGraphicsContext2D();

	gc.setStroke(Color.BLACK);
	gc.setLineWidth(LINE_WIDE);
	
	gc.clearRect(0,0,SIZE_WIDE,SIZE_TALL);

	//Player Zero Snake is BLUE
	Snake sn = state.getSnake(0);
	drawHead(sn.head.getX(), sn.head.getY(), sn.head.getDir(), Color.BLUE);
	for(SnakePiece p : sn.body){
	    drawBody(p.getX(), p.getY(), Color.BLUE);
	}

	//Player One Snake is RED
	sn = state.getSnake(1);
	drawHead(sn.head.getX(), sn.head.getY(), sn.head.getDir(), Color.RED);
	for(SnakePiece p : sn.body){
	    drawBody(p.getX(), p.getY(), Color.RED);
	}

	//Food is GREEN
	for(FoodPiece f : state.getFoods()){
	    drawFood(f.getX(), f.getY(), Color.GREEN);
	}

	gc.setStroke(Color.BLACK);
	gc.setLineWidth(LINE_WIDE/2);
	int bound = Math.min(SIZE_WIDE, SIZE_TALL);
	gc.strokeLine(0,0,bound,0);
	gc.strokeLine(bound,0, bound,bound);
	gc.strokeLine(bound, bound, 0, bound);
	gc.strokeLine(0, bound, 0, 0);
	
	gc.setFill(Color.BLUE);
	gc.setFont(Font.font("Helvetica", FONT_SIZE));
	gc.setLineWidth(1.5);
	gc.fillText(state.getPlayName(0) + " is BLUE", 875, 50);

	gc.setFill(Color.RED);
	gc.fillText(state.getPlayName(1) + " is RED", 875, 200);

	if(state.isGameOver()){
	    if(state.getWinner()==0){
		gc.setFill(Color.BLUE);
		gc.fillText(state.getPlayName(0) + " Wins!", 875, 350);
	    }
	    if(state.getWinner()==1){
		gc.setFill(Color.RED);
		gc.fillText(state.getPlayName(1) + " Wins!", 875, 350);
	    }
	}
    }

    static void drawFood(int x, int y, Color col){
	gc.setFill(col);
	gc.fillOval(x*BOX_WIDE, y*BOX_TALL, BOX_WIDE, BOX_TALL);
    }
    
    static void drawBody(int x, int y, Color col){
	gc.setFill(col);
	gc.fillRect(x*BOX_WIDE, y*BOX_TALL, BOX_WIDE, BOX_TALL);
	gc.setStroke(Color.BLACK);
	gc.strokeRect(x*BOX_WIDE, y*BOX_TALL, BOX_WIDE, BOX_TALL);
    }

    static void drawHead(int x, int y, DirType dir, Color col){
	gc.setFill(Color.BLACK);
	gc.fillRect(x*BOX_WIDE, y*BOX_TALL, BOX_WIDE, BOX_TALL);
	gc.setStroke(col);
	gc.strokeRect(x*BOX_WIDE, y*BOX_TALL, BOX_WIDE, BOX_TALL);

	x = x*BOX_WIDE + BOX_WIDE/2;
	y = y*BOX_TALL + BOX_TALL/2;
	if(dir ==DirType.North){
	    gc.strokeLine(x,y, x , y - BOX_TALL/2);
	}
	if(dir ==DirType.South){
	    gc.strokeLine(x,y, x , y + BOX_TALL/2);
	}
	if(dir ==DirType.West){
	    gc.strokeLine(x,y, x - BOX_WIDE/2 , y);
	}
	if(dir ==DirType.East){
	    gc.strokeLine(x,y, x + BOX_WIDE/2 , y);
	}
	
    }
	
}
