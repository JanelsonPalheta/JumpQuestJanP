package entities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Player {
    private int x,y; //posição do jogador no eixo x e no eixo y
    private int width = 30, height = 30; // dimnções do jogador
    private int velocityY = 0; // velocidade vertical para simular a gravidade
    private boolean jumping = false; //indica se o jogador esta pulando

    //construtor que inicializar a posição do jogador
    public Player(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw (Graphics g){
        g.setColor(Color.RED); // define cor do jogador como vermelho
        g.fillRect(x,y,width,height); //desenha o retangulo representando o jogador
    }

    public void update(){
        velocityY += 1;// aplica a gravidade encrementa a velocidade vertical
        y += velocityY; // atuaiza a posição y do jogador
    }

    public void jump(){
        if(!jumping){ // se nao estiver pulando
            jumping = true; // define que esta pulando
            velocityY = -15; // define a velocidade vertical negativa para o pulo
        }
    }


    //metodo para mover o jogador para esquerda
    public void moveleft(){
        x-=5; //decrementa a posição x para mover a esquerda
    }

    // metodo para mover o jogador para direita
    public void moverRight(){
        x +=5;
    }

    //metodo getters para obter a posição e dimensões do jogador
    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    //metodo para definir a posição Y do jogador (usado ao colidir com as plataformas)
    public void setY (int y) {
        this.y = y;
    }

    //metodo para resetar a velocidade vertical e estado de pulo
    public void resetVelocity(){
        velocityY = 0;
        jumping = false;
    }

    //metodo para verificar colisão com um retangulo (plataformas, obstaculos)
    public boolean isColliding(Rectangle rect){
        Rectangle playerRect = new Rectangle(x,y,width,height);//cria um retangulo representando o jogador

        return playerRect.intersects(rect); //verificar se ha intersecção

    }



}