package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class BombermanGame extends Application {
    public static int time = 0;
    public static long FPS_GAME = 1000/60;
    public static int animate = 0;
    public static int WIDTH;
    public static int HEIGHT;
    public static Bomber bomber;
    
    private GraphicsContext gc;
    private Canvas canvas;
    public List<Entity> entities = new ArrayList<>();
    public List<Entity> flames = new ArrayList<>();
    public List<Entity> stillObjects = new ArrayList<>();
    public static Entity[][] table;
    private KeyListener keyListener;
    public Entity bomberman;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    public void fps() {
        try {
            Thread.sleep(10);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void start(Stage stage) {
        int level = 1;
        File file = new File(System.getProperty("user.dir") + "/res/levels/Level" + level + ".txt");
        try {
            Scanner scanner = new Scanner(file);
            int height = scanner.nextInt(); // level
            height = scanner.nextInt();
            int width = scanner.nextInt();
            HEIGHT = height;
            WIDTH = width;
//            System.out.println(WIDTH + " " + HEIGHT);
            table = new Entity[WIDTH][HEIGHT];
            // Tao Canvas
            canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
            gc = canvas.getGraphicsContext2D();

            // Tao root container
            Group root = new Group();
            root.getChildren().add(canvas);

            // Tao scene
            Scene scene = new Scene(root);
            keyListener = new KeyListener(scene);

            // Them scene vao stage
            stage.setScene(scene);
            stage.show();

            AnimationTimer timer = new AnimationTimer() {
                private long lastUpdate = 0;
                private long frameTime = 0;
                @Override
                public void handle(long now) {
                    render();
                    update();
                    frameTime = (long)(now - lastUpdate)/1000000;
//                    System.out.println(frameTime + " " + FPS_GAME);
                    if (frameTime < FPS_GAME) {
                        try {
//                            System.out.println(frameTime + " " + (long)FPS_GAME );
                            Thread.sleep(FPS_GAME - frameTime);
//                            Thread.sleep(0);

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
//                    fps();
                    lastUpdate = System.nanoTime();
                }
            };

            timer.start();

            scanner.nextLine();
            for (int i = 0; i < height; i++) {
                String cur = scanner.nextLine();
                for (int j = 0; j < width; j++) {
//                    System.out.println(i + " " + j);
                    Entity stillObject = null;
                    Entity object = null;
                    stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                    switch (cur.charAt(j)) {
                        // Tiles:
                        case '#':
                            stillObject = new Wall(j, i, Sprite.wall.getFxImage());
                            break;
                        case '*':
                            object = new Brick(j, i, Sprite.brick.getFxImage(), entities);
                            break;
                        case 'x':
                            stillObject = new Portal(j, i, Sprite.portal.getFxImage());
                            break;
                        // Character:
                        case 'p':
                            object = new Bomber(j, i, Sprite.player_right.getFxImage(), keyListener, entities);
                            bomber = (Bomber) object;
                            break;
                        case '1':
                            object = new Balloom(j, i, Sprite.balloom_right1.getFxImage(), entities);
                            break;
                        case '2':
                            object = new Oneal(j, i, Sprite.oneal_right1.getFxImage(), entities);
                            break;
                        // Items:
                        case 'f':
                            object = new FlameItem(j, i, Sprite.powerup_flames.getFxImage(), entities);
                            break;
                        case 's':
                            stillObject = new Speed(j, i, Sprite.powerup_speed.getFxImage());
                            break;
                    }
                    if (stillObject != null) {
                        stillObjects.add(stillObject);
                        table[j][i] = stillObject;
                    } else if (object != null) {
                        entities.add(object);
                        table[j][i] = object;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        bomber.render(gc);
    }
}
