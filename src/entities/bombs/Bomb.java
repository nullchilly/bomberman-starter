package entities.bombs;

import audio.Sound;
import entities.Entity;
import entities.character.*;
import entities.items.*;
import entities.tiles.Brick;
import entities.tiles.Wall;
import graphics.Sprite;
import javafx.application.Platform;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static core.Game.*;

public class Bomb extends Entity {
    private int animate = 0;
    public static int cnt = 0;
    private final int size;
    private boolean exploded = false;
    private Entity portalPos = null;
    private final List<Entity> entities;

    public Bomb(int x, int y, Image img, List<Entity> entities, int size) {
        super(x, y, img);
        this.size = size;
        this.entities = entities;
        cnt++;
        if (table[x][y] instanceof PortalItem) {
            portalPos = table[x][y];
        }
        table[x][y] = this;
    }

    public void getImg() {
        if (exploded) {
            sprite =
                    Sprite.movingSprite(
                            Sprite.bomb_exploded,
                            Sprite.bomb_exploded1,
                            Sprite.bomb_exploded2,
                            animate,
                            20);
        } else {
            sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 20);
        }
        img = sprite.getFxImage;
    }

    private boolean checkBreak(int i, int j) {
        Entity cur = table[i][j];
        if (cur instanceof Wall) {
            return true;
        }
        if (cur instanceof Brick) {
            ((Brick) cur).setExploded();
            return true;
        }
        return false;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setExplode() {
        this.animate = 69;
    }

    private void setDied(int i, int j) {
        Entity cur = table[i][j];
        if (cur instanceof Enemy) {
            for (Entity e : enemies) {
                if (e.getTileX() == i && e.getTileY() == j) {
                    e.setHurt();
                }
            }
        }
//        if (cur instanceof Oneal) cur.setDied();
        if (cur instanceof Item) cur.setDied();
//        if (cur instanceof SpeedItem) cur.setDied();
//        if (cur instanceof BombItem) cur.setDied();
        if (cur instanceof Bomb && !((Bomb) cur).isExploded()) ((Bomb) cur).setExplode();
        if (bomber.getPlayerX() == i && bomber.getPlayerY() == j && !bomber.isFlamePass() && !bomber.isProtectded()) {
            bomber.setHurt();
        }
    }

    @Override
    public void update() {
        animate++;
        int px = x / Sprite.SCALED_SIZE;
        int py = y / Sprite.SCALED_SIZE;
        if (table[px][py] instanceof PortalItem) {
            portalPos = table[px][py];
        }
        table[px][py] = this;

        if (animate == 70) {
            (new Sound("explosion.wav")).play();
            exploded = true;
            Platform.runLater(
                    () -> {
                        for (int c = 1; c <= size; c++) {
                            int i = x / Sprite.SCALED_SIZE - c, j = y / Sprite.SCALED_SIZE;
                            if (checkBreak(i, j)) break;
                            if (c < size) {
                                entities.add(new Flame(i, j, null, Direction.OH, entities));
                            } else {
                                entities.add(new Flame(i, j, null, Direction.L, entities));
                            }
                        }
                        for (int c = 1; c <= size; c++) {
                            int i = x / Sprite.SCALED_SIZE + c, j = y / Sprite.SCALED_SIZE;
                            if (checkBreak(i, j)) break;
                            if (c < size) {
                                entities.add(new Flame(i, j, Sprite.explosion_horizontal.getFxImage, Direction.OH, entities));
                            } else {
                                entities.add(new Flame(i, j, Sprite.explosion_horizontal_right_last.getFxImage, Direction.R, entities));
                            }
                        }
                        for (int c = 1; c <= size; c++) {
                            int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE - c;
                            if (checkBreak(i, j)) break;
                            if (c < size) {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical.getFxImage, Direction.OV, entities));
                            } else {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical_top_last.getFxImage, Direction.U, entities));
                            }
                        }
                        for (int c = 1; c <= size; c++) {
                            int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE + c;
                            if (checkBreak(i, j)) break;
                            if (c < size) {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical.getFxImage, Direction.OV, entities));
                            } else {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical_down_last.getFxImage, Direction.D, entities));
                            }
                        }
                        Timer bombTimer = new Timer();
                        bombTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                for (int c = 0; c <= size; c++) {
                                    int i = x / Sprite.SCALED_SIZE - c, j = y / Sprite.SCALED_SIZE;
                                    if (checkBreak(i, j)) break;
                                    setDied(i, j);
                                }
                                for (int c = 1; c <= size; c++) {
                                    int i = x / Sprite.SCALED_SIZE + c, j = y / Sprite.SCALED_SIZE;
                                    if (checkBreak(i, j)) break;
                                    setDied(i, j);
                                }
                                for (int c = 1; c <= size; c++) {
                                    int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE - c;
                                    if (checkBreak(i, j)) break;
                                    setDied(i, j);
                                }
                                for (int c = 1; c <= size; c++) {
                                    int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE + c;
                                    if (checkBreak(i, j)) break;
                                    setDied(i, j);
                                }
                            }
                        }, 10);
                    });
        }
        if (animate == 80) {
            Platform.runLater(
                    () -> {
                        table[px][py] = portalPos;
                        entities.remove(this);
                        cnt--;
                    });
        }
        if (animate > 1000000) {
            animate = 0;
        }
        getImg();
    }
}
