package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    public int x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    public int y;

    protected Image img;

    protected static enum Direction {
        L,
        R,
        U,
        D,
        OH,
        OV,
    };
    protected Direction direction = Direction.R;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity( int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }
    public abstract void update();

    public static Entity getEntity(int x, int y) {
        return BombermanGame.table[x][y];
    }

    public static boolean checkWall(int x, int y) {
        if (x < 0 || y < 0 || x >= Sprite.SCALED_SIZE * BombermanGame.WIDTH || y >= Sprite.SCALED_SIZE * BombermanGame.HEIGHT) {
            return false;
        }

        x /= Sprite.SCALED_SIZE;
        y /= Sprite.SCALED_SIZE;
        Entity cur = getEntity(x, y);
        return !(cur instanceof Wall || cur instanceof Brick);
    }
}
