package ProjectThreeEngine;

public interface Player{

    //This function is called when the game starts
    public void begin(GameState init_state, int play_num);

    //You can return null to just keep going straight
    public DirType getMove(GameState state);

    public String getPlayName();
}

   
