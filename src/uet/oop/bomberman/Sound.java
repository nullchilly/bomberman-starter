package uet.oop.bomberman;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Sound {
    private MediaPlayer mediaPlayer;

    public Sound(String s) {
        String musicFile = "res/sound/" + s;     // For example

        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }

    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }
}
