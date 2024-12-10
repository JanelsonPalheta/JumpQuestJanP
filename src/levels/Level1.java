package levels;

import entities.Player;
import entities.Platform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Level1 extends JPanel implements Runnable, KeyListener {
    private final JFrame frame;
    private final Player player;
    private final ArrayList<Platform> platforms = new ArrayList<>();
    private boolean gameOver = false;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;
    private Image background1, background2, background3;

    public Level1(JFrame frame) {
        this.frame = frame;
        player = new Player(100, 460);

        // Adicionar plataformas fixas
        platforms.add(new Platform(200, 400, 100, 20));
        platforms.add(new Platform(400, 300, 100, 20));
        platforms.add(new Platform(0, 965, 1920, 20)); // Chão principal

        try {
            background1 = ImageIO.read(new File("src/images/background1.png"));
            background2 = ImageIO.read(new File("src/images/background2.png"));
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

        if (player.getY() > getHeight()) {
            gameOver = true;
            frame.getContentPane().removeAll();
            frame.add(new JLabel("Game Over!"));
            frame.revalidate();
            frame.repaint();
        }
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

        // Desenhar chão por cima das plataformas
        if (background2 != null) {
            g.drawImage(background2, 0, 580, panelWidth, 510, null);
        }

        // Desenhar o jogador
        player.draw(g);
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
}
