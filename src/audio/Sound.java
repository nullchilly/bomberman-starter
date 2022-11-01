package audio;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Sound extends JFrame {

    public static Sound main_bgm = new Sound("main_bgm.wav");
    public static Sound title_screen = new Sound("title_screen.wav");
    public static Sound collect_item = new Sound("collect_item.wav");
    public static Sound powerup_get = new Sound("powerup_get.wav");
    public static Sound place_bomb = new Sound("place_bomb.wav");
    public static Sound died = new Sound("died.wav");
    public static Sound move = new Sound("move.wav");
    public static Sound explosion = new Sound("explosion.wav");
    public static Sound win = new Sound("stage_clear.wav");
    public static Sound ending = new Sound("miss.wav");

    private Clip clip;
    String soundFile;

    public Sound(String soundFile) {
        this.soundFile = soundFile;
        try {
            File f = new File("res/sound/" + soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
