package entities.items;

import entities.Entity;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;

public class PortalItem extends Item {
    public PortalItem(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        img = Sprite.portal.getFxImage;
    }
}
