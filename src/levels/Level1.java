package levels;

import entities.Player;
import entities.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Level1 extends JPanel implements Runnable, KeyListener {
    private final JFrame frame;
    private final Player player;
    private final ArrayList<Platform> platforms = new ArrayList<>();
    private boolean gameOver = false;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    public Level1(JFrame frame) {
        this.frame = frame;
        player = new Player(100, 421);
        platforms.add(new Platform(0, 500, 800, 20));

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
                Thread.sleep(15); // Intervalo consistente para manter 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        boolean isOnPlatform = false;

        for (Platform platform : platforms) {
            if (player.isColliding(platform.getBounds())) {
                Rectangle platformBounds = platform.getBounds();
                System.out.println("Verificando colisão: Player em (" + player.getX() + ", " + player.getY() + "), Plataforma em " + platformBounds);

                System.out.println("Player Bottom: " + (player.getY() + player.getHeight()) + ", Platform Top: " + platformBounds.y);
                System.out.println("VelocityY antes do ajuste: " + player.getVelocityY());

                if (player.getY() + player.getHeight() >= platformBounds.y - 10
                        && player.getY() + player.getHeight() <= platformBounds.y + 10) {
                    System.out.println("Colisão válida! Ajustando posição do jogador.");
                    player.setY(platformBounds.y - player.getHeight());
                    player.resetVelocity();
                    isOnPlatform = true;
                    break; // Encerra o loop para evitar sobrescrita do estado
                } else {
                    System.out.println("Colisão detectada, mas inválida para ajuste.");
                }
            }
        }

        if (!isOnPlatform) {
            player.update(); // Só aplica gravidade se não estiver sobre uma plataforma
        } else {
            player.setJumping(false); // Evita estado inconsistente de pulo
        }

        // Permite pulo apenas se estiver sobre uma plataforma
        if (spacePressed && isOnPlatform) {
            System.out.println("Comando de pulo detectado.");
            player.jump();
        }

        // Movimentos laterais
        if (leftPressed) {
            System.out.println("Movendo para a esquerda.");
            player.moveLeft();
        }
        if (rightPressed) {
            System.out.println("Movendo para a direita.");
            player.moveRight();
        }

        // Verifica se o jogador caiu da tela
        if (player.getY() > getHeight()) {
            System.out.println("Game Over! Jogador caiu da tela.");
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
        player.draw(g);

        for (Platform platform : platforms) {
            platform.draw(g);
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
}
