package entities;

import core.Game;
import javafx.application.Platform;
import javafx.scene.image.Image;
import graphics.Sprite;

import java.util.List;
import java.util.Random;

import static core.Game.bomber;

public class Balloom extends Entity {

    private static final int STEP = Math.max(1, Math.round(Sprite.STEP / 2));
    private boolean moving = false;

    public Balloom(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    private void findDirection() {
        if (animate > 100000) animate = 0;
        if (animate % 30 == 0 && x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0) {
            direction = Direction.values()[new Random().nextInt(Direction.values().length)];
        }
    }
    private void balloomMoving() {
        Platform.runLater(() -> {
            int px = (x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
            Game.table[px][py] = null;
            sprite = Sprite.balloom_right1;
            switch (direction) {
                case U:
                    sprite = Sprite.balloom_left2;
                    if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                        y -= STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                    }
                    break;
                case D:
                    sprite = Sprite.balloom_right1;
                    if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                        y += STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                    }
                    break;
                case L:
                    sprite = Sprite.balloom_left1;
                    if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                        x -= STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, animate, 20);
                    }
                    break;
                case R:
                    sprite = Sprite.balloom_right1;
                    if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
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
            Game.table[px][py] = this;
        });
    }

    @Override
    public void update() {
        if (died) {
            dieEnemy(Sprite.balloom_dead);
            return;
        }
        int px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        int py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        if (bomber.getPlayerX() == px && bomber.getPlayerY() == py) {
            bomber.setDied();
        }
        animate++;
        moving = false;
        findDirection();
        balloomMoving();
    }
    public void updateBalloon() {}
}
