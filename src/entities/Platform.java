package entities;

import java.awt.*;

public class Platform {
    private int x, y;
    private int width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        // Converte Graphics para Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        // Define o nível de transparência (0.0 = totalmente transparente, 1.0 = totalmente opaco)
        float alpha = 0.5f; // Ajuste este valor para a transparência desejada
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Define a cor e desenha o retângulo
        g2d.setColor(Color.GRAY);
        g2d.fillRect(x, y, width, height);

        // Restaura o estado original do Composite
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
