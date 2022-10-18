package uet.oop.bomberman.entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.KeyListener;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

import static uet.oop.bomberman.BombermanGame.table;

public class Bomber extends Entity {

    private List<Entity> entities = new ArrayList<>();

    public static final int STEP = Sprite.STEP;
    private boolean moving = false;
    private KeyListener keyListener;

    public Bomber(int x, int y, Image img, KeyListener keyListener, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
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
        int px = (x + (75*Sprite.SCALED_SIZE)/(2*100))/Sprite.SCALED_SIZE;
        int py = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
        table[px][py] = null;
        if (keyListener.isPressed(KeyCode.D)) {
            direction = Direction.R;
            if (checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + 3) && checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + Sprite.SCALED_SIZE - 3)) {
                x += STEP;
                moving = true;
            }
        }
        if (keyListener.isPressed(KeyCode.A)) {
            direction = Direction.L;
            if (checkWall(x - STEP, y + 3) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                x -= STEP;
                moving = true;
            }
        }
        if (keyListener.isPressed(KeyCode.W)) {
            direction = Direction.U;
            if (checkWall(x, y - STEP + 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y - STEP + 3)) {
                y -= STEP;
                moving = true;
            }
        }
        if (keyListener.isPressed(KeyCode.S)) {
            direction = Direction.D;
            if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y + STEP + Sprite.SCALED_SIZE - 3)) {
                y += STEP;
                moving = true;
            }
        }
        chooseSprite();

        if (keyListener.isPressed(KeyCode.SPACE) && Bomb.cnt == 0) {
//            Entity object = new Bomb((x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, Sprite.bomb.getFxImage());
//            entities.add(object);
            Platform.runLater(() ->  {
                Entity object = new Bomb((x + (75*Sprite.SCALED_SIZE)/(2*100))/Sprite.SCALED_SIZE, (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, Sprite.bomb.getFxImage(), entities);
                entities.add(object);
            });
        }
        px = (x + (75*Sprite.SCALED_SIZE)/(2*100))/Sprite.SCALED_SIZE;
        py = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
        table[px][py] = this;
    }

    public void updateBomber() {}
}
