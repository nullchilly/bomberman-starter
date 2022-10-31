package entities.character;

import core.Game;
import entities.Entity;
import entities.items.Item;
import entities.items.PortalItem;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Pair;
import graphics.Sprite;

import java.util.*;

import static core.Game.bomber;
import static core.Game.table;

public class
Oneal extends Entity {

    private boolean moving = false;
    private int px;
    private int py;
    private boolean canReach = false;
    private boolean[][] check = new boolean[Game.WIDTH][Game.HEIGHT];
    private static final int STEP = Math.max(1, Sprite.STEP / 2);

    public Oneal(int x, int y, Image img) {
        super(x, y, img);
    }

    private void findDirection() {
        animate++;
        if (animate > 100000) animate = 0;
        if (x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0) {
            check = new boolean[Game.WIDTH][Game.HEIGHT];
            direction = bfs(px, py);
            moving = false;
        }
        if (animate % 70 == 0 && x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0 && !canReach) {
            direction = Direction.values()[new Random().nextInt(Direction.values().length)];
            moving = false;
        }
    }
    private void onealMoving() {
        int px = (x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, py = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
        if (table[px][py] instanceof PortalItem) {
            old_cur = table[px][py];
            System.out.println("Whoops");
        }
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
                    sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left2, animate, 20);
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
        int new_px = (x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
        int new_py = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
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

    public Direction bfs(int i, int j) {
        Queue< Pair<Integer, Pair<Integer, Direction> >> q = new LinkedList<>();
        if (Entity.checkWall((i + 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE)) {
            check[i + 1][j] = true;
            q.add(new Pair<>(i + 1, new Pair<>(j, Direction.R)));
        }
        if (Entity.checkWall(i * Sprite.SCALED_SIZE, (j + 1) * Sprite.SCALED_SIZE)) {
            check[i][j + 1] = true;
            q.add(new Pair<>(i, new Pair<>(j + 1, Direction.D)));
        }
        if (Entity.checkWall((i - 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE)) {
            check[i - 1][j] = true;
            q.add(new Pair<>(i - 1, new Pair<>(j, Direction.L)));
        }
        if (Entity.checkWall(i * Sprite.SCALED_SIZE, (j - 1) * Sprite.SCALED_SIZE)) {
            check[i][j - 1] = true;
            q.add(new Pair<>(i, new Pair<>(j - 1, Direction.U)));
        }
        check[i][j] = true;
        while(!q.isEmpty()) {
            i = q.peek().getKey();
            assert q.peek() != null;
            j = q.peek().getValue().getKey();
            assert q.peek() != null;
            Direction direction = q.peek().getValue().getValue();
            q.remove();
            if (Entity.checkWall((i + 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE) && !check[i + 1][j]) {
                q.add(new Pair<>(i + 1, new Pair<>(j,direction)));
                check[i + 1][j] = true;
            }
            if (Entity.checkWall(i * Sprite.SCALED_SIZE, (j + 1) * Sprite.SCALED_SIZE) && !check[i][j + 1]) {
                q.add(new Pair<>(i, new Pair<>(j + 1, direction)));
                check[i][j + 1] = true;
            }
            if (Entity.checkWall((i - 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE) && !check[i - 1][j]) {
                q.add(new Pair<>(i - 1, new Pair<>(j, direction)));
                check[i - 1][j] = true;
            }
            if (Entity.checkWall(i * Sprite.SCALED_SIZE, (j - 1) * Sprite.SCALED_SIZE) && !check[i][j - 1]) {
                q.add(new Pair<>(i, new Pair<>(j - 1, direction)));
                check[i][j - 1] = true;
            }
//            Entity cur = Entity.getEntity(i, j);
//            if (cur instanceof Bomber) {
            if (bomber.getPlayerX() == i && bomber.getPlayerY() == j) {
//                System.out.println(i + " " + j + " " + px + " " + py);
//                if (i == px && i == py) {
//                    Platform.exit();
//                }
//                System.out.println("Oops");
                canReach = true;
                return direction;
            }
        }
        return this.direction;
    }
    @Override
    public void update() {
        px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        if (bomber.getPlayerX() == px && bomber.getPlayerY() == py) {
            bomber.setDied();
        }
        if (died) {
            dieEnemy(Sprite.oneal_dead);
            return;
        }

        findDirection();
        onealMoving();
    }
}
