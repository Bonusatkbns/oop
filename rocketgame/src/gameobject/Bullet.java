/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameobject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
/**
 *
 * @author Bonus
 */
public class Bullet {

    private double x;
    private double y;
    private final Shape shape;
    private Color color;  // เพิ่มตัวแปรสี
    private final float angle;
    private double size;
    private float speed = 1f;

    // เพิ่มพารามิเตอร์สีใน constructor
    public Bullet(double x, double y, float angle, double size, float speed, Color color) {
        x += User.playersize / 2 - (size / 2);
        y += User.playersize / 2 - (size / 2);
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.size = size;
        this.speed = speed;
        this.color = color;  // กำหนดสีให้กับกระสุน
        shape = new Ellipse2D.Double(0, 0, size, size);
    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }
    
    public boolean check(int w, int h){
        if(x <= -size || y < -size || x > w || y > h){
            return false;
        } else {
            return true;
        }
    }

    public void drawBullet(Graphics2D g2) {
        AffineTransform oldTransform = g2.getTransform();
        
        g2.setColor(color);  // ใช้สีที่กำหนด
        g2.translate(x, y);
        g2.fill(shape);
        g2.setTransform(oldTransform);
    }

    public Shape getShape() {
        return new Area(new Ellipse2D.Double(x, y, size, size));
    }

    public float getAngle() {
        return angle;
    }

    public double getY() {
        return y;
    }

    public double getX(){
        return x;
    }

    public double getSize(){
        return size;
    }

    public double getCenterX(){
        return x + size / 2;
    }

    public double getCenterY(){
        return y + size / 2;
    }
}

