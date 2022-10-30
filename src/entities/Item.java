package entities;

import core.Sound;
import javafx.scene.image.Image;

import java.util.List;

public abstract class Item extends Entity {
    protected boolean pickedup = false;

    public Item(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    public void pick() {
        this.pickedup = true;
        (new Sound("collect_item.mp3")).play();
    }

    public boolean isPickedup() {
        return pickedup;
    }
}
