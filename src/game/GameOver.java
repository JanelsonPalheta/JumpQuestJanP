package game; // Pacote principal do jogo

import javax.swing.*; // Biblioteca para botões e layout
import java.awt.*; // Biblioteca para layouts e gráficos
import java.awt.event.ActionEvent; // Para capturar cliques em botões

// Classe que implementa a tela de "Game Over"
public class GameOver extends JPanel {
    private JFrame frame; // Referência à janela principal

    // Construtor que inicializa a tela de Game Over
    public GameOver(JFrame frame) {
        this.frame = frame;

        // Rótulo que exibe "Game Over"
        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Define a fonte do texto

        // Botão para reiniciar o jogo
        JButton retryButton = new JButton("Tentar Novamente");
        retryButton.addActionListener((ActionEvent e) -> restartGame());

        // Botão para sair do jogo
        JButton exitButton = new JButton("Sair");
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        // Define o layout da tela
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os botões

        // Adiciona os componentes ao layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(gameOverLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(retryButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    // Método para reiniciar o jogo
    private void restartGame() {
        frame.getContentPane().removeAll(); // Remove a tela de Game Over
//        frame.add(new Level1(frame)); // Adiciona o primeiro nível do jogo
        frame.revalidate(); // Atualiza o layout da janela
        frame.repaint(); // Redesenha a janela
    }
}
