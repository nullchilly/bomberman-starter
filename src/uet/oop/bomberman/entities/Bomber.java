package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.KeyListener;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends Entity {

    private int animate = 0;
    public static final int STEP = Sprite.STEP;
    private enum Direction {
        L,
        R,
        U,
        D
    };
    private Direction direction = Direction.R;
    private boolean moving = false;
    private KeyListener keyListener;

    public Bomber(int x, int y, Image img, KeyListener keyListener) {
        super(x, y, img);
        this.keyListener = keyListener;
    }

    private void chooseSprite() {
        Sprite sprite = Sprite.player_right;
        switch (direction) {
            case U:
                sprite = Sprite.player_up;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20);
                }
                break;
            case R:
                sprite = Sprite.player_right;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                }
                break;
            case D:
                sprite = Sprite.player_down;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20);
                }
                break;
            case L:
                sprite = Sprite.player_left;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20);
                }
                break;
            default:
                sprite = Sprite.player_right;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                }
                break;
        }
        img = sprite.getFxImage();
    }

    @Override
    public void update() {
        animate++;
        if (animate > 100000) animate = 0;
        moving = false;
        if (keyListener.isPressed(KeyCode.D)) {
            direction = Direction.R;
            if (checkWall(x + STEP + Sprite.SCALED_SIZE - 15, y + 5) && checkWall(x + STEP + Sprite.SCALED_SIZE - 15, y + Sprite.SCALED_SIZE - 5)) {
                x += STEP;
                moving = true;
            }
        }
        if (keyListener.isPressed(KeyCode.A)) {
            direction = Direction.L;
            if (checkWall(x - STEP, y + 5) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 5)) {
                x -= STEP;
                moving = true;
            }
        }
        if (keyListener.isPressed(KeyCode.W)) {
            direction = Direction.U;
            if (checkWall(x, y - STEP + 5) && checkWall(x + Sprite.SCALED_SIZE - 15, y - STEP + 5)) {
                y -= STEP;
                moving = true;
            }
        }
        if (keyListener.isPressed(KeyCode.S)) {
            direction = Direction.D;
            if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 5) && checkWall(x + Sprite.SCALED_SIZE - 15, y + STEP + Sprite.SCALED_SIZE - 5)) {
                y += STEP;
                moving = true;
            }
        }
        chooseSprite();
    }

    public void updateBomber() {}
}
