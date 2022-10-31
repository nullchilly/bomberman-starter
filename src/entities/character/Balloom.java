package entities.character;

import core.Game;
import entities.items.Item;
import graphics.Sprite;
import javafx.scene.image.Image;

import static core.Game.table;

public class Balloom extends Enemy {

    private static final int STEP = Math.max(1, Sprite.STEP / 2);
    private boolean moving = false;

    public Balloom(int x, int y, Image img) {
        super(x, y, img);
    }

    private void balloomMoving() {
        int px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        if (table[px][py] instanceof Item) {
            old_cur = table[px][py];
            System.out.println("Whoops");
        }
        Game.table[px][py] = null;
        sprite = Sprite.balloom_right1;
        switch (direction) {
            case D:
                if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                    y += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                }
                break;
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
                if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 20);
                }
                break;
        }
        img = sprite.getFxImage;
        int new_px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        int new_py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        if (new_px != px || new_py != py) {
            Game.table[px][py] = old_cur;
            old_cur = null;
        }
        if (table[new_px][new_py] instanceof Item) {
            old_cur = table[new_px][new_py];
            System.out.println("Whoops");
        }
        Game.table[new_px][new_py] = this;
    }

    @Override
    public void update() {
        if (hurt) {
            gotHurt(Sprite.balloom_dead);
            return;
        }
        animate++;
        moving = false;
        checkCollideWithBomber();
        findDirection();
        balloomMoving();
    }
}
