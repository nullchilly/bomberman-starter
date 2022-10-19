package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Item extends Entity {

    public Item(int x, int y, Image img, List<Entity> entities) {
        super(x, y, img);
        this.entities = entities;
    }

    @Override
    public void update() {
    }
}
