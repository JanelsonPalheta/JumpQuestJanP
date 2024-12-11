package levels;

import entities.Player;
import entities.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;
import settings.GameSettings;

public class Level1 extends JPanel implements Runnable, KeyListener {
    private final JFrame frame;
    private final Player player;
    private final ArrayList<Platform> platforms = new ArrayList<>();
    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private boolean gameOver = false;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;
    private static Image background1;
    private final Random random = new Random();

    // Variáveis para controle de dificuldade
    private int bombSpawnChance;
    private int bombSpeed;

    public Level1(JFrame frame) {
        this.frame = frame;
        player = new Player(100, 460);

        // Configuração da dificuldade
        adjustDifficulty();

        // Adicionar plataformas fixas
        platforms.add(new Platform(200, 50, 100, 20));
        platforms.add(new Platform(600, 100, 100, 20));
        platforms.add(new Platform(900, 200, 100, 20));
        platforms.add(new Platform(150, 400, 100, 20));
        platforms.add(new Platform(250, 250, 100, 20));
        platforms.add(new Platform(300, 550, 100, 20));
        platforms.add(new Platform(500, 400, 100, 20));
        platforms.add(new Platform(0, 630, 1280, 20)); // Chão principal

        try {
            background1 = ImageIO.read(new File("src/images/background1.png"))
                    .getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true);
        addKeyListener(this);

        new Thread(this).start();
    }

    private void adjustDifficulty() {
        String difficulty = GameSettings.getInstance().getDifficulty();
        System.out.println("Ajustando dificuldade: " + difficulty); // Log
        switch (difficulty) {
            case "Fácil":
                bombSpawnChance = 1; // 1% de chance a cada frame
                bombSpeed = 3;       // Velocidade reduzida
                break;
            case "Médio":
                bombSpawnChance = 3; // 3% de chance a cada frame
                bombSpeed = 5;       // Velocidade moderada
                break;
            case "Difícil":
                bombSpawnChance = 6; // 6% de chance a cada frame
                bombSpeed = 7;       // Velocidade aumentada
                break;
            default:
                bombSpawnChance = 2;
                bombSpeed = 4;
        }
        System.out.println("Configurações aplicadas: bombSpawnChance=" + bombSpawnChance + ", bombSpeed=" + bombSpeed); // Log
    }

    @Override
    public void run() {
        while (!gameOver) {
            long startTime = System.currentTimeMillis();

            updateGame();
            repaint();

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = Math.max(16 - elapsedTime, 5); // 60 FPS

            try {
                Thread.sleep(sleepTime);
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

        System.out.println("Dificuldade atual: " + GameSettings.getInstance().getDifficulty());

        // Atualizar bombas
        Iterator<Bomb> iterator = bombs.iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            bomb.update(bombSpeed);

            if (bomb.hasExploded()) {
                iterator.remove();
                continue;
            }

            // Verificar colisão com jogador
            if (Math.abs(bomb.getX() - player.getX()) < 50 && Math.abs(bomb.getY() - player.getY()) < 50) {
                if (bomb.isCollidingWith(player.getBounds())) {
                    gameOver = true;
                    endGame();
                    return;
                }
            }
        }

        // Adicionar nova bomba aleatoriamente
        if (random.nextInt(100) < bombSpawnChance) { // Chance definida pela dificuldade
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
        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();

        // Desenhar fundo
        if (background1 != null) {
            g2.drawImage(background1, 0, 0, getWidth(), getHeight(), null);
        }

        // Desenhar plataformas
        for (Platform platform : platforms) {
            platform.draw(g2);
        }

        // Desenhar o jogador
        player.draw(g2);

        // Desenhar bombas
        for (Bomb bomb : bombs) {
            bomb.draw(g2);
        }

        g.drawImage(buffer, 0, 0, null);
        g2.dispose();
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
        private static final Image bombImage;
        private static final Image[] explosionFrames;
        private int explosionTimer = 0;
        private int animationFrame = 0;

        static {
            try {
                bombImage = ImageIO.read(new File("src/images/bomb.png"))
                        .getScaledInstance(30, 50, Image.SCALE_SMOOTH);

                explosionFrames = new Image[6];
                for (int i = 0; i < explosionFrames.length; i++) {
                    explosionFrames[i] = ImageIO.read(new File("src/images/explosions/explosion" + (i + 1) + ".png"))
                            .getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                }
            } catch (IOException e) {
                throw new RuntimeException("Erro ao carregar imagens da bomba.", e);
            }
        }

        public Bomb(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void update(int speed) {
            if (exploded) {
                explosionTimer++;
                if (explosionTimer % 5 == 0) { // Alterar frame a cada 5 ticks
                    animationFrame++;
                }
                return;
            }

            y += speed; // Velocidade de queda ajustável

            if (y > 580) { // Altura do chão
                exploded = true;
            }
        }

        public void draw(Graphics g) {
            if (exploded) {
                if (animationFrame < explosionFrames.length) {
                    g.drawImage(explosionFrames[animationFrame], x - 20, 560, null);
                }
            } else {
                g.drawImage(bombImage, x, y, null);
            }
        }

        public boolean hasExploded() {
            return exploded && animationFrame >= explosionFrames.length; // Fim da animação
        }

        public boolean isCollidingWith(Rectangle rect) {
            if (!exploded) {
                return new Rectangle(x, y, 30, 50).intersects(rect);
            } else {
                return new Rectangle(x - 20, 560, 60, 60).intersects(rect);
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
