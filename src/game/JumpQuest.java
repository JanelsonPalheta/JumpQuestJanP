package game;

import javax.swing.*;

public class JumpQuest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JumpQuest");
        frame.setSize(1920, 1080);  // Defina o tamanho da janela do jogo
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Defina a operação de fechar a janela

        // Inicializa o menu
        Menu menu = new Menu(frame);
        frame.add(menu);  // Adiciona o painel do menu à janela

        frame.setVisible(true);  // Exibe a janela
    }
}
