package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import uet.oop.bomberman.KeyListener;

public class Bomber extends Entity {

    private KeyListener keyListener;
    public Bomber(int x, int y, KeyListener keyListener, Image img) {
        super( x, y, img);
        this.keyListener = keyListener;
    }

    @Override
    public void update() {
        if (keyListener.isPressed(KeyCode.D)) {
                x += 4;
        }
        if (keyListener.isPressed(KeyCode.A)) {
            x -= 4;
        }
        if (keyListener.isPressed(KeyCode.W)) {
            y -= 4;
        }
        if (keyListener.isPressed(KeyCode.S)) {
            y += 4;
        }
    }

    public void updateBomber() {

    }
}
