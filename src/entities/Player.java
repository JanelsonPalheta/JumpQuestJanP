package entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Classe Player com sobrecarga do metodo update
public class Player {
    private int x, y;
    private final int width = 47, height = 80;
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

    public int getX() {
        return x;
    }

    public void loadImages() {
        try {
            for (int i = 0; i < moveRightImages.length; i++) {
                File moveRightFile = new File("src/images/moveRight/move" + i + ".png");
                if (!moveRightFile.exists()) {
                    System.out.println("Arquivo não encontrado: " + moveRightFile.getAbsolutePath());
                } else {
                    moveRightImages[i] = ImageIO.read(moveRightFile);
                }
            }
            for (int i = 0; i < moveLeftImages.length; i++) {
                File moveLeftFile = new File("src/images/moveLeft/move" + i + ".png");
                if (!moveLeftFile.exists()) {
                    System.out.println("Arquivo não encontrado: " + moveLeftFile.getAbsolutePath());
                } else {
                    moveLeftImages[i] = ImageIO.read(moveLeftFile);
                }
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
        } else {
            System.out.println("Erro: imagem atual é nula.");
        }
    }

    private int animationDelay = 5; // Intervalo entre os quadros
    private int animationCounter = 0;

    public void updateAnimation() {
        animationCounter++;
        if (animationCounter >= animationDelay) {
            currentFrame = (currentFrame + 1) % 9;
            animationCounter = 0;
        }
    }

    // Sobrecarregado: metodo update sem argumentos
    public void update() {
        if (!jumping) {
            velocityY = 0; // Evita ajustes contínuos ao pousar
        } else {
            velocityY += 1;
            if (velocityY > 10) velocityY = 10;
        }
        y += velocityY;
    }

    // Sobrecarregado: metodo update com plataforma (Rectangle)
    public void update(Rectangle ground) {
        velocityY += 1; // Aplicação da gravidade
        if (velocityY > 10) velocityY = 10;
        y += velocityY;

        // Verifica colisão com o chão
        Rectangle playerBounds = new Rectangle(x, y + height, width, 1);
        if (playerBounds.intersects(ground)) {
            if (velocityY > 0) { // Apenas ajusta a posição se estiver caindo
                y = ground.y - height;
                velocityY = 0;
                jumping = false; // Atualiza o estado de pulo apenas ao pousar
            }
        } else {
            jumping = true; // Atualiza o estado ao sair do chão
        }
    }

    public void jump() {
        if (!jumping) { // Permite pular apenas se não estiver no ar
            jumping = true;
            velocityY = -15;
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

    public void resetVelocity() {
        velocityY = 0;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public boolean isColliding(Rectangle rect) {
        boolean collision = new Rectangle(x, y, width, height).intersects(rect);
        if (collision) {
            System.out.println("Colisão detectada! Player: (" + x + ", " + y + "), Plataforma: " + rect);
        }
        return collision;
    }

}
