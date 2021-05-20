// To play with your AI player, you need to do two things:
// 1) Add an import statement to import your AI player
// 2) Change the lines inside start that say:
//      Player_0 =
//      Player_1 =

package ProjectThreeEngine;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import TeamiumPremium.Claustrophobium;
import TeamiumPremium.DeepQiumPlayer;

public class AIGameApp extends Application{
    public static final int MAX_TURN = 500;
    public static int turn_num;
    
    Player Player_0;
	Player Player_1;
	
	String name_0, name_1;
    
    Canvas test_canvas;
    Stage primary;
    GameState state;
    
    // You can raise this delay to slow down the AI moves
    final double DELAY_TIME = 0.75;

    public void start(Stage primaryStage){
		//IMPORTANT : Change these lines to change who is playing!
		Player_0 = new Claustrophobium();
		Player_1 =  new DeepQiumPlayer("trainedNetwork.premium");


		//Set up the names in the state object
		name_0 = Player_0.getPlayName();
		name_1 = Player_1.getPlayName();

		turn_num = 0;
		state = new GameState(name_0, name_1 );
		
		Player_0.begin(new GameState(state), 0 );
		Player_1.begin(new GameState(state), 1 );
		
		primary = primaryStage;
		String title = " " + name_0 + " " + name_1;
		
		primaryStage.setTitle(title);
		Group root = new Group();
		test_canvas = new Canvas(1200, 850);

		root.getChildren().add(test_canvas);
		Scene mainScene;
		mainScene = new Scene(root, 1200, 850, Color.BEIGE);
	
		
		primaryStage.setScene(mainScene);
		primaryStage.show();

		Timeline quickTimer = new Timeline(new KeyFrame(Duration.seconds(DELAY_TIME), new EventHandler<ActionEvent>() {	
			public void handle(ActionEvent event) {
				nextTurn();
			}
		}));

		
		quickTimer.setCycleCount(Timeline.INDEFINITE);
		quickTimer.play();		

			
		GameDisplayGraphics.displayState(test_canvas, state);
    }


    void nextTurn(){
		List<Move> moves = new ArrayList<Move>();
		
		if (! state.isGameOver() ){
			DirType new_dir;

			new_dir = Player_0.getMove( new GameState(state) );
			if(new_dir != null){
			moves.add(new Move(0, new_dir));
			}

			new_dir = Player_1.getMove( new GameState(state) );
			if(new_dir != null){
			moves.add(new Move(1, new_dir));
			}
			
			state = GameRules.makeMoves(state, moves);
			turn_num = turn_num + 1;

			//If we hit the turn limit, the longer snake wins
			if (turn_num >= MAX_TURN){
				int sn0_len = state.getSnake(0).max_len;
				int sn1_len = state.getSnake(1).max_len;
				state.makeGameOver();
				if(sn0_len > sn1_len){
					state.game_winner = 0;
				}
				if(sn1_len > sn0_len){
					state.game_winner = 1;
				}
				if(sn0_len == sn1_len){
					Random rand = new Random();
					int x = rand.nextInt(2);
					state.game_winner = x;
				}
			}
			GameDisplayGraphics.displayState(test_canvas, state);
		} else {
			state = new GameState(name_0, name_1 );
			Player_0.begin(new GameState(state), 0 );
			Player_1.begin(new GameState(state), 1 );
		}
    }
}

	


	
