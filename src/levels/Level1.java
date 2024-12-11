package levels;

import entities.Player;
import entities.Platform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Level1 extends JPanel implements Runnable, KeyListener {
    private final JFrame frame;
    private final Player player;
    private final ArrayList<Platform> platforms = new ArrayList<>();
    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private boolean gameOver = false;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;
    private Image background1;
    private final Random random = new Random();

    public Level1(JFrame frame) {
        this.frame = frame;
        player = new Player(100, 460);

        // Adicionar plataformas fixas
        platforms.add(new Platform(200, 50, 100, 20));
        platforms.add(new Platform(600, 100, 100, 20));
        platforms.add(new Platform(900, 200, 100, 20));
        platforms.add(new Platform(150, 400, 100, 20));
        platforms.add(new Platform(250, 250, 100, 20));
        platforms.add(new Platform(300, 550, 100, 20));
        platforms.add(new Platform(500, 400, 100, 20));
        platforms.add(new Platform(0, 600, 1280, 20)); // Chão principal

        try {
            background1 = ImageIO.read(new File("src/images/background1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true);
        addKeyListener(this);

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (!gameOver) {
            updateGame();
            repaint();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        player.update(platforms);

        if (spacePressed && !player.isJumping()) {
            player.jump();
        }

        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();

        // Atualizar bombas
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            bomb.update();

            if (bomb.hasExploded()) {
                bombs.remove(i);
                i--;
                continue;
            }

            // Verificar colisão com jogador
            if (bomb.isCollidingWith(player.getBounds())) {
                gameOver = true;
                endGame();
                return;
            }
        }

        // Adicionar nova bomba aleatoriamente
        if (random.nextInt(100) < 2) { // 2% de chance a cada frame
            bombs.add(new Bomb(random.nextInt(getWidth() - 30), 0));
        }

        if (player.getY() > getHeight()) {
            gameOver = true;
            endGame();
        }
    }

    private void endGame() {
        frame.getContentPane().removeAll();
        frame.add(new JLabel("Game Over!", SwingConstants.CENTER));
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Desenhar fundo
        if (background1 != null) {
            g.drawImage(background1, 0, 0, panelWidth, panelHeight, null);
        }

        // Desenhar plataformas
        for (Platform platform : platforms) {
            platform.draw(g);
        }

        // Desenhar o jogador
        player.draw(g);

        // Desenhar bombas
        for (Bomb bomb : bombs) {
            bomb.draw(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = true;
            case KeyEvent.VK_RIGHT -> rightPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
            case KeyEvent.VK_SPACE -> spacePressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private static class Bomb {
        private final int x;
        private int y;
        private boolean exploded = false;
        private final Image bombImage;
        private final Image explosionImage;
        private int explosionTimer = 0;

        public Bomb(int x, int y) {
            this.x = x;
            this.y = y;
            try {
                bombImage = ImageIO.read(new File("src/images/bomb.png"));
                explosionImage = ImageIO.read(new File("src/images/explosion.gif"));
            } catch (IOException e) {
                throw new RuntimeException("Erro ao carregar imagens da bomba.", e);
            }
        }

        public void update() {
            if (exploded) {
                explosionTimer++;
                return;
            }

            y += 5; // Velocidade de queda

            if (y > 580) { // Altura do chão
                exploded = true;
            }
        }

        public void draw(Graphics g) {
            if (exploded) {
                g.drawImage(explosionImage, x - 20, 560, 60, 60, null);
            } else {
                g.drawImage(bombImage, x, y, 30, 50, null);
            }
        }

        public boolean hasExploded() {
            return exploded && explosionTimer > 30; // Duração da explosão
        }

        public boolean isCollidingWith(Rectangle rect) {
            if (!exploded) {
                return new Rectangle(x, y, 30, 50).intersects(rect);
            } else {
                return new Rectangle(x - 20, 560, 60, 60).intersects(rect);
            }
        }
    }
}
