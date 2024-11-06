package gameobject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class Hprender {
    final HP hp;

    public Hprender(HP hp) {
        this.hp = hp;
    }

    protected void hprender(Graphics2D g2, Shape shape, double y) {
        if (hp.getNowhp() != hp.getMaxhp()) {
            double hpY = shape.getBounds().getY() - y - 10;
            g2.setColor(new Color(70, 70, 70));
            g2.fill(new Rectangle2D.Double(0, hpY, User.playersize, 2));  
            g2.setColor(new Color(253, 91, 91));
            double hpSize = hp.getNowhp() / hp.getMaxhp() * User.playersize;
            g2.fill(new Rectangle2D.Double(0, hpY, hpSize, 2));  
            g2.setColor(Color.BLACK);
            g2.draw(new Rectangle2D.Double(0, hpY, User.playersize, 2));  
        }
    }

    public boolean update(double cuthp) {
        hp.setNowhp(hp.getNowhp() - cuthp);
        return hp.getNowhp() > 0;
    }

    public double getHp() {
        return hp.getNowhp();
    }

    public void resetHp() {
        hp.setNowhp(hp.getMaxhp());
    }
}
