package entities.character;

import core.Game;
import entities.Entity;
import entities.items.Item;
import entities.tiles.Brick;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;

import static core.Game.bomber;
import static core.Game.table;

public class Kondoria extends Entity {

    private static final int STEP = Math.max(1, Sprite.STEP / 2);
    private boolean moving = false;

    public Kondoria(int x, int y, Image img) {
        super(x, y, img);
    }

    private void findDirection() {
        if (animate > 100000) animate = 0;
        if (animate % 30 == 0 && x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0) {
            direction = Direction.values()[new Random().nextInt(Direction.values().length)];
        }
    }

    private void kondoriaMoving() {
        int px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
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
        if (new_px != px || new_py != py) {
            Game.table[px][py] = old_cur;
            old_cur = null;
        }
        if (table[new_px][new_py] instanceof Item || table[new_px][new_py] instanceof Brick) {
            old_cur = table[new_px][new_py];
            System.out.println("Whoops");
        }
        Game.table[new_px][new_py] = this;
    }

    @Override
    public void update() {
        if (hurt) {
            gotHurt(Sprite.kondoria_dead);
            return;
        }
        int px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        int py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        if (bomber.getPlayerX() == px && bomber.getPlayerY() == py && bomber.isProtectded()) {
            bomber.setHurt();
        }
        animate++;
        moving = false;
        findDirection();
        kondoriaMoving();
    }
}
