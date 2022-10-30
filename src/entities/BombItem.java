package entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import graphics.Sprite;

import java.util.List;

import static core.Game.table;

public class BombItem extends Item {
    public BombItem(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img, entities);
    }
    @Override
    public void update() {
        img = Sprite.powerup_bombs.getFxImage();

        if (pickedup || died) {
            Platform.runLater(() -> {
                table[x/Sprite.SCALED_SIZE][y/Sprite.SCALED_SIZE] = null;
                entities.remove(this);
            });
        }
    }
}
