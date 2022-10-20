package uet.oop.bomberman.entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class FlameItem extends Entity {
    public boolean pickedup = false;
    public FlameItem(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    @Override
    public void update() {
        img = Sprite.powerup_flames.getFxImage();
        if (pickedup) {
            Platform.runLater(() -> {
                entities.remove(this);
            });
        }
    }
}
