package entities.character;

import algo.FindPath;
import core.Game;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.Random;

import static core.Game.table;

public class Oneal extends Enemy {

    private static final int STEP = Math.max(1, Sprite.STEP / 2);
    private boolean moving = false;
    private int px;
    private int py;
    private boolean canReach = false;

    public Oneal(int x, int y, Image img) {
        super(x, y, img);
    }

    protected void findDirection() {
        animate++;
        if (animate > 100000) animate = 0;
        if (x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0) {
            Direction newDirection = FindPath.bfs(px, py);
            if (newDirection != null) {
                canReach = true;
                direction = newDirection;
            }
            moving = false;
        }
        if (animate % 70 == 0 && x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0 && !canReach) {
            direction = Direction.values()[new Random().nextInt(Direction.values().length)];
            moving = false;
        }
    }

    private void onealMoving() {
        int px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        table[px][py] = null;
        sprite = Sprite.oneal_right1;
        switch (direction) {
            case U:
                if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                    y -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 20);
                }
                break;
            case D:
                if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                    y += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 20);
                }
                break;
            case L:
                sprite = Sprite.oneal_left1;
                if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                    x -= STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, animate, 20);
                }
                break;
            case R:
                if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                    x += STEP;
                    moving = true;
                }
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 20);
                }
                break;
        }
        img = sprite.getFxImage;
        int new_px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        int new_py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        Game.table[new_px][new_py] = this;
    }

    @Override
    public void update() {
        px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        if (hurt) {
            gotHurt(Sprite.oneal_dead);
            return;
        }
        checkCollideWithBomber();
        findDirection();
        onealMoving();
    }
}
