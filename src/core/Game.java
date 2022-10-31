package core;

import entities.character.Entity;
import entities.character.*;
import entities.items.*;
import entities.tiles.Brick;
import entities.tiles.Grass;
import entities.tiles.Wall;
import graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game extends Application {
    public static long FPS_GAME = 1000 / 60;
    public static int WIDTH;
    public static int HEIGHT;
    public static Bomber bomber;
    public static int level = 1;
//    public static int cnt_enemy = 0;
    public static Entity[][] table;
    public static Entity[][] hiddenTable;
    public static STATE gameState = STATE.MENU;
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> enemies= new ArrayList<>();
    public List<Entity> flames = new ArrayList<>();
    public List<Entity> stillObjects = new ArrayList<>();
    //    public Entity bomberman;
    public static List<Sound> bgMusic = new ArrayList<>();
    public boolean isEnd = false;
    private GraphicsContext gc;
    private Canvas canvas;

    public static void main(String[] args) {
        Application.launch(Game.class);
    }

    public void setup(Stage stage, int level) {
        for (Sound sound : bgMusic) {
            sound.stop();
        }
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        flames = new ArrayList<>();
        stillObjects = new ArrayList<>();
        bgMusic = new ArrayList<>();
        Sound main = new Sound("main_bgm.wav");
        main.loop();
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
//            cnt_enemy = 0;
            scanner.nextLine();
            for (int i = 0; i < height; i++) {
                String cur = scanner.nextLine();
                for (int j = 0; j < width; j++) {
                    Entity stillObject = null;
                    Entity object = null;
                    Entity hiddenObject = null;
                    Entity enemy = null;
                    stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage));
                    switch (cur.charAt(j)) {
                        // Tiles:
                        case '#':
                            stillObject = new Wall(j, i, Sprite.wall.getFxImage);
                            break;
                        case '*':
                            object = new Brick(j, i, Sprite.brick.getFxImage);
                            break;
                        // Character:
                        case 'p':
                            object = new Bomber(j, i, Sprite.player_right.getFxImage, keyListener);
                            bomber = (Bomber) object;
                            break;
                        case '1':
                            enemy = new Balloom(j, i, Sprite.balloom_right1.getFxImage);
//                            cnt_enemy++;
                            break;
                        case '2':
                            enemy = new Oneal(j, i, Sprite.oneal_right1.getFxImage);
//                            cnt_enemy++;
                            break;
                        case '3':
                            enemy = new Doll(j, i, Sprite.doll_right1.getFxImage);
//                            cnt_enemy++;
                            break;
                        case '4':
                            enemy = new Kondoria(j, i, Sprite.doll_right1.getFxImage);
//                            cnt_enemy++;
                            break;
                        // Items:
                        case 'f':
                            hiddenObject = new FlameItem(j, i, Sprite.powerup_flames.getFxImage);
                            break;
                        case 's':
                            hiddenObject = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage);
                            break;
                        case 'b':
                            hiddenObject = new BombItem(j, i, Sprite.powerup_bombs.getFxImage);
                            break;
                        case 'x':
                            hiddenObject = new PortalItem(j, i, Sprite.portal.getFxImage);
                            break;
                        case 'm':
                            hiddenObject = new FlamePassItem(j, i, Sprite.powerup_flamepass.getFxImage);
                            break;
                        case 'w':
                            hiddenObject = new WallPassItem(j, i, Sprite.powerup_wallpass.getFxImage);
                            break;
                    }
                    if (stillObject != null) {
                        stillObjects.add(stillObject);
                        table[j][i] = stillObject;
                    } else if (object != null) {
                        entities.add(object);
                        table[j][i] = object;
                    } else if (hiddenObject != null) {
                        object = new Brick(j, i, Sprite.brick.getFxImage);
                        entities.add(object);
                        table[j][i] = object;
                        hiddenTable[j][i] = hiddenObject;
                    } else if (enemy != null) {
                        enemies.add(enemy);
                        table[j][i] = enemy;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
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
        Sound start = new Sound("title_screen.wav");
        start.loop();
        bgMusic.add(start);
        //Creating a Button
        Button button = new Button();
//        button.setText("Single player");
        button.setPrefSize(160, 60);
        button.setTranslateX(Sprite.SCALED_SIZE * 30 / 2 - 80);
        button.setTranslateY(Sprite.SCALED_SIZE * 15 / 2 + 30);
        InputStream stream = null;
        try {
            stream = new FileInputStream("res/start.png");
        } catch (Exception e) {
            e.getMessage();
        }
        Image img = new Image(stream);
        ImageView view = new ImageView();
        view.setImage(img);
//        view.setFitHeight(70);
//        view.setPreserveRatio(true);
        button.setGraphic(view);
        button.setOnAction(event -> {
            gameState = STATE.SINGLE;
            start(stage);
        });

        try {
            stream = new FileInputStream("res/menu.jpeg");
        } catch (Exception e) {
            e.getMessage();
        }
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitHeight(Sprite.SCALED_SIZE * 15);
        imageView.setFitWidth(Sprite.SCALED_SIZE * 30);
//        imageView.setPreserveRatio(true);
        //Setting the Scene object
        Group root = new Group(imageView);
//        Scene scene = new Scene(root, 595, 370);
//        Group root = new Group(button);
        root.getChildren().add(button);
        Scene scene = new Scene(root, Sprite.SCALED_SIZE * 30, Sprite.SCALED_SIZE * 15, Color.BLACK);
        stage.setTitle("Bomberman NES");
        stage.setScene(scene);
        stage.show();
    }

    public void end(Stage stage) {
        for (Sound sound : bgMusic) {
            sound.stop();
        }
        Sound died = new Sound("ending.wav");
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
        enemies.forEach(Entity::update);
//        if (cnt_enemy == 0) gameState = STATE.NEXT_LV;
    }

    public void render(Stage stage) {
        switch (gameState) {
            case SINGLE:
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                stillObjects.forEach(g -> g.render(gc));
                entities.forEach(g -> g.render(gc));
                enemies.forEach(g -> g.render(gc));
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

    public enum STATE {
        MENU, SINGLE, MULTIPLAYER, PAUSE, END, NEXT_LV
    }
}
