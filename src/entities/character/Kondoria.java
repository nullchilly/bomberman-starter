package entities.character;

import core.Game;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Kondoria extends Enemy {

    private static final int STEP = Math.max(1, Sprite.STEP / 2);
    private boolean moving = false;

    public Kondoria(int x, int y, Image img) {
        super(x, y, img);
    }

    private void kondoriaMoving() {
        int px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
//        if (table[px][py] instanceof Item || table[px][py] instanceof Brick) {
//            old_cur = table[px][py];
//            System.out.println("Whoops");
//        }
        Game.table[px][py] = null;
        sprite = Sprite.kondoria_right1;
        switch (direction) {
            case D:
                if (checkBrick(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkBrick(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                    y += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 20);
                }
                break;
            case U:
                sprite = Sprite.kondoria_left2;
                if (checkBrick(x, y - STEP) && checkBrick(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                    y -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 20);
                }
                break;
            case L:
                sprite = Sprite.kondoria_left1;
                if (checkBrick(x - STEP, y) && checkBrick(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                    x -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, animate, 20);
                }
                break;
            case R:
                if (checkBrick(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkBrick(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 20);
                }
                break;
        }
        img = sprite.getFxImage;
        int new_px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        int new_py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
//        if (new_px != px || new_py != py) {
//            Game.table[px][py] = old_cur;
//            old_cur = null;
//        }
//        if (table[new_px][new_py] instanceof Item || table[new_px][new_py] instanceof Brick) {
//            old_cur = table[new_px][new_py];
//            System.out.println("Whoops");
//        }
        Game.table[new_px][new_py] = this;
    }

    @Override
    public void update() {
        if (hurt) {
            gotHurt(Sprite.kondoria_dead);
            return;
        }
        animate++;
        moving = false;
        checkCollideWithBomber();
        findDirection();
        kondoriaMoving();
    }
}
