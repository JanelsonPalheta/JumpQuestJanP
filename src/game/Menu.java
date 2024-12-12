package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Classe que representa a tela de menu inicial
public class Menu extends JPanel {
    private JFrame frame;

    // Construtor que aceita um JFrame
    public Menu(JFrame frame) {
        this.frame = frame;

        // Botão para iniciar o jogo
        JButton startButton = new JButton("Iniciar Jogo");
        startButton.addActionListener((ActionEvent e) -> startGame());

        // Botão para abrir as configurações
        JButton settingsButton = new JButton("Configurações");
        settingsButton.addActionListener((ActionEvent e) -> openSettings());

        // Botão para sair do jogo
        JButton exitButton = new JButton("Sair");
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        // Configuração do layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(startButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(settingsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    // Metodo para iniciar o jogo
    private void startGame() {
        frame.getContentPane().removeAll(); // Remove o conteúdo atual
        frame.add(new levels.Level1(frame)); // Adiciona o primeiro nível do jogo
        frame.revalidate(); // Atualiza o layout
        frame.repaint(); // Redesenha a janela
        frame.getContentPane().getComponent(0).requestFocusInWindow();
    }

    // Metodo para abrir as configurações
    private void openSettings() {
        frame.getContentPane().removeAll(); // Remove o conteúdo atual
        frame.add(new settings.Settings(frame)); // Adiciona a tela de configurações
        frame.revalidate(); // Atualiza o layout
        frame.repaint(); // Redesenha a janela
    }
}
