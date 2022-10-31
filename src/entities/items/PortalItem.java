package entities.items;

import entities.Entity;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;

public class PortalItem extends Item {
    public PortalItem(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img, entities);
    }

    @Override
    public void update() {
        img = Sprite.portal.getFxImage;
    }
}
