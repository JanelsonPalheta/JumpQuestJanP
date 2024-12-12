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

import game.Menu;
import settings.GameSettings;

public class Level1 extends JPanel implements Runnable, KeyListener {
    private final JFrame frame;
    private final Player player;
    private final ArrayList<Platform> platforms = new ArrayList<>();
    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private Image[] collectibleFrames = new Image[6];
    private Point collectiblePosition;
    private int score = 0;
    private int itemsCollected = 0;
    private final Random random = new Random();

    private boolean gameOver = false;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;
    private static Image background1;

    // Variáveis para controle de dificuldade
    private int bombSpawnChance;
    private int bombSpeed;

    private int lives = 3; // Número de vidas iniciais
    private boolean invulnerable = false;
    private long invulnerabilityEndTime;
    private Image lifeIcon;

    public Level1(JFrame frame) {
        this.frame = frame;
        player = new Player(100, 460);

        // Configuração da dificuldade
        adjustDifficulty();

        // Adicionar plataformas fixas com imagens
        platforms.add(new Platform(200, 50, 100, 20, "src/images/platforms/platform4.png"));
        platforms.add(new Platform(600, 100, 100, 20, "src/images/platforms/platform4.png"));
        platforms.add(new Platform(900, 200, 100, 20, "src/images/platforms/platform4.png"));
        platforms.add(new Platform(150, 400, 100, 20, "src/images/platforms/platform4.png"));
        platforms.add(new Platform(250, 250, 100, 20, "src/images/platforms/platform4.png"));
        platforms.add(new Platform(300, 550, 100, 20, "src/images/platforms/platform4.png"));
        platforms.add(new Platform(500, 400, 100, 20, "src/images/platforms/platform4.png"));
        platforms.add(new Platform(400, 100, 100, 20, "src/images/platforms/platform4.png"));  // Altura ajustada
        platforms.add(new Platform(700, 200, 100, 20, "src/images/platforms/platform4.png"));  // Altura ajustada
        platforms.add(new Platform(1000, 300, 100, 20, "src/images/platforms/platform4.png")); // Altura ajustada
        platforms.add(new Platform(200, 450, 100, 20, "src/images/platforms/platform4.png"));  // Altura ajustada
        platforms.add(new Platform(800, 500, 100, 20, "src/images/platforms/platform4.png"));  // Altura ajustada
        platforms.add(new Platform(600, 550, 100, 20, "src/images/platforms/platform4.png"));  // Altura ajustada
        platforms.add(new Platform(50, 300, 100, 20, "src/images/platforms/platform4.png"));   // Altura ajustada
        platforms.add(new Platform(950, 250, 100, 20, "src/images/platforms/platform4.png"));
        platforms.add(new Platform(0, 630, 1280, 200)); // Chão principal sem imagem

        try {
            background1 = ImageIO.read(new File("src/images/background1.png"))
                    .getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < collectibleFrames.length; i++) {
                collectibleFrames[i] = ImageIO.read(new File("src/images/item/item" + (i + 1) + ".png"))
                        .getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar imagens do item coletável.", e);
        }

        try {
            lifeIcon = ImageIO.read(new File("src/images/life.png"))
                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar ícone de vida.", e);
        }
        spawnCollectible();


        setFocusable(true);
        addKeyListener(this);

        new Thread(this).start();
    }

    private void spawnCollectible() {
        Platform platform = platforms.get(random.nextInt(platforms.size() - 1)); // Ignorar o chão
        collectiblePosition = new Point(platform.getX() + platform.getWidth() / 2 - 15, platform.getY() - 30);
    }

    private void adjustDifficulty() {
        String difficulty = GameSettings.getInstance().getDifficulty();
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
        // Atualiza o jogador com base nas plataformas
        player.update(platforms);

        // Verifica e aplica comandos de movimento
        if (spacePressed && !player.isJumping()) {
            player.jump();
        }
        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();

        // Atualiza as bombas
        updateBombs();

        // Gerencia o estado de invulnerabilidade
        if (invulnerable && System.currentTimeMillis() > invulnerabilityEndTime) {
            invulnerable = false;
        }

        // Verifica coleta de itens
        checkCollectibleCollision();

        // Adiciona novas bombas com chance definida pela dificuldade
        if (random.nextInt(100) < bombSpawnChance) {
            bombs.add(new Bomb(random.nextInt(getWidth() - 30), 0));
        }

        // Verifica se o jogador caiu fora da tela
        if (player.getY() > getHeight()) {
            gameOver = true;
            endGame();
        }
    }

    // Atualiza as bombas e verifica colisões
    private void updateBombs() {
        Iterator<Bomb> iterator = bombs.iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            bomb.update(bombSpeed);

            if (bomb.hasExploded()) {
                iterator.remove();
                continue;
            }

            // Verifica colisão com o jogador
            if (!invulnerable && bomb.isCollidingWith(player.getBounds())) {
                handlePlayerHitByBomb();
                if (gameOver) return; // Encerra o jogo se vidas acabarem
            }
        }
    }

    // Gerencia o evento de colisão entre jogador e bomba
    private void handlePlayerHitByBomb() {
        lives--;
        invulnerable = true;
        invulnerabilityEndTime = System.currentTimeMillis() + 3000; // 3 segundos de invulnerabilidade

        if (lives <= 0) {
            gameOver = true;
            endGame();
        }
    }

    // Verifica colisão com itens coletáveis
    private void checkCollectibleCollision() {
        if (collectiblePosition != null &&
                new Rectangle(collectiblePosition.x, collectiblePosition.y, 30, 30)
                        .intersects(player.getBounds())) {

            itemsCollected++;
            score += (itemsCollected % 10 == 0) ? 50 : 10; // 50 pontos a cada 10 itens, caso contrário, 10 pontos
            spawnCollectible();
        }
    }


    private void endGame() {
        frame.getContentPane().removeAll();

        JLabel gameOverLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        frame.add(gameOverLabel);
        frame.revalidate();
        frame.repaint();

        // Cria um timer para retornar ao menu principal após 5 segundos
        Timer timer = new Timer(5000, e -> showMainMenu());
        timer.setRepeats(false); // Garante que o timer execute apenas uma vez
        timer.start();
    }

    // Metodo para exibir o menu principal
    private void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new Menu(frame)); // Adiciona a tela de menu principal
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

        // Desenhar item coletável
        if (collectiblePosition != null) {
            g2.drawImage(collectibleFrames[(int) (System.currentTimeMillis() / 100 % collectibleFrames.length)],
                    collectiblePosition.x, collectiblePosition.y, null);
        }

        // Desenhar vidas restantes (corações)
        for (int i = 0; i < lives; i++) {
            g2.drawImage(lifeIcon, 1100 + i * 35, 10, null);
        }

        // Efeito de piscar durante invulnerabilidade
        if (invulnerable && (System.currentTimeMillis() / 200 % 2 == 0)) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            player.draw(g2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else {
            player.draw(g2);
        }

        // Desenhar o score
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Score: " + score, 20, 40);

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
