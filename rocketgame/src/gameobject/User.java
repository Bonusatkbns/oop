/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameobject;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.ImageIcon;

/**
 *
 * @author Bonus
 */
public class User extends Hprender{

    public User() {
        super(new HP(400,400));
        this.image = new ImageIcon(getClass().getResource("/gameimage/rocket1.png")).getImage();
        this.imagespeed = new ImageIcon(getClass().getResource("/gameimage/rocket2.png")).getImage();
        Path2D p = new Path2D.Double();
        p.moveTo(0, 15);
        p.lineTo(20, 5);
        p.lineTo(playersize+15,playersize/2);
        p.lineTo(20, playersize-5);
        p.lineTo(0, playersize-15);
        playerShape = new Area(p);
    }

    public static final double playersize = 100;
    private double x, y;
    private float angle = 0f;
    private final float MaX_SPEED = 1f;
    private float speed = 0f;
    private boolean speedup;
    private final Area playerShape;
    
    
    private boolean alive = true;

    private final Image image;
    private final Image imagespeed;

    public void Move(double x, double y) {
        this.x = x;
        this.y = y;

    }

    public void update(int frameWidth, int frameHeight) {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;

        if (x < 0) {
            x = 0;
        } else if (x + playersize > frameWidth) {
            x = frameWidth - playersize;
        }

        if (y < 0) {
            y = 0;
        } else if (y + playersize > frameHeight) {
            y = frameHeight - playersize;
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

        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.rotate(Math.toRadians(angle), playersize / 2, playersize / 2);

        g2.setTransform(transform);
        g2.drawImage(speedup ? imagespeed : image, 0, 0, (int) playersize, (int) playersize, null);
        hprender(g2, getArea(),y);
        g2.setTransform(oldAffineTransform);
        
     //   g2.setColor(new Color(12, 172,84));
      //  g2.draw(getArea());
       // g2.draw(getArea().getBounds());
    }
    public Area getArea() {
        AffineTransform afx = new AffineTransform();
         afx.translate(x ,y);
        afx.rotate(Math.toRadians(angle), playersize / 2, playersize / 2);
        return new Area(afx.createTransformedShape(playerShape));

    }    

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void speedup() {
        speedup = true;
        if (speed > MaX_SPEED) {
            speed = MaX_SPEED;
        } else {
            speed += 0.005f;
        }
    }

    public void speedown() {
        speedup = false;
        if (speed <= 0) {
            speed = 0;
        } else {
            speed -= 0.005f;
        }
    }
    public boolean isAlive(){
        return alive;
        
    }
    public void setAlive(boolean alive){
        this.alive=alive;
    }
    public void reset(){
        alive=true;
        resetHp();
        angle=0;
        speed=0;
    }

 
}
