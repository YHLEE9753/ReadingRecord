package EffectiveJava.chapter2.item6.adpater;

public class MP3 implements  MediaPlayer{

    @Override
    public void play(String filename) {
        System.out.println("Playing MP3 File" + filename);
    }
}
