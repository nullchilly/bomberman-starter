package entities.tiles;

import core.Game;
import entities.Entity;
import graphics.Sprite;
import javafx.application.Platform;
import javafx.scene.image.Image;

import java.util.List;

public class Brick extends Entity {
    private boolean exploded = false;
    private final List<Entity> entities;

    public Brick(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    private int animate = 0;

    public void brickExploded() {
        Sprite sprite = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2, animate, 20);
        img = sprite.getFxImage;
        animate++;
        if (animate == 10) {
            Platform.runLater(() -> {
                entities.remove(this);
                x /= Sprite.SCALED_SIZE;
                y /= Sprite.SCALED_SIZE;
                Game.table[x][y] = null;
                Entity hiddenItem = Game.hiddenTable[x][y];
                if (hiddenItem != null) {
                    Game.table[x][y] = hiddenItem;
                    entities.add(hiddenItem);
                }
            });
        }
    }

    public void setExploded() {
        exploded = true;
    }

    @Override
    public void update() {
        if (exploded) {
            brickExploded();
        }
    }
}