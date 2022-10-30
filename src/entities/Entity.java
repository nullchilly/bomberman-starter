package entities;

import core.Game;
import entities.tiles.Brick;
import entities.tiles.Wall;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected int x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected int y;

    protected Sprite sprite;

    protected int animate = 0;

    protected int die_time = 0;

    protected boolean died = false;

    protected Image img;

    protected List<Entity> entities = new ArrayList<>();

    protected enum Direction { L, R, U, D, OH, OV }
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
        return Game.table[x][y];
    }

    public static boolean checkWall(int x, int y) {
        if (x < 0 || y < 0 || x > Sprite.SCALED_SIZE * Game.WIDTH || y > Sprite.SCALED_SIZE * Game.HEIGHT) {
            return false;
        }

        x /= Sprite.SCALED_SIZE;
        y /= Sprite.SCALED_SIZE;
        Entity cur = getEntity(x, y);
        return !(cur instanceof Wall) &&  !(cur instanceof Brick) && !(cur instanceof Bomb);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDied() {
        this.died = true;
    }
    public void dieEnemy(Sprite sprite) {
        die_time++;
        img = sprite.getFxImage();
        if (die_time == 20) {
            Platform.runLater(() -> {
                entities.remove(this);
                Game.table[(x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE][(y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE] = null;
            });
        }
    }

    public Sprite getSprite() {
        return this.sprite;
    }

}