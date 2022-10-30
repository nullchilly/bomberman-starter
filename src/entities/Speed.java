package entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import graphics.Sprite;

import java.util.List;

import static core.Game.table;

public class Speed extends Entity {
    private boolean pickedup = false;
    public Speed(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    public void pick() {
        this.pickedup = true;
    }

    public boolean isPickedup() {
        return pickedup;
    }

    @Override
    public void update() {
        img = Sprite.powerup_speed.getFxImage();

        if (pickedup || died) {
            Platform.runLater(() -> {
                table[x/Sprite.SCALED_SIZE][y/Sprite.SCALED_SIZE] = null;
                entities.remove(this);
            });
        }
    }
}
