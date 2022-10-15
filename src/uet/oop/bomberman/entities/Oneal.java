package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class Oneal extends Entity {
    private int animate = 0;

    public Oneal(int x, int y, Image img) {
        super(x, y, img);
    }

    public void getImg(boolean exploded) {
        Sprite sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, animate, 20);
        img = sprite.getFxImage();
    }

    @Override
    public void update() {
        animate++;
        if (animate > 1000000) animate = 0;
        getImg(true);
    }
}
