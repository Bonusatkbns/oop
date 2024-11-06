/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameobject;

import java.awt.*;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 *
 * @author Bonus
 */
public class Effect {

    private final double x;
    private final double y;
    private final double maxdis;
    private final int maxsize;
    private final Color color;
    private final int totalEffect;
    private final float speed;
    private double curentdis;
    private ModelBoom booms[];
    private float alpha = 1f;

    public Effect(double x, double y,int totalEffect,int maxsize,double maxdis ,float speed , Color color) {
        this.x = x;
        this.y = y;
        this.totalEffect = totalEffect;
        this.maxsize = maxsize;
        this.maxdis = maxdis;
        this.speed = speed;
        this.color=color;
        createrandom();
    }

    private void createrandom() {
        booms = new ModelBoom[totalEffect];
        float per = 360f / totalEffect;
        Random ran = new Random();
        for (int i = 1; i <= totalEffect; i++) {
            int r = ran.nextInt((int) per) + 1;
            int boomSize = ran.nextInt(maxsize) + 1;
            float angle = i * per + r;
            booms[i - 1] = new ModelBoom(boomSize, angle);

        }

    }

    public void draw(Graphics2D g2) {
        AffineTransform olAffineTransform = g2.getTransform();
        Composite olComposite = g2.getComposite();
        g2.setColor(color);
        g2.translate(x, y);
        for (ModelBoom b : booms) {
            double bx = Math.cos(Math.toRadians(b.getAngle())) * curentdis;
            double by = Math.sin(Math.toRadians(b.getAngle())) * curentdis;
            double boomSize = b.getSize();
            double space = boomSize / 2;
            if (curentdis >= maxdis - (maxdis * 0.7f)) {
                alpha = (float) ((maxdis - curentdis) / (maxdis * 0.7f));
            }
            if (alpha > 1) {
                alpha = 1;

            } else if (alpha < 0) {
                alpha = 0;
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.fill(new Rectangle2D.Double(bx - space, by - space, boomSize, boomSize));
        }

        g2.setTransform(olAffineTransform);
        g2.setComposite(olComposite);

    }
    public void update(){
        curentdis+=speed;
        
    }
    public boolean check(){
        return curentdis<maxdis;
    }
}
