package TeamiumPremium;

import ProjectThreeEngine.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Claustrophobium implements Player {

    int me;

    final int maxX = 15;
    final int maxY = 15;

    public void begin(GameState state, int play) {
        this.me = play;
    }

    public DirType getMove(GameState state) {
        Random rand = new Random();
        HeadPiece head = state.getSnake(this.me).head;

        ArrayList<DirType> goodDirections = new ArrayList<DirType>();

        DirType[] possibleDirections = DirType.values();
        for (DirType dir: possibleDirections) {
            int x = head.getX();
            int y = head.getY();
            switch(dir) {
                case North:
                    y--;
                    break;
                case East:
                    x++;
                    break;
                case South:
                    y++;
                    break;
                case West:
                    x--;
                    break;
            }
            if (!isPositionDeath(state, x, y)) goodDirections.add(dir);
            else if (state.isFood(x, y)) {
                goodDirections.add(dir);
                goodDirections.add(dir);
            }
        }
        return goodDirections.get(rand.nextInt(goodDirections.size()));
    }

    public String getPlayName() {
        return "Claustrophobium";
    }

    private boolean isPositionDeath(GameState state, int x, int y) {
        if (state.getPiece(x, y) == null) return !isPieceInBounds(x, y);
        return true;
    }

    private boolean isPieceInBounds(int x, int y) {
        return ((x < maxX && x >= 0) &&
                (y < maxY && y >= 0));
    }

}