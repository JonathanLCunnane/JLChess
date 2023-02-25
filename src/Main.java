import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Generate board.
        Board board = new Board();

        // Setup GUI
        JFrame frame = new JFrame("Chess");
        int additionalBorderWidth = 16;
        int additionalBorderHeight = 39;
        frame.setBounds(100, 100, 704 + additionalBorderWidth, 704 + additionalBorderHeight);

        JPanel panel = new ChessBoardPanel(board);
        // Setup frame
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.LIGHT_GRAY);

        // Show frame
        frame.setVisible(true);


    }
}