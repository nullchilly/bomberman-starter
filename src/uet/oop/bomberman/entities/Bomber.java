package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.KeyListener;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends Entity {

    public static final int STEP = 4;

    private KeyListener keyListener;

    public Bomber(int x, int y, Image img, KeyListener keyListener) {
        super(x, y, img);
        this.keyListener = keyListener;
    }

    @Override
    public void update() {
        if (keyListener.isPressed(KeyCode.D)) {
            System.out.println(x + " " + y);
            if (checkWall(x + STEP + 60, y))
                x += STEP;
        }
        if (keyListener.isPressed(KeyCode.A)) {
            if (checkWall(x - STEP, y))
                x -= STEP;
        }
        if (keyListener.isPressed(KeyCode.W)) {
            if (checkWall(x, y - STEP))
                y -= STEP;
        }
        if (keyListener.isPressed(KeyCode.S)) {
            if (checkWall(x, y + STEP + 60)) {
                y += STEP;
            }
        }
    }

    public void updateBomber() {}
}
