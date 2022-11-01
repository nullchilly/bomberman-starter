package entities.tiles;

import entities.Entity;
import graphics.Sprite;
import javafx.scene.image.Image;

import static core.Game.table;

public class Wall extends Entity {

    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        table[x / Sprite.SCALED_SIZE][y / Sprite.SCALED_SIZE] = this;
    }
}
