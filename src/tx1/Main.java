package tx1;

import tx1.ui.TvUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }
    private static void createGUI() {
        TvUI ui = new TvUI();
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setTitle("TV UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
