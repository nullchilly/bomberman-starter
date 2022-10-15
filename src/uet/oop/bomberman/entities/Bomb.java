package uet.oop.bomberman.entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomb extends Entity {
    private int animate = 0;
    private boolean exploded = false;

    private List<Entity> entities;

    public Bomb(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    public void getImg() {
        Sprite sprite = null;
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
        img = sprite.getFxImage();
    }

    @Override
    public void update() {
        animate++;
        if (animate == 50) {
            exploded = true;
            Platform.runLater(
                    () -> {
                        int explosion_size = 2;
                        int[] dx = new int[explosion_size * 4];
                        int[] dy = new int[explosion_size * 4];
                        int cnt = 0;
                        for (int i = -explosion_size; i <= explosion_size; i++) {
                            if (i == 0) continue;
                            dx[cnt++] = i;
                        }
                        cnt = explosion_size * 4 - 1;
                        for (int i = explosion_size; i >= -explosion_size; i--) {
                            if (i == 0) continue;
                            dy[cnt--] = i;
                        }
                        for (int c = 0; c < explosion_size * 4; c++) {
                            int i = x / Sprite.SCALED_SIZE + dx[c], j = y / Sprite.SCALED_SIZE + dy[c];
                            if (BombermanGame.table[i][j] instanceof Wall) {
                                continue;
                            }
                            Sprite sprite = null;
                            if (dx[c] < 0) {
                                entities.add(new Flame(i, j, Sprite.explosion_horizontal_left_last.getFxImage(), Direction.L, entities));
                            }
                            if (dx[c] > 0) {
                                entities.add(new Flame(i, j, Sprite.explosion_horizontal_right_last.getFxImage(), Direction.R, entities));
                            }
                            if (dy[c] < 0) {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical_top_last.getFxImage(), Direction.U, entities));
                            }
                            if (dy[c] > 0) {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical_down_last.getFxImage(), Direction.D, entities));
                            }
                        }
                    });
        }
        if (animate == 60) {
            Platform.runLater( () -> {
                entities.remove(this);
            });
        }
        if (animate > 1000000) {
            animate = 0;
        }
        getImg();
    }
}
