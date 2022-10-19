package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class FlameItem extends Item {
    public FlameItem(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img, entities);
    }

    @Override
    public void update() {
        img = Sprite.powerup_flames.getFxImage();
    }
}
