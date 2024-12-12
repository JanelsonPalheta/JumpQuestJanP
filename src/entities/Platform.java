package entities;

import java.awt.*;

public class Platform {
    private int x, y;
    private int width, height;
    private Image image; // Adicionado para suportar imagens

    public Platform(int x, int y, int width, int height) {
        this(x, y, width, height, null);
    }

    public Platform(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Carregar a imagem apenas se o caminho for fornecido
        if (imagePath != null && !imagePath.isEmpty()) {
            this.image = Toolkit.getDefaultToolkit().getImage(imagePath);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (image != null) {
            // Desenha a imagem caso exista
            g2d.drawImage(image, x, y, width, height, null);
        } else {
            // Define o nível de transparência (0.0 = totalmente transparente, 1.0 = totalmente opaco)
            float alpha = 0.0f; // Transparência para plataformas sólidas
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Define a cor e desenha o retângulo
            g2d.setColor(Color.GRAY);
            g2d.fillRect(x, y, width, height);

            // Restaura o estado original do Composite
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
