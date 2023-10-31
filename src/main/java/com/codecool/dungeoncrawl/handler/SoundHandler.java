package com.codecool.dungeoncrawl.handler;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundHandler {

    public final String STEP_SOUND = "step.wav";
    public final String ELIXIR_SOUND = "elixir.wav";
    public static final String FIGHT_SOUND = "fight.wav";
    public final String SWORD_SOUND = "sword.wav";
    public final String KEYS_SOUND = "keys.wav";
    public static final String CHEAT_SOUND = "/cheatOn.wav";


    public static Clip playSound(String fileName) {
        try {
            File wavFile = new File("src/main/resources/" + fileName);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(wavFile));
            clip.start();
            return clip;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
