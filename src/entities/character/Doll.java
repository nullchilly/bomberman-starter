package entities.character;

import core.Game;
import entities.items.Item;
import graphics.Sprite;
import javafx.scene.image.Image;

import static core.Game.table;

public class Doll extends Enemy {

    private static final int STEP = Math.max(1, Sprite.STEP / 2);
    private boolean moving = false;
    public Doll(int x, int y, Image img) {
        super(x, y, img);
        life = 2;
    }

    private void dollMoving() {
        int px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        Game.table[px][py] = null;
        sprite = Sprite.doll_left1;
        switch (direction) {
            case D:
                if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                    y += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 20);
                }
                break;
            case U:
                sprite = Sprite.doll_left2;
                if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                    y -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 20);
                }
                break;
            case L:
                sprite = Sprite.doll_left1;
                if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                    x -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, animate, 20);
                }
                break;
            case R:
                if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 20);
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
            gotHurt(Sprite.doll_dead);
            return;
        }
        animate++;
        moving = false;
        checkCollideWithBomber();
        findDirection();
        dollMoving();
    }
}
