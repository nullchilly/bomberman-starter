package entities.player;

import algo.FindPath;
import algo.FindPathAI;
import audio.Sound;
import core.Game;
import entities.Entity;
import entities.bombs.Bomb;
import entities.items.*;
import entities.tiles.Brick;
import graphics.Sprite;
import input.KeyListener;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.Random;

import static core.Game.*;

public class Bomber extends Player {
    public static boolean AI = false;

    private static int bomber_life;
    private final KeyListener keyListener;
    public int STEP = Sprite.STEP;
    public int protection_time = 0;
    private boolean moving = false;
    public int bombQuantity;
    private boolean flamePass = false;
    private boolean wallPass = false;
    private int bomb_size = 1;
    private boolean died = false;
    private int hurtTick = 0;

    public Bomber(int x, int y, Image img, KeyListener keyListener) {
        super(x, y, img);
        this.keyListener = keyListener;
        bombQuantity = 1;
        life = 3;
        if (AI) life = 15;
    }

    public static int getBomberLife() {
        return bomber_life;
    }

    public void setDied() {
        this.died = true;
        Sound.died.play();
    }

    private void chooseSprite() {
        animate++;
        if (animate > 100000) animate = 0;
        if (hurt) {
            img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 20).getFxImage;
            return;
        }
        Sprite sprite;
        switch (direction) {
            case U:
                sprite = Sprite.player_up;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20);
                }
                break;
            case D:
                sprite = Sprite.player_down;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20);
                }
                break;
            case L:
                sprite = Sprite.player_left;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20);
                }
                break;
            default:
                sprite = Sprite.player_right;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                }
                break;
        }
        img = sprite.getFxImage;
    }

    public void getItem() {
        int px = (x + (75 * Sprite.SCALED_SIZE) / (2 * 100)) / Sprite.SCALED_SIZE;
        int py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        if (table[px][py] instanceof FlameItem) {
            if (!((FlameItem) table[px][py]).isPickedup()) {
                bomb_size++;
            }
            ((FlameItem) table[px][py]).pick();
        } else if (table[px][py] instanceof SpeedItem) {
            if (!((SpeedItem) table[px][py]).isPickedup()) {
                STEP++;
            }
            ((SpeedItem) table[px][py]).pick();
        } else if (table[px][py] instanceof BombItem) {
            if (!((BombItem) table[px][py]).isPickedup()) {
                bombQuantity++;
            }
            ((BombItem) table[px][py]).pick();
        } else if (table[px][py] instanceof PortalItem) {

            if (enemies.isEmpty()) gameState = Game.STATE.NEXT_LV;

        } else if (table[px][py] instanceof FlamePassItem) {
            if (!((FlamePassItem) table[px][py]).isPickedup()) {
                flamePass = true;
            }
            ((FlamePassItem) table[px][py]).pick();
        } else if (table[px][py] instanceof WallPassItem) {
            if (!((WallPassItem) table[px][py]).isPickedup()) {
                wallPass = true;
            }
            ((WallPassItem) table[px][py]).pick();
        }
    }

    protected void AIDirection() {
        if (x % Sprite.SCALED_SIZE == 0 && y % Sprite.SCALED_SIZE== 0) {
            animate++;
            if (animate > 100000) animate = 0;
            int px = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            int py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            Direction newDirection = FindPathAI.bfsFindBrick(px, py);
            if (newDirection != null) {
                direction = newDirection;
            }
            moving = true;
        }
    }

    public void bomberMoving() {
        int px = (x + (75 * Sprite.SCALED_SIZE) / (2 * 100)) / Sprite.SCALED_SIZE;
        int py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
//        int px = (x + (1 * Sprite.SCALED_SIZE) / (2 * 1)) / Sprite.SCALED_SIZE;
//        int py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        Entity cur = table[px][py];
        table[px][py] = null;
        if (AI) {
            px = (x + (1 * Sprite.SCALED_SIZE) / (2 * 1)) / Sprite.SCALED_SIZE;
            py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            AIDirection();
            if (!wallPass) {
                if (keyListener.isPressed(KeyCode.D) || (AI && direction == Direction.R)) {
                    direction = Direction.R;
                    if (checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkWall(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                        x += STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.A) || (AI && direction == Direction.L)) {
                    direction = Direction.L;
                    if (checkWall(x - STEP, y) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                        x -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.W) || (AI && direction == Direction.U)) {
                    direction = Direction.U;
                    if (checkWall(x, y - STEP) && checkWall(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                        y -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.S) || (AI && direction == Direction.D)) {
                    direction = Direction.D;
                    if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkWall(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                        y += STEP;
                        moving = true;
                    }
                }
            } else {
                if (keyListener.isPressed(KeyCode.D) || AI) {
                    direction = Direction.R;
                    if (checkBrick(x + STEP + Sprite.SCALED_SIZE - 1, y) && checkBrick(x + STEP + Sprite.SCALED_SIZE - 1, y + Sprite.SCALED_SIZE - 1)) {
                        x += STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.A) || AI) {
                    direction = Direction.L;
                    if (checkBrick(x - STEP, y) && checkBrick(x - STEP, y + Sprite.SCALED_SIZE - 1)) {
                        x -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.W) || AI) {
                    direction = Direction.U;
                    if (checkBrick(x, y - STEP) && checkBrick(x + Sprite.SCALED_SIZE - 1, y - STEP)) {
                        y -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.S) || AI) {
                    direction = Direction.D;
                    if (checkBrick(x, y + STEP + Sprite.SCALED_SIZE - 1) && checkBrick(x + Sprite.SCALED_SIZE - 1, y + STEP + Sprite.SCALED_SIZE - 1)) {
                        y += STEP;
                        moving = true;
                    }
                }
            }
        } else if (!AI) {
            if (!wallPass) {
                if (keyListener.isPressed(KeyCode.D) || (AI && direction == Direction.R)) {
                    direction = Direction.R;
                    if (checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + 3) && checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + Sprite.SCALED_SIZE - 3)) {
                        x += STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.A) || (AI && direction == Direction.L)) {
                    direction = Direction.L;
                    if (checkWall(x - STEP, y + 3) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                        x -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.W) || (AI && direction == Direction.U)) {
                    direction = Direction.U;
                    if (checkWall(x, y - STEP + 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y - STEP + 3)) {
                        y -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.S) || (AI && direction == Direction.D)) {
                    direction = Direction.D;
                    if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y + STEP + Sprite.SCALED_SIZE - 3)) {
                        y += STEP;
                        moving = true;
                    }
                }
            } else {
                if (keyListener.isPressed(KeyCode.D) || AI) {
                    direction = Direction.R;
                    if (checkBrick(x + STEP + Sprite.SCALED_SIZE - 12, y + 3) && checkBrick(x + STEP + Sprite.SCALED_SIZE - 12, y + Sprite.SCALED_SIZE - 3)) {
                        x += STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.A) || AI) {
                    direction = Direction.L;
                    if (checkBrick(x - STEP, y + 3) && checkBrick(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                        x -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.W) || AI) {
                    direction = Direction.U;
                    if (checkBrick(x, y - STEP + 3) && checkBrick(x + Sprite.SCALED_SIZE - 12, y - STEP + 3)) {
                        y -= STEP;
                        moving = true;
                    }
                }
                if (keyListener.isPressed(KeyCode.S) || AI) {
                    direction = Direction.D;
                    if (checkBrick(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkBrick(x + Sprite.SCALED_SIZE - 12, y + STEP + Sprite.SCALED_SIZE - 3)) {
                        y += STEP;
                        moving = true;
                    }
                }
            }
        }
        if (moving && animate % 15 == 0) {
            Sound.move.play();
        }
        table[px][py] = cur;
    }

    public void placeBomb() {
        if (Bomb.cnt < bombQuantity && !(table[getPlayerX()][getPlayerY()] instanceof Bomb) && !(table[getPlayerX()][getPlayerY()] instanceof Brick)) {
            Platform.runLater(() -> {
                Entity object = new Bomb(getPlayerX(), getPlayerY(), Sprite.bomb.getFxImage, entities, bomb_size);
                entities.add(object);
            });
            Sound.place_bomb.play();
        }
    }

    @Override
    public void update() {
        bomber_life = life;
        if (hurt) {
            if (hurtTick == 0) {
                Sound.died.play();
            }
            if (hurtTick == 30) {
                if (life == 0) {
                    Game.gameState = Game.STATE.END;
                }
                hurt = false;
                hurtTick = 0;
                protection_time = 60 * 3 / 2;
                return;
            }
            chooseSprite();
            hurtTick++;
            return;
        }
        protection_time = Math.max(0, protection_time - 1);
        moving = false;
        bomberMoving();
        getItem();
        chooseSprite();
        if (keyListener.isPressed(KeyCode.SPACE)) placeBomb();
    }

    public int getPlayerX() {
        if (AI) {
            return (x + (1 * Sprite.SCALED_SIZE) / (2 * 1)) / Sprite.SCALED_SIZE;
        }
        return (x + (75 * Sprite.SCALED_SIZE) / (2 * 100)) / Sprite.SCALED_SIZE;
    }

    public int getPlayerY() {
        if (AI) {
            return (y + (1 * Sprite.SCALED_SIZE) / (2 * 1)) / Sprite.SCALED_SIZE;
        }
        return (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
    }

    public boolean isFlamePass() {
        return flamePass;
    }

    public boolean isProtectded() {
        return protection_time > 0;
    }

}
