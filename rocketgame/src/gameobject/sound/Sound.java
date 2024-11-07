package gameobject.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    private final URL shoot;
    private final URL hit;
    private final URL destroy;

    public Sound() {
   // กำหนดไฟล์เสียงที่ต้องการใช้ โดยอ้างอิงจากตำแหน่งใน package gameobject.sound
        this.shoot = this.getClass().getClassLoader().getResource("gameobject/sound/shoot.wav");
        this.hit = this.getClass().getClassLoader().getResource("gameobject/sound/hit.wav");
        this.destroy = this.getClass().getClassLoader().getResource("gameobject/sound/destroy.wav");
    }

    // ฟังก์ชันเพื่อเล่นเสียงยิง
    public void soundShoot() {
        play(shoot);
    }

    // ฟังก์ชันเพื่อเล่นเสียงชน
    public void soundHit() {
        play(hit);
    }

    // ฟังก์ชันเพื่อเล่นเสียงทำลาย
    public void soundDestroy() {
        play(destroy);
    }

    // ฟังก์ชันในการเล่นไฟล์เสียง
    private void play(URL url) {
        try {
            // สร้าง AudioInputStream จาก URL ของไฟล์เสียง
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // กำหนด Clip สำหรับการเล่นเสียง
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            // เล่นเสียง
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
