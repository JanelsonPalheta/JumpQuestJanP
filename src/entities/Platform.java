package entities;

import java.awt.*;

public class Platform {
    private int x, y; //posição da plataforma no eixo x e eixo y
    private int width, height; //largura e altura da plataforma

    //construtor que inicializa a plataforma com posição de tamanho
    public Platform (int x, int y, int width, int height){
        this.x = x; //posição x
        this.y = y; //posição y
        this.width = width; //largura
        this.height = height; //altura
    }

    //metodo para desenhar a plataforma na tela
    public void draw(Graphics g){
        g.setColor(Color.GRAY); //define a cor da plataforma como cinza
        g.fillRect(x,y,width,height);
    }

    //metodo para obter o retangulo da plataforma (usado para a colisão)
    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }


}
