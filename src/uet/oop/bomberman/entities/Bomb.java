package uet.oop.bomberman.entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Entity {
    private int animate = 0;
    public static int cnt = 0;
    private boolean exploded = false;

    private List<Entity> entities;

    public Bomb(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
        cnt++;
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
                        int size = 1;
                        Sprite sprite = null;
                        for (int c = 1; c <= size; c++) {
                            int i = x / Sprite.SCALED_SIZE - c, j = y / Sprite.SCALED_SIZE;
                            Entity cur = BombermanGame.table[i][j];
                            if (cur instanceof Wall) break;
                            if (cur instanceof Brick) {
                                ((Brick) cur).exploded = true;
                                break;
                            }
                            if (c < size) {
                                entities.add(new Flame(i, j, Sprite.explosion_horizontal.getFxImage(), Direction.OH, entities));
                            } else {
                                entities.add(new Flame(i, j, Sprite.explosion_horizontal_left_last.getFxImage(), Direction.L, entities));
                            }
                        }
                        for (int c = 1; c <= size; c++) {
                            int i = x / Sprite.SCALED_SIZE + c, j = y / Sprite.SCALED_SIZE;
                            Entity cur = BombermanGame.table[i][j];
                            if (cur instanceof Wall) break;
                            if (cur instanceof Brick) {
                                ((Brick) cur).exploded = true;
                                break;
                            }
                            if (c < size) {
                                entities.add(new Flame(i, j, Sprite.explosion_horizontal.getFxImage(), Direction.OH, entities));
                            } else {
                                entities.add(new Flame(i, j, Sprite.explosion_horizontal_right_last.getFxImage(), Direction.R, entities));
                            }
                        }
                        for (int c = 1; c <= size; c++) {
                            int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE - c;
                            Entity cur = BombermanGame.table[i][j];
                            if (cur instanceof Wall) break;
                            if (cur instanceof Brick) {
                                ((Brick) cur).exploded = true;
                                break;
                            }
                            if (c < size) {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical.getFxImage(), Direction.OV, entities));
                            } else {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical_top_last.getFxImage(), Direction.U, entities));
                            }
                        }
                        for (int c = 1; c <= size; c++) {
                            int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE + c;
                            Entity cur = BombermanGame.table[i][j];
                            if (cur instanceof Wall) break;
                            if (cur instanceof Brick) {
                                ((Brick) cur).exploded = true;
                                break;
                            }
                            if (c < size) {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical.getFxImage(), Direction.OV, entities));
                            } else {
                                entities.add(new Flame(i, j, Sprite.explosion_vertical_down_last.getFxImage(), Direction.D, entities));
                            }
                        }
                        Timer bombTimer = new Timer();
                        bombTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                for (int c = 0; c <= size; c++) {
                                    int i = x / Sprite.SCALED_SIZE - c, j = y / Sprite.SCALED_SIZE;
//                                    int i = (x - c + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, j = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
                                    Entity cur = BombermanGame.table[i][j];
                                    if (cur instanceof Wall) break;
                                    if (cur instanceof Brick) {
                                        ((Brick) cur).exploded = true;
                                        break;
                                    }
                                    if (cur instanceof Balloom) ((Balloom) cur).died = true;
                                    if (cur instanceof Oneal) ((Oneal) cur).died = true;
                                }
                                for (int c = 1; c <= size; c++) {
                                    int i = x / Sprite.SCALED_SIZE + c, j = y / Sprite.SCALED_SIZE;
//                                    int i = (x + c + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, j = (y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
                                    Entity cur = BombermanGame.table[i][j];
                                    if (cur instanceof Wall) break;
                                    if (cur instanceof Brick) {
                                        ((Brick) cur).exploded = true;
                                        break;
                                    }
                                    if (cur instanceof Balloom) ((Balloom) cur).died = true;
                                    if (cur instanceof Oneal) ((Oneal) cur).died = true;
                                }
                                for (int c = 1; c <= size; c++) {
                                    int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE - c;
//                                    int i = (x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, j = (y - c + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
                                    Entity cur = BombermanGame.table[i][j];
                                    if (cur instanceof Wall) break;
                                    if (cur instanceof Brick) {
                                        ((Brick) cur).exploded = true;
                                        break;
                                    }
                                    if (cur instanceof Balloom) ((Balloom) cur).died = true;
                                    if (cur instanceof Oneal) ((Oneal) cur).died = true;
                                }
                                for (int c = 1; c <= size; c++) {
                                    int i = x / Sprite.SCALED_SIZE, j = y / Sprite.SCALED_SIZE + c;
//                                    int i = (x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, j = (y + c + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE;
                                    System.out.println(i + " " + j);
                                    Entity cur = BombermanGame.table[i][j];
                                    if (cur instanceof Wall) break;
                                    if (cur instanceof Brick) {
                                        ((Brick) cur).exploded = true;
                                        break;
                                    }
                                    if (cur instanceof Balloom) ((Balloom) cur).died = true;
                                    if (cur instanceof Oneal) ((Oneal) cur).died = true;
                                }
                            }
                        }, 10);
                    });
        }
        if (animate == 60) {
            Platform.runLater(
                    () -> {
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
