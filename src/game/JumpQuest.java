package game;

import javax.swing.*;
import java.awt.PopupMenu;

public class JumpQuest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JumpQuest");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Menu(frame));
        frame.setVisible(true);
    }
}
