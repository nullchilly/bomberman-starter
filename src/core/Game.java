package core;

import entities.character.Balloom;
import entities.character.Bomber;
import entities.character.Oneal;
import entities.items.BombItem;
import entities.items.FlameItem;
import entities.items.PortalItem;
import entities.items.SpeedItem;
import entities.tiles.Brick;
import entities.tiles.Grass;
import entities.tiles.Wall;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import entities.*;
import graphics.Sprite;

//import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Game extends Application {
    public static long FPS_GAME = 1000/60;
    public static int WIDTH;
    public static int HEIGHT;
    public static Bomber bomber;
    public static int level = 1;
    public static int cnt_enemy = 0;
    private GraphicsContext gc;
    private Canvas canvas;
    public List<Entity> entities = new ArrayList<>();
    public List<Entity> flames = new ArrayList<>();
    public List<Entity> stillObjects = new ArrayList<>();
    public static Entity[][] table;
    public static Entity[][] hiddenTable;
//    public Entity bomberman;
    public List<Sound> bgMusic = new ArrayList<>();
    public enum STATE {
        MENU, SINGLE, MULTIPLAYER, PAUSE, END, NEXT_LV
    }

    public static STATE gameState = STATE.MENU;

    public boolean isEnd = false;

    public void setup(Stage stage, int level) {
        for (Sound sound : bgMusic) {
            sound.stop();
        }
        entities = new ArrayList<>();
        flames = new ArrayList<>();
        stillObjects = new ArrayList<>();
        bgMusic = new ArrayList<>();
        Sound main = new Sound("main.mp3");
        main.play();
        bgMusic.add(main);
//        int level = 1;
        File file = new File(System.getProperty("user.dir") + "/res/levels/Level" + level + ".txt");
        try {
            Scanner scanner = new Scanner(file);
            scanner.nextInt(); // level
            int height = scanner.nextInt();
            int width = scanner.nextInt();
            HEIGHT = height;
            WIDTH = width;
//            System.out.println(WIDTH + " " + HEIGHT);
            table = new Entity[WIDTH][HEIGHT];
            hiddenTable = new Entity[WIDTH][HEIGHT];
            // Tao Canvas
            canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
            gc = canvas.getGraphicsContext2D();

            // Tao root container
            Group root = new Group();
            root.getChildren().add(canvas);

            // Tao scene
            Scene scene = new Scene(root);
            KeyListener keyListener = new KeyListener(scene);

            // Them scene vao stage
            stage.setScene(scene);
            stage.show();
            cnt_enemy = 0;
            scanner.nextLine();
            for (int i = 0; i < height; i++) {
                String cur = scanner.nextLine();
                for (int j = 0; j < width; j++) {
                    Entity stillObject = null;
                    Entity object = null;
                    Entity hiddenObject = null;
                    stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage));
                    switch (cur.charAt(j)) {
                        // Tiles:
                        case '#':
                            stillObject = new Wall(j, i, Sprite.wall.getFxImage);
                            break;
                        case '*':
                            object = new Brick(j, i, Sprite.brick.getFxImage, entities);
                            break;
                        // Character:
                        case 'p':
                            object = new Bomber(j, i, Sprite.player_right.getFxImage, keyListener, entities);
                            bomber = (Bomber) object;
                            break;
                        case '1':
                            object = new Balloom(j, i, Sprite.balloom_right1.getFxImage, entities);
                            cnt_enemy++;
                            break;
                        case '2':
                            object = new Oneal(j, i, Sprite.oneal_right1.getFxImage, entities);
                            cnt_enemy++;
                            break;
                        // Items:
                        case 'f':
                            hiddenObject = new FlameItem(j, i, Sprite.powerup_flames.getFxImage, entities);
                            break;
                        case 's':
                            hiddenObject = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage, entities);
                            break;
                        case 'b':
                            hiddenObject = new BombItem(j, i, Sprite.powerup_bombs.getFxImage, entities);
                            break;
                        case 'x':
                            hiddenObject = new PortalItem(j, i, Sprite.portal.getFxImage, entities);
                            break;
                    }
                    if (stillObject != null) {
                        stillObjects.add(stillObject);
                        table[j][i] = stillObject;
                    } else if (object != null) {
                        entities.add(object);
                        table[j][i] = object;
                    } else if (hiddenObject != null) {
                        object = new Brick(j, i, Sprite.brick.getFxImage, entities);
                        entities.add(object);
                        table[j][i] = object;
                        hiddenTable[j][i] = hiddenObject;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Application.launch(Game.class);
    }

    public void single(Stage stage) {
        setup(stage, level);
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                update();
                render(stage);
                long frameTime = (now - lastUpdate) / 1000000;
                if (frameTime < FPS_GAME) {
                    try {
                        Thread.sleep(FPS_GAME - frameTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
//                    else {
//                        System.out.println(frameTime == 0 ? "" : 1000/frameTime);
//                    }
                lastUpdate = System.nanoTime();
            }
        };

        timer.start();
    }

    public void menu(Stage stage) {
        for (Sound sound : bgMusic) {
            sound.stop();
        }
        Sound start = new Sound("title.mp3");
        start.play();
        bgMusic.add(start);
        //Creating a Button
        Button button = new Button();
        button.setText("Single player");
        button.setTranslateX(Sprite.SCALED_SIZE * 15);
        button.setTranslateY(Sprite.SCALED_SIZE * 10);
        button.setOnAction(event -> {
            gameState = STATE.SINGLE;
            start(stage);
        });
        //Setting the stage
        Group root = new Group(button);
        Scene scene = new Scene(root, Sprite.SCALED_SIZE * 30, Sprite.SCALED_SIZE * 20, Color.BLACK);
        stage.setTitle("Bomberman NES");
        stage.setScene(scene);
        stage.show();
    }

    public void end(Stage stage) {
        for (Sound sound : bgMusic) {
            sound.stop();
        }
        Sound died = new Sound("ending.mp3");
        died.play();
        bgMusic.add(died);
        Button button = new Button();
        button.setText("Replay");
        button.setTranslateX(Sprite.SCALED_SIZE * 15);
        button.setTranslateY(Sprite.SCALED_SIZE * 10);
        button.setOnAction(event -> {
            gameState = STATE.SINGLE;
            isEnd = false;
            setup(stage, level);
        });
        //Setting the stage
        Group root = new Group(button);
        Scene scene = new Scene(root, Sprite.SCALED_SIZE * 30, Sprite.SCALED_SIZE * 20, Color.BLACK);
        stage.setTitle("Bomberman NES");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage stage) {
        switch (gameState) {
            case MENU:
                menu(stage);
                break;

            case SINGLE:
                single(stage);
                break;

            case MULTIPLAYER:
                break;

            case PAUSE:
                break;

            case END:
                break;
            default:
                throw new IllegalArgumentException("Invalid game state");
        }
    }

    public void update() {
        entities.forEach(Entity::update);
//        if (cnt_enemy == 0) gameState = STATE.NEXT_LV;
    }

    public void render(Stage stage) {
        switch (gameState) {
            case SINGLE:
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                stillObjects.forEach(g -> g.render(gc));
                entities.forEach(g -> g.render(gc));
                bomber.render(gc);
                break;

            case MENU:
                break;

            case MULTIPLAYER:
                break;

            case PAUSE:
                break;
            case NEXT_LV:
                gameState = STATE.SINGLE;
                setup(stage, ++level);
                break;
            case END:
                if (!isEnd) {
                    end(stage);
                    isEnd = true;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid game state");
        }
    }
}
