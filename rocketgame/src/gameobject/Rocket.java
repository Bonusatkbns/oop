/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameobject;

import static gameobject.User.playersize;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.ImageIcon;

/**
 *
 * @author Bonus
 */
public class Rocket extends Hprender {

    public Rocket() {
        super(new HP(100, 100));
        this.image = new ImageIcon(getClass().getResource("/gameimage/enermy1.png")).getImage();
        Path2D p = new Path2D.Double();
        p.moveTo(0, ROCKET_SIZE / 2);
        p.lineTo(15, 10);
        p.lineTo(ROCKET_SIZE - 5, 13);
        p.lineTo(ROCKET_SIZE + 10, ROCKET_SIZE / 2);
        p.lineTo(ROCKET_SIZE - 5, ROCKET_SIZE - 13);
        p.lineTo(15, ROCKET_SIZE - 10);
        rocArea = new Area(p);
    }

    public static final double ROCKET_SIZE = 80;
    private double x;
    private double y;
    private float speed = 0.3f; // ความเร็วเริ่มต้น
    private float angle = 0;
    private final Image image;
    private final Area rocArea;

    // ตัวแปรเพื่อเก็บคะแนนที่ใช้ในการปรับความเร็ว
    private int currentStage = 1;

    public void setHp(double maxHp, double nowHp) {
        super.hp.setHP(maxHp, nowHp);
    }

    public void Move(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        // อัพเดตตำแหน่งของ Rocket ด้วยค่าความเร็วที่ปรับแล้ว
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
        
    }

    // ปรับความเร็วตาม stage
    public void updateSpeed(int stage) {
        if (stage != currentStage) {
            currentStage = stage;
            speed = 0.3f + currentStage * 0.1f; // เพิ่มความเร็วเมื่อ stage เพิ่มขึ้น
        }
    }

    public void Angle(float angle) {
        if (angle < 0) {
            angle = 359;
        } else if (angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    public void draw(Graphics2D g2) {
        AffineTransform oldAffineTransform = g2.getTransform();
        g2.translate(x, y);
        AffineTransform tran = new AffineTransform();
        tran.rotate(Math.toRadians(angle), ROCKET_SIZE / 2, ROCKET_SIZE / 2);
        g2.drawImage(image, tran, null);
        Area shape = getArea();
        hprender(g2, shape, y);
        g2.setTransform(oldAffineTransform);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public Area getArea() {
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        afx.rotate(Math.toRadians(angle), ROCKET_SIZE / 2, ROCKET_SIZE / 2);
        return new Area(afx.createTransformedShape(rocArea));
    }

    public boolean check(int w, int h) {
          return x >= -ROCKET_SIZE / 2 && x <= w && y >= -ROCKET_SIZE / 2 && y <= h;
    }
}


