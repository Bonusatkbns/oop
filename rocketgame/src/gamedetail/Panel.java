package gamedetail;

import gameobject.Bullet;
import gameobject.Effect;
import gameobject.Rocket;
import gameobject.User;
import gameobject.sound.Sound;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.List;
import java.util.Random;

public class Panel extends JComponent {

    private int timeLeft = 60;
    private Timer timer;
    private boolean gamePaused = false;
    private Graphics2D g2;
    private BufferedImage image;
    private BufferedImage backgroundImage;
    private int w;
    private int h;
    private Thread thread;
    private boolean gameOver = false;
    private boolean backgroundChanged = false;
    private boolean start = true;
    private final int FPS = 60;
    private final int Time = 1000000000 / FPS;
    private int timeshot;
    private Sound sound;

    private User user;
    private Keyboard key;
    private List<Bullet> bullets;
    private List<Rocket> rockets;
    private List<Effect> effects;
    private int score = 0;
    private List<String> backgroundImageFiles = new ArrayList<>();

    public void randomizeBackground() {
        backgroundImageFiles.clear();
        backgroundImageFiles.add("Bgspace.png");
        backgroundImageFiles.add("ExK_p7wVoAg_Rij (1).png");
        backgroundImageFiles.add("space1.png");
        backgroundImageFiles.add("space2.png");
        backgroundImageFiles.add("space 3.png");
        if (!backgroundImageFiles.isEmpty()) {
            Random random = new Random();
            String randomBackgroundFile = backgroundImageFiles.get(random.nextInt(backgroundImageFiles.size()));
            try {
                backgroundImage = ImageIO.read(getClass().getResource("/gameimage/" + randomBackgroundFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {

        w = getWidth();
        h = getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        randomizeBackground();

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                } else {
                    gameOver = true;
                    gamePaused = true;
                    resetGame();
                }
            }
        });
        timer.start();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    long startTime = System.nanoTime();
                    drawBg();
                    drawGame();
                    render();
                    long time = System.nanoTime() - startTime;
                    if (time < Time) {
                        long sleep = (Time - time) / 1000000;
                        sleep(sleep);
                    }
                }
            }
        });

        ObjectGame();
        Control();
        Shot();
        thread.start();
    }

    private void addRocket() {
        Random ran = new Random();

        int rocketsToAdd = (score / 20) + 1;

        for (int i = 0; i < rocketsToAdd; i++) {
            int locationY = ran.nextInt(h - 50) + 25;
            Rocket rocket = new Rocket();
            rocket.Move(0, locationY);
            rocket.Angle(0);
            rockets.add(rocket);

            int locationY2 = ran.nextInt(h - 50) + 25;
            Rocket rocket2 = new Rocket();
            rocket2.Move(w, locationY2);
            rocket2.Angle(180);
            rockets.add(rocket2);
        }
    }

    private void ObjectGame() {
        sound = new Sound();

        user = new User();
        user.Move(150, 150);
        rockets = new ArrayList<>();
        effects = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    addRocket();
                    sleep(3000);
                }
            }
        }).start();
    }

    private void resetGame() {
        rockets.clear();
        bullets.clear();
        user.Move(150, 150);
        user.reset();
        score = 0;
        timeLeft = 60; //
        gameOver = true;
        timer.start();
    }

    private void Control() {
        key = new Keyboard();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_left(true);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_right(true);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(true);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(true);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (gameOver) {
                        resetGame();
                        gameOver = false;
                        gamePaused = false; // รีเซ็ตสถานะหยุดเกม
                    } else if (!gamePaused) {
                        key.setKey_enter(true); // ใช้ Enter สำหรับการกระทำในเกมที่ไม่หยุด
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gamePaused = !gamePaused;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_left(false);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_right(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(false);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(false);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(false);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(false);
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                long lastShotTimeK = 0;
                long lastShotTimeJ = 0;
                while (start) {
                    if (gamePaused) {
                        sleep(100);
                        continue;
                    }

                    if (user.isAlive()) {
                        float angle = user.getAngle();
                        if (key.isKey_left()) {
                            angle -= s;
                        }
                        if (key.isKey_right()) {
                            angle += s;
                        }
                        long currentTime = System.currentTimeMillis();

                        if (key.isKey_k()) {
                            if (currentTime - lastShotTimeK >= 2500) {
                                bullets.add(new Bullet(user.getX(), user.getY(), user.getAngle(), 50, 3f, new Color(0x7482728)));  // กระสุนยิงตรงเป็นสีม่วง
                                bullets.add(new Bullet(user.getX(), user.getY(), user.getAngle() - 8, 40, 3f, new Color(0x7e4e60))); // ยิงไปทางซ้าย
                                bullets.add(new Bullet(user.getX(), user.getY(), user.getAngle() + 8, 40, 3f, new Color(0xb287a3))); // ยิงไปทางขวา
                                sound.soundShoot();
                                lastShotTimeK = currentTime;
                            }

                        }

                        if (key.isKey_j()) {
                            if (currentTime - lastShotTimeJ >= 70) {

                                bullets.add(new Bullet(user.getX(), user.getY(), user.getAngle() - 5, 10, 5.5f, new Color(0xBDCFB5))); // ยิงไปทางซ้าย สี #BDCFB5
                                bullets.add(new Bullet(user.getX(), user.getY(), user.getAngle() + 5, 10, 5.5f, new Color(0xC0F8D1))); // ยิงไปทางขวา สี #C0F8D1
                                sound.soundShoot();
                                lastShotTimeJ = currentTime;
                            }
                        }

                        if (key.isKey_space()) {
                            user.speedup();
                        } else {
                            user.speedown();
                        }
                        user.update(w, h);
                        user.Angle(angle);
                    } else {
                        if (key.isKey_enter()) {
                            resetGame();
                        }
                    }

                    for (int i = rockets.size() - 1; i >= 0; i--) {
                        Rocket rocket = rockets.get(i);
                        if (rocket != null) {
                            rocket.update();
                            if (!rocket.check(w, h)) {
                                rockets.remove(i);
                            } else {
                                if (user.isAlive()) {
                                    checkplayer(rocket);
                                }
                            }
                        }
                    }
                    sleep(2);
                }
            }
        }).start();
    }

    private void Shot() {
        bullets = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    for (int i = 0; i < bullets.size(); i++) {
                        Bullet bullet = bullets.get(i);
                        if (bullet != null) {
                            bullet.update();
                            checkbullet(bullet);
                            if (!bullet.check(w, h)) {
                                bullets.remove(bullet);
                            }
                        } else {
                            bullets.remove(bullet);
                        }
                    }
                    for (int i = 0; i < effects.size(); i++) {
                        Effect effect = effects.get(i);
                        if (effect != null) {
                            effect.update();
                            if (!effect.check()) {
                                effects.remove(effect);

                            }

                        } else {
                            effects.remove(effect);
                        }
                    }
                    sleep(5);
                }
            }
        }).start();
    }

    private void checkbullet(Bullet bullet) {
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            if (rocket != null) {
                Area area = new Area(bullet.getShape());
                area.intersect(rocket.getArea());
                if (!area.isEmpty()) {
                    effects.add(new Effect(bullet.getCenterX(), bullet.getCenterY(), 3, 5, 60, 0.5f, new Color(148, 0, 211)));
                    if (!rocket.update(bullet.getSize())) {
                        score += 5;
                        rockets.remove(rocket);
                        sound.soundDestroy();
                        double x = rocket.getX() + Rocket.ROCKET_SIZE / 2;
                        double y = rocket.getY() + Rocket.ROCKET_SIZE / 2;
                        effects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(0, 0, 205)));
                        effects.add(new Effect(x, y, 5, 5, 75, 0.45f, new Color(0, 255, 127)));
                        effects.add(new Effect(x, y, 10, 5, 100, 0.3f, new Color(255, 215, 0)));
                        effects.add(new Effect(x, y, 10, 5, 100, 0.2f, new Color(255, 0, 0)));
                    }
                    {
                        sound.soundHit();
                    }

                    bullets.remove(bullet);
                }
            }
        }
    }

    private void checkplayer(Rocket rocket) {
        if (rocket != null) {
            Area area = new Area(user.getArea());
            area.intersect(rocket.getArea());
            if (!area.isEmpty()) {
                double rocketHp = rocket.getHp();
                if (!rocket.update(user.getHp())) {
                    rockets.remove(rocket);
                    sound.soundDestroy();
                    double x = user.getX() + user.playersize / 2;
                    double y = user.getY() + user.playersize / 2;
                    effects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(0, 0, 205)));
                    effects.add(new Effect(x, y, 5, 5, 75, 0.45f, new Color(0, 255, 127)));
                    effects.add(new Effect(x, y, 10, 5, 100, 0.3f, new Color(255, 215, 0)));
                    effects.add(new Effect(x, y, 10, 5, 100, 0.2f, new Color(255, 0, 0)));
                    score += 5;
                }
                if (!user.update(rocketHp)) {
                    user.setAlive(false);
                    sound.soundDestroy();
                    double x = rocket.getX() + Rocket.ROCKET_SIZE / 2;
                    double y = rocket.getY() + Rocket.ROCKET_SIZE / 2;
                    effects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(0, 0, 205)));
                    effects.add(new Effect(x, y, 5, 5, 75, 0.45f, new Color(0, 255, 127)));
                    effects.add(new Effect(x, y, 10, 5, 100, 0.3f, new Color(255, 215, 0)));
                    effects.add(new Effect(x, y, 10, 5, 100, 0.2f, new Color(255, 0, 0)));

                }

            }

        }
    }

    void drawBg() {
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }

    private int lastScore = 0;

    void drawGame() {
        if (user.isAlive() && timeLeft > 0) {
            user.draw(g2);
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet != null) {
                bullet.drawBullet(g2);
            }
        }
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            if (rocket != null) {
                rocket.draw(g2);
                rocket.update();
            }
        }
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            if (effect != null) {
                effect.draw(g2);
            }
        }
        int stage = (score / 20) + 1;

        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
        g2.drawString("SCORE : " + score, 5, 30);
        g2.drawString("STAGE: " + stage, 5, 55);
        g2.drawString("TIME LEFT: " + timeLeft + "s", w - 150, 30);
        if (score / 20 > lastScore / 20) {
            randomizeBackground();
            lastScore = score;
            user.resetHp();
        }

        if (user.isAlive() && timeLeft > 0) {
            if (score >= 100) {
                String text = "You Win !! ";

                rockets.clear();
                user.reset();
                rockets.clear();
                g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r2 = fm.getStringBounds(text, g2);
                double textWidth = r2.getWidth();
                double textHeight = r2.getHeight();
                double x = (w - textWidth) / 2;
                double y = (h - textHeight) / 2;
                g2.drawString(text, (int) x, (int) y + fm.getAscent());
                timer.stop();
            }
        } else if (!user.isAlive() || timeLeft <= 0) {
            gameOver=true;
            String text = "GAME OVER";
            String textKey = "Press Enter to Continue ...";
            g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r2 = fm.getStringBounds(text, g2);
            double textWidth = r2.getWidth();
            double textHeight = r2.getHeight();
            double x = (w - textWidth) / 2;
            double y = (h - textHeight) / 2;
            g2.drawString(text, (int) x, (int) y + fm.getAscent());

            g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
            fm = g2.getFontMetrics();
            r2 = fm.getStringBounds(textKey, g2);
            textWidth = r2.getWidth();
            textHeight = r2.getHeight();
            x = (w - textWidth) / 2;
            y = (h - textHeight) / 2;
            g2.drawString(textKey, (int) x, (int) y + fm.getAscent() + 50);
            timer.stop();
        }

    }

    void render() {
        Graphics2D g = (Graphics2D) getGraphics();
        if (g != null && image != null) {
            g.drawImage(image, 0, 0, null);
            g.dispose();
        }
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
    }
}
