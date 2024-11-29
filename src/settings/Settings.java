package settings;

import game.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Settings extends JPanel {
    private JFrame frame; // Referência à janela principal
    private JLabel difficultyLabel; // Exibe a dificuldade selecionada
    private JButton soundButton; // Botão para ativar/desativar som
    private boolean soundOn = true; // Estado do som (ligado por padrão)

    public Settings(JFrame frame) {
        this.frame = frame;

        // Cria um rótulo para exibir a dificuldade selecionada
        difficultyLabel = new JLabel("Dificuldade: Fácil");
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Botões para selecionar a dificuldade
        JButton easyButton = new JButton("Fácil");
        JButton mediumButton = new JButton("Médio");
        JButton hardButton = new JButton("Difícil");

        // Define ações para os botões de dificuldade
        easyButton.addActionListener((ActionEvent e) -> difficultyLabel.setText("Dificuldade: Fácil"));
        mediumButton.addActionListener((ActionEvent e) -> difficultyLabel.setText("Dificuldade: Médio"));
        hardButton.addActionListener((ActionEvent e) -> difficultyLabel.setText("Dificuldade: Difícil"));

        // Botão para ativar/desativar som
        soundButton = new JButton("Som: Ligado");
        soundButton.addActionListener((ActionEvent e) -> toggleSound());

        // Botão para voltar ao menu principal
        JButton backButton = new JButton("Voltar");
        backButton.addActionListener((ActionEvent e) -> returnToMenu());

        // Define o layout e adiciona os componentes
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(difficultyLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(easyButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(mediumButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(hardButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(soundButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(backButton, gbc);
    }

    // Alterna o estado do som
    private void toggleSound() {
        soundOn = !soundOn; // Alterna entre ligado e desligado
        soundButton.setText(soundOn ? "Som: Ligado" : "Som: Desligado"); // Atualiza o texto do botão
    }

    // Retorna ao menu principal
    private void returnToMenu() {
        frame.getContentPane().removeAll(); // Remove o conteúdo atual
        frame.add(new Menu(frame)); // Adiciona o menu principal
        frame.revalidate(); // Atualiza o layout
        frame.repaint(); // Redesenha a janela
    }
}
