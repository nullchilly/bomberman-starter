package entities.items;

import graphics.Sprite;
import javafx.application.Platform;
import javafx.scene.image.Image;

import static core.Game.table;
import static core.Game.entities;

public class BombItem extends Item {
    public BombItem(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        img = Sprite.powerup_bombs.getFxImage;
        table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = this;
        if (pickedup || died) {
            Platform.runLater(() -> {
                table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = null;
                entities.remove(this);
            });
        }
    }
}
