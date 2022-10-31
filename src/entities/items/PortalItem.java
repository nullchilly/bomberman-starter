package entities.items;

import graphics.Sprite;
import javafx.scene.image.Image;

import static core.Game.table;

public class PortalItem extends Item {
    public PortalItem(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = this;
        img = Sprite.portal.getFxImage;
    }
}
