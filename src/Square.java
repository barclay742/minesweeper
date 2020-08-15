import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class Square {
    int x, y, size, nearbyBombs, Bomb;
    Boolean reavealed;

    public Square(int i, int j, int size, int Bomb) {
        this.x = i;
        this.y = j;
        this.size = size;
        this.Bomb = Bomb;
        this.reavealed = false;
    }

    Square() {
    }

    public int Neighbours(Square[][] squares) {
        int rtn = 0;
        for (int j = y - 1; j < y + 2; j++) {
            for (int i = x - 1; i < x + 2; i++) {
                if (!(i == x && j == y)) {
                    if (i >= 0 && j >= 0 && i < squares.length && j < squares.length) {
                        rtn += squares[j][i].Bomb;
                    }
                }
            }
        }
        return rtn;
    }

    void setNearby() {
        this.nearbyBombs = Neighbours(Main.cells);
    }

    Boolean reveal() {
        if (!this.reavealed) {
            this.reavealed = true;
            return true;
        } else {
            return false;
        }
    }
}