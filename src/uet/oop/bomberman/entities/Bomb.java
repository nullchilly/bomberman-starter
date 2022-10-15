package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class Bomb extends Entity {
    private int animate = 0;

    public Bomb(int x, int y, Image img) {
        super(x, y, img);
    }

    public void getImg(boolean exploded) {
        if (exploded == false) {
            Sprite sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 20);
            img = sprite.getFxImage();
        }
        else {
            Sprite sprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, animate, 20);
            img = sprite.getFxImage();
        }
    }

    @Override
    public void update() {
        animate++;
        if (animate > 1000000) animate = 0;
        getImg(true);
    }
}