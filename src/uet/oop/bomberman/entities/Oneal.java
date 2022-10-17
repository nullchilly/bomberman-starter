package uet.oop.bomberman.entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;
import java.util.Random;

public class Oneal extends Entity {

    private boolean moving = false;
    private static final int STEP = Math.max(1, Math.round(Sprite.STEP / 2));

    public Oneal(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    public void getImg(boolean exploded) {
        Sprite sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, animate, 20);
        img = sprite.getFxImage();
    }

    private void chooseSprite() {
//        System.out.println(Sprite.STEP);
        Platform.runLater(() -> {
            int px = (x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
            BombermanGame.table[px][py] = null;
//            System.out.println(px + " " + py);
            Sprite sprite = Sprite.oneal_right1;
            switch (direction) {
                case U:
                    sprite = Sprite.oneal_right1;
                    if (checkWall(x, y - STEP + 3) && checkWall(x + Sprite.SCALED_SIZE - 3, y - STEP + 3)) {
                        y -= STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 20);
                    }
                    break;
                case D:
                    sprite = Sprite.oneal_right1;
                    if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkWall(x + Sprite.SCALED_SIZE - 3, y + STEP + Sprite.SCALED_SIZE - 3)) {
                        y += STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 20);
                    }
                    break;
                case L:
                    sprite = Sprite.oneal_left1;
                    if (checkWall(x - STEP, y + 3) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                        x -= STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left2, animate, 20);
                    }
                    break;
                case R:
                    sprite = Sprite.oneal_right1;
                    if (checkWall(x + STEP + Sprite.SCALED_SIZE - 3, y + 3) && checkWall(x + STEP + Sprite.SCALED_SIZE - 3, y + Sprite.SCALED_SIZE - 3)) {
                        x += STEP;
                        moving = true;
                    }
                    if (moving) {
                        sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 20);
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
            die_time++;
            img = Sprite.oneal_dead.getFxImage();
            if (die_time == 20) {
                Platform.runLater(() -> {
                    entities.remove(this);
                    BombermanGame.table[(x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE][(y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE] = null;
                });
            }
        } else {
            animate++;
            if (animate > 100000) animate = 0;
            if (animate % 70 == 0) {
                direction = Direction.values()[new Random().nextInt(Direction.values().length)];
//                System.out.println("Oneal " + x / Sprite.SCALED_SIZE + " " + y / Sprite.SCALED_SIZE);
            }
            moving = false;
            chooseSprite();
        }
    }
}
