package entities.player;

import core.Game;
import entities.Entity;
import input.KeyListener;
import audio.Sound;
import entities.bombs.Bomb;
import entities.items.*;
import entities.tiles.Brick;
import graphics.Sprite;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import static core.Game.*;

public class Bomber extends Player {

    public int STEP = Sprite.STEP;
    private boolean moving = false;
    private int bombQuantity;
    private boolean flamePass = false;
    private boolean wallPass = false;
    private int bomb_size = 1;
    private boolean died = false;
    public int protection_time = 0;
    private int hurtTick = 0;
    private final KeyListener keyListener;
    private static int bomber_life;

    public Bomber(int x, int y, Image img, KeyListener keyListener) {
        super(x, y, img);
        this.keyListener = keyListener;
        bombQuantity = 1;
        life = 2;
    }

    public void setDied() {
        this.died = true;
        (new Sound("died.wav")).play();
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

    public void bomberMoving() {
        int px = (x + (75 * Sprite.SCALED_SIZE) / (2 * 100)) / Sprite.SCALED_SIZE;
        int py = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
        Entity cur = table[px][py];
        table[px][py] = null;
        if (!wallPass) {
            if (keyListener.isPressed(KeyCode.D)) {
                direction = Direction.R;
                if (checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + 3) && checkWall(x + STEP + Sprite.SCALED_SIZE - 12, y + Sprite.SCALED_SIZE - 3)) {
                    x += STEP;
                    moving = true;
                }
            }
            if (keyListener.isPressed(KeyCode.A)) {
                direction = Direction.L;
                if (checkWall(x - STEP, y + 3) && checkWall(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                    x -= STEP;
                    moving = true;
                }
            }
            if (keyListener.isPressed(KeyCode.W)) {
                direction = Direction.U;
                if (checkWall(x, y - STEP + 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y - STEP + 3)) {
                    y -= STEP;
                    moving = true;
                }
            }
            if (keyListener.isPressed(KeyCode.S)) {
                direction = Direction.D;
                if (checkWall(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkWall(x + Sprite.SCALED_SIZE - 12, y + STEP + Sprite.SCALED_SIZE - 3)) {
                    y += STEP;
                    moving = true;
                }
            }
        } else {
            if (keyListener.isPressed(KeyCode.D)) {
                direction = Direction.R;
                if (checkBrick(x + STEP + Sprite.SCALED_SIZE - 12, y + 3) && checkBrick(x + STEP + Sprite.SCALED_SIZE - 12, y + Sprite.SCALED_SIZE - 3)) {
                    x += STEP;
                    moving = true;
                }
            }
            if (keyListener.isPressed(KeyCode.A)) {
                direction = Direction.L;
                if (checkBrick(x - STEP, y + 3) && checkBrick(x - STEP, y + Sprite.SCALED_SIZE - 3)) {
                    x -= STEP;
                    moving = true;
                }
            }
            if (keyListener.isPressed(KeyCode.W)) {
                direction = Direction.U;
                if (checkBrick(x, y - STEP + 3) && checkBrick(x + Sprite.SCALED_SIZE - 12, y - STEP + 3)) {
                    y -= STEP;
                    moving = true;
                }
            }
            if (keyListener.isPressed(KeyCode.S)) {
                direction = Direction.D;
                if (checkBrick(x, y + STEP + Sprite.SCALED_SIZE - 3) && checkBrick(x + Sprite.SCALED_SIZE - 12, y + STEP + Sprite.SCALED_SIZE - 3)) {
                    y += STEP;
                    moving = true;
                }
            }
        }
        if (moving && animate % 15 == 0) {
            (new Sound("move.wav")).play();
        }
        table[px][py] = cur;
    }

    public void placeBomb() {
        if (keyListener.isPressed(KeyCode.SPACE) && Bomb.cnt < bombQuantity && !(table[getPlayerX()][getPlayerY()] instanceof Bomb) && !(table[getPlayerX()][getPlayerY()] instanceof Brick)) {
            Platform.runLater(() -> {
                Entity object = new Bomb(getPlayerX(), getPlayerY(), Sprite.bomb.getFxImage, entities, bomb_size);
                entities.add(object);
            });
            Sound bomb = new Sound("place_bomb.wav");
            bomb.play();
        }
    }

    @Override
    public void update() {
        bomber_life = life;
        if (hurt) {
            Sound sound;
            if (hurtTick == 0) {
                sound = new Sound("died.wav");
                sound.play();
            }
            if (hurtTick == 30) {
                if (life == 0) {
                    Game.gameState = Game.STATE.END;
                }
                hurt = false;
                hurtTick = 0;
                protection_time = 60 * 2;
                return;
            }
            chooseSprite();
            hurtTick++;
            return;
        }
        protection_time = Math.max(0, protection_time - 1);
        moving = false;
        getItem();
        bomberMoving();
        chooseSprite();
        placeBomb();
    }

    public int getPlayerX() {
        return (x + (75 * Sprite.SCALED_SIZE) / (2 * 100)) / Sprite.SCALED_SIZE;
    }

    public int getPlayerY() {
        return (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
    }

    public boolean isFlamePass() {
        return flamePass;
    }

    public boolean isProtectded() {
        return protection_time > 0;
    }

    public static int getBomberLife() {
        return bomber_life;
    }
}
