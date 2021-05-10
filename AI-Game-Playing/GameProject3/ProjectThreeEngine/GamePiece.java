package ProjectThreeEngine;

public interface GamePiece {
    public int getX();
    public int getY();

    //Deep Copy Method
    public GamePiece copy();
}
