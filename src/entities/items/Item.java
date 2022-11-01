package entities.items;

import audio.Sound;
import entities.Entity;
import javafx.scene.image.Image;

import static core.Game.bgMusic;

public abstract class Item extends Entity {
    protected boolean pickedup = false;

    public Item(int x, int y, Image img) {
        super(x, y, img);
    }

    public void pick() {
        this.pickedup = true;
        Sound.collect_item.play();
        bgMusic.stop();
        bgMusic = Sound.powerup_get;
        bgMusic.loop();
    }

    public boolean isPickedup() {
        return pickedup;
    }
}
