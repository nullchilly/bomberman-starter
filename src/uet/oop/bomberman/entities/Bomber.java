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
        int time = BombermanGame.time++;
        if (keyListener.isPressed(KeyCode.D)) {
            int animate = BombermanGame.animate++;
            img = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, animate, time).getFxImage();
            System.out.println(x + " " + y);
            if (checkWall(x + STEP + Sprite.SCALED_SIZE - 20, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 20, y + Sprite.SCALED_SIZE - 5))
                x += STEP;
        }
        if (keyListener.isPressed(KeyCode.A)) {
            if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 5))
                x -= STEP;
        }
        if (keyListener.isPressed(KeyCode.W)) {
            if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 20, y - STEP))
                y -= STEP;
        }
        if (keyListener.isPressed(KeyCode.S)) {
            if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 5) && checkWall(x + Sprite.SCALED_SIZE - 20, y + STEP + Sprite.SCALED_SIZE - 5)) {
                y += STEP;
            }
        }
    }

    public void updateBomber() {}
}
