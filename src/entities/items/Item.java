package entities.items;

import core.Sound;
import entities.character.Entity;
import javafx.scene.image.Image;

public abstract class Item extends Entity {
    protected boolean pickedup = false;

    public Item(int x, int y, Image img) {
        super(x, y, img);
    }

    public void pick() {
        this.pickedup = true;
        (new Sound("collect_item.mp3")).play();
    }

    public boolean isPickedup() {
        return pickedup;
    }
}
