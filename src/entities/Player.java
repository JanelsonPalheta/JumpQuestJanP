package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Player {
    private int x, y;
    private final int width = 28, height = 47;
    private int velocityY = 0;
    private boolean jumping = false;

    private BufferedImage[] moveRightImages = new BufferedImage[9];
    private BufferedImage[] moveLeftImages = new BufferedImage[9];
    private int currentFrame = 0;
    private boolean facingRight = true;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        loadImages();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }

    public boolean isJumping() {
        return jumping;
    }

    private void loadImages() {
        try {
            for (int i = 0; i < moveRightImages.length; i++) {
                moveRightImages[i] = ImageIO.read(new File("src/images/moveRight/move" + i + ".png"));
            }
            for (int i = 0; i < moveLeftImages.length; i++) {
                moveLeftImages[i] = ImageIO.read(new File("src/images/moveLeft/move" + i + ".png"));
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagens do jogador.");
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        BufferedImage currentImage = facingRight ? moveRightImages[currentFrame] : moveLeftImages[currentFrame];
        if (currentImage != null) {
            g.drawImage(currentImage, x, y, width, height, null);
        }
    }

    public void update(ArrayList<Platform> platforms) {
        velocityY += 1; // Gravidade
        if (velocityY > 10) velocityY = 10;
        y += velocityY;

        for (Platform platform : platforms) {
            if (isColliding(platform.getBounds())) {
                if (velocityY > 0) { // Apenas ajusta se estiver caindo
                    y = platform.getBounds().y - height; // Posição acima da plataforma
                    velocityY = 0;
                    jumping = false;
                    break;
                }
            }
        }
    }

    public void jump() {
        if (!jumping) {
            jumping = true;
            velocityY = -18;
        }
    }

    public void moveLeft() {
        x -= 5;
        facingRight = false;
        updateAnimation();
    }

    public void moveRight() {
        x += 5;
        facingRight = true;
        updateAnimation();
    }

    private void updateAnimation() {
        currentFrame = (currentFrame + 1) % 9;
    }

    private boolean isColliding(Rectangle rect) {
        return new Rectangle(x, y, width, height).intersects(rect);
    }
}
