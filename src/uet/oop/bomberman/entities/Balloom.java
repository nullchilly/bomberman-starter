package uet.oop.bomberman.entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.KeyListener;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Balloom extends Entity {

    private int animate = 0;
    private int animated = 0;
    public boolean died = false;

    private List<Entity> entities = new ArrayList<>();

    public static final int STEP = Sprite.STEP - 3;
    private enum Direction {
        L,
        R,
        U,
        D
    };
    private Direction direction = Direction.R;
    private boolean moving = false;
    private KeyListener keyListener;

    public Balloom(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    private void chooseSprite() {
        Platform.runLater(() -> {
            int px = (x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
            BombermanGame.table[px][py] = null;
            System.out.println(px + " " + py);
            Sprite sprite = Sprite.balloom_right1;
            switch (direction) {
                case U:
                    sprite = Sprite.balloom_left2;
                    if (checkWall(x, y - STEP + 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y - STEP + 3)) {
                        y -= STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                    }
                    break;
                case D:
                    sprite = Sprite.balloom_right1;
                    if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y + STEP + Sprite.SCALED_SIZE - 3)) {
                        y += STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                    }
                    break;
                case L:
                    sprite = Sprite.balloom_left1;
                    if (checkWall(x - STEP, y + 3) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                        x -= STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, animate, 20);
                    }
                    break;
                case R:
                    sprite = Sprite.balloom_right1;
                    if (checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + 3) && checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + Sprite.SCALED_SIZE - 3)) {
                        x += STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                    }
                    break;
            }
            img = sprite.getFxImage();
            px = (x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
            py = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
            BombermanGame.table[px][py] = this;
        });
    }

    @Override
    public void update() {
        if (died) {
            animated++;
            img = Sprite.balloom_dead.getFxImage();
            if (animated == 20) {
                Platform.runLater(() -> {
                    entities.remove(this);
                    BombermanGame.table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = null;
                });
            }
        } else {
            animate++;
            if (animate > 100000) animate = 0;
            if (animate % 70 == 0) {
                direction = Direction.values()[new Random().nextInt(Direction.values().length)];
                System.out.println("Balloom " + x / Sprite.SCALED_SIZE + " " + y / Sprite.SCALED_SIZE);
            }
            moving = false;
            chooseSprite();
        }
    }

    public void updateBalloon() {}
}
