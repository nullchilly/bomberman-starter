package algo;

import core.Game;
import entities.Entity;
import entities.Entity.Direction;
import entities.bombs.Bomb;
import entities.character.Enemy;
import entities.tiles.Brick;
import entities.tiles.Wall;
import graphics.Sprite;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

import static core.Game.bomber;
import static core.Game.table;
import static entities.Entity.getEntity;
import static java.lang.Math.abs;

public class FindPathAI {
    public static int distance(int a, int b, int x, int y) {
        int ans = abs(a - x) + abs(b - y);
        return ans;
    }

    public static boolean checkWall(int x, int y) {
        if (x < 0 || y < 0 || x > Sprite.SCALED_SIZE * Game.WIDTH || y > Sprite.SCALED_SIZE * Game.HEIGHT) {
            return false;
        }

        x /= Sprite.SCALED_SIZE;
        y /= Sprite.SCALED_SIZE;
        Entity cur = getEntity(x, y);
        return !(cur instanceof Wall) && !(cur instanceof Brick) && !(cur instanceof Bomb) && !(cur instanceof Enemy);
    }

    public static Direction bfsFindBrick(int i, int j) {
        int px = i, py = j;
        boolean[][] check = new boolean[Game.WIDTH][Game.HEIGHT];
        Queue<Pair<Integer, Pair<Integer, Direction>>> q = new LinkedList<>();
        if (checkWall((i + 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE)) {
            check[i + 1][j] = true;
            q.add(new Pair<>(i + 1, new Pair<>(j, Direction.R)));
        }
        if (checkWall(i * Sprite.SCALED_SIZE, (j + 1) * Sprite.SCALED_SIZE)) {
            check[i][j + 1] = true;
            q.add(new Pair<>(i, new Pair<>(j + 1, Direction.D)));
        }
        if (checkWall((i - 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE)) {
            check[i - 1][j] = true;
            q.add(new Pair<>(i - 1, new Pair<>(j, Direction.L)));
        }
        if (checkWall(i * Sprite.SCALED_SIZE, (j - 1) * Sprite.SCALED_SIZE)) {
            check[i][j - 1] = true;
            q.add(new Pair<>(i, new Pair<>(j - 1, Direction.U)));
        }
        check[i][j] = true;
        Direction firstDirection = null;
        while (!q.isEmpty()) {
            i = q.peek().getKey();
            assert q.peek() != null;
            j = q.peek().getValue().getKey();
            assert q.peek() != null;
            Direction direction = q.peek().getValue().getValue();
            q.remove();
            if (checkWall((i + 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE) && !check[i + 1][j]) {
                q.add(new Pair<>(i + 1, new Pair<>(j, direction)));
                check[i + 1][j] = true;
            }
            if (checkWall(i * Sprite.SCALED_SIZE, (j + 1) * Sprite.SCALED_SIZE) && !check[i][j + 1]) {
                q.add(new Pair<>(i, new Pair<>(j + 1, direction)));
                check[i][j + 1] = true;
            }
            if (checkWall((i - 1) * Sprite.SCALED_SIZE, j * Sprite.SCALED_SIZE) && !check[i - 1][j]) {
                q.add(new Pair<>(i - 1, new Pair<>(j, direction)));
                check[i - 1][j] = true;
            }
            if (checkWall(i * Sprite.SCALED_SIZE, (j - 1) * Sprite.SCALED_SIZE) && !check[i][j - 1]) {
                q.add(new Pair<>(i, new Pair<>(j - 1, direction)));
                check[i][j - 1] = true;
            }
            if (Bomb.cnt == 0) {
                if (table[px - 1][py] instanceof Brick || table[px + 1][py] instanceof Brick || table[px][py - 1] instanceof Brick || table[px][py + 1] instanceof Brick) {
//                    if (distance(i, j, px, py) <= 4) {
                    bomber.placeBomb();
//                    }
                    break;
                }
            }
            //boolean nope = false;
            if (table[i - 1][j] instanceof Brick || table[i + 1][j] instanceof Brick || table[i][j - 1] instanceof Brick || table[i][j + 1] instanceof Brick) {
                firstDirection = direction;
                break;
            }
        }
        return firstDirection;
    }
}