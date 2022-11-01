package entities.character;

import core.Game;
import audio.Sound;
import entities.Entity;
import entities.bombs.Bomb;
import entities.tiles.Brick;
import entities.tiles.Wall;
import graphics.Sprite;
import javafx.application.Platform;
import javafx.scene.image.Image;

import java.util.Random;

import static core.Game.bomber;
import static core.Game.enemies;

public abstract class Enemy extends Entity {

    protected Entity old_cur = null;
    public Enemy(int x, int y, Image Img) {
        super(x, y, Img);
    }

    protected void findDirection() {
        if (animate > 100000) animate = 0;
        if (animate % 30 == 0 && x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE == 0) {
            direction = Direction.values()[new Random().nextInt(Direction.values().length)];
        }
    }



    protected void gotHurt(Sprite sprite) {
        hurt_time++;
        if (hurt_time == 1) {
            Sound.died.play();
        }
        img = sprite.getFxImage;
        if (hurt_time == 20) {
            hurt_time = 0;
            hurt = false;
            if (life == 0) {
                Platform.runLater(() -> {
                    enemies.remove(this);
                    Game.table[(x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE][(y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE] = old_cur;
                });
            }
        }
    }
    protected void checkCollideWithBomber () {
        if (bomber.getPlayerX() == getTileX() && bomber.getPlayerY() == getTileY() && !bomber.isProtectded()) {
            bomber.setHurt();
        }
    }
    public static boolean checkWall(int x, int y) {
        if (x < 0 || y < 0 || x > Sprite.SCALED_SIZE * Game.WIDTH || y > Sprite.SCALED_SIZE * Game.HEIGHT) {
            return false;
        }

        x /= Sprite.SCALED_SIZE;
        y /= Sprite.SCALED_SIZE;
        Entity cur = getEntity(x, y);
        return !(cur instanceof Wall) &&  !(cur instanceof Brick) && !(cur instanceof Bomb) && !(cur instanceof Kondoria);
    }
}
