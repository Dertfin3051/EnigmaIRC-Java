package ru.dfhub.eirc.util;

import ru.dfhub.eirc.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Notification sound
 */
public class NotificationSound {

    public static void play() {
        try {
            InputStream audioFileInputStream = Main.class.getClassLoader().getResourceAsStream("notification.wav");
            BufferedInputStream audioBufferedInputStream = new BufferedInputStream(audioFileInputStream);
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioBufferedInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
