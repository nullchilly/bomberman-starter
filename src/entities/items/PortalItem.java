package entities.items;

import graphics.Sprite;
import javafx.scene.image.Image;

public class PortalItem extends Item {
    public PortalItem(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        img = Sprite.portal.getFxImage;
    }
}
