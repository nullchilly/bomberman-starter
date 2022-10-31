package entities;

import entities.character.Balloom;
import entities.character.Oneal;
import entities.items.BombItem;
import entities.items.FlameItem;
import entities.items.PortalItem;
import entities.items.SpeedItem;
import entities.tiles.Brick;
import entities.tiles.Wall;
import javafx.application.Platform;
import javafx.scene.image.Image;
import graphics.Sprite;

import java.util.*;

import static core.Game.bomber;
import static core.Game.table;

public class Bomb extends Entity {
    private int animate = 0;
    public static int cnt = 0;
    private int size = 1;
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
        if (cur instanceof Balloom) cur.setDied();
        if (cur instanceof Oneal) cur.setDied();
        if (cur instanceof FlameItem) cur.setDied();
        if (cur instanceof SpeedItem) cur.setDied();
        if (cur instanceof BombItem) cur.setDied();
        if (cur instanceof Bomb && !((Bomb) cur).isExploded()) ((Bomb) cur).setExplode();
        if (bomber.getPlayerX() == i && bomber.getPlayerY() == j ) bomber.setDied();
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
            Entity finalCur = portalPos;
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
