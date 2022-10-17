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
    public static int animate = 0;
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;
    
    private GraphicsContext gc;
    private Canvas canvas;
    public List<Entity> entities = new ArrayList<>();
    public List<Entity> flames = new ArrayList<>();
    public List<Entity> stillObjects = new ArrayList<>();
    public static Entity[][] table = new Entity[WIDTH][HEIGHT];
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
        System.out.println(Sprite.width);
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
            @Override
            public void handle(long l) {
                fps();
                render();
                update();
            }
        };

        timer.start();

//        Timer bombTimer = new Timer();
//        bombTimer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                createBomb(scene);
//            }
//        }, 1000, 3000);

        createMap(1);
    }

    public void createMap(int level) {
        File file = new File(System.getProperty("user.dir") + "/res/levels/Level" + level + ".txt");
        try {
            Scanner scanner = new Scanner(file);
            int height = scanner.nextInt(); // level
            height = scanner.nextInt();
            int width = scanner.nextInt();
            System.out.println(height + " " + width);
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
                            break;
                        case '1':
                            object = new Balloom(j, i, Sprite.balloom_right1.getFxImage(), entities);
                            break;
                        case '2':
                            object = new Oneal(j, i, Sprite.oneal_right1.getFxImage(), entities);
                            break;
                        // Items:
                        case 's':
                            stillObject = new Speed(j, i, Sprite.powerup_speed.getFxImage());
                            break;
                    }
                    if (stillObject != null) {
                        stillObjects.add(stillObject);
                        table[j][i] = stillObject;
                    }
                    else if (object != null) {
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

//    public void createBomb(Scene scene) {
//        Entity object = new Bomb((bomberman.x + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, (bomberman.y + Sprite.SCALED_SIZE/2)/Sprite.SCALED_SIZE, Sprite.bomb.getFxImage());
//        entities.add(object);
////        Platform.runLater(() -> root.getChildren().add(e)) -- Fix different thread
//    }

    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
    }
}
