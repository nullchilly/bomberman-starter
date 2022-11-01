package entities.items;

import core.Game;
import audio.Sound;
import entities.Entity;
import javafx.scene.image.Image;

public abstract class Item extends Entity {
    protected boolean pickedup = false;

    public Item(int x, int y, Image img) {
        super(x, y, img);
    }

    public void pick() {
        this.pickedup = true;
        Sound collect_sound = new Sound("collect_item.wav");
        collect_sound.play();
        for (Sound sound : Game.bgMusic) {
            sound.stop();
        }
        Sound newBg = new Sound("powerup_get.wav");
        newBg.loop();
        Game.bgMusic.add(newBg);
    }

    public boolean isPickedup() {
        return pickedup;
    }
}
