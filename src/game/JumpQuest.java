package game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import levels.Level1;  // Certifique-se de que a importação da classe Level1 esteja correta

public class JumpQuest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JumpQuest");
        frame.setSize(800, 600);  // Defina o tamanho da janela do jogo
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Defina a operação de fechar a janela

        // Inicializa o menu
        Menu menu = new Menu(frame);
        frame.add(menu);  // Adiciona o painel do menu à janela

        frame.setVisible(true);  // Exibe a janela
    }
}
