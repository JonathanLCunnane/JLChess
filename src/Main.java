import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Setup game.
        Board board = new Board();
        Game game = new Game(true, board);

        // Setup GUI
        JFrame frame = new JFrame("Chess");
        int additionalBorderWidth = 16;
        int additionalBorderHeight = 39;
        frame.setBounds(100, 100, 704 + additionalBorderWidth, 704 + additionalBorderHeight);

        ChessBoardPanel panel = new ChessBoardPanel(game);
        // Setup frame
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.LIGHT_GRAY);

        // Setup menu bar
        JMenuBar menuBar = new JMenuBar();
        ChessActionListener listener = new ChessActionListener(panel, board);

        JButton toggleCaptureMapButton = new JButton("Show Capture Map");
        toggleCaptureMapButton.addActionListener(listener);

        JButton cyclePromotionPieceButton = new JButton("Pawn Promotes to: Queen");
        cyclePromotionPieceButton.addActionListener(listener);

        menuBar.add(toggleCaptureMapButton);
        menuBar.add(cyclePromotionPieceButton);
        frame.setJMenuBar(menuBar);

        // Show frame
        frame.setVisible(true);


    }
}