import javax.swing.*;
import java.awt.event.*;

public class ChessActionListener implements ActionListener {
    ChessBoardPanel panel;
    ChessActionListener(ChessBoardPanel boardPanel)
    {
        panel = boardPanel;
    }
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "Show Capture Map" ->
            {
                JButton button = (JButton) e.getSource();
                button.setText("Hide Capture Map");
                panel.showCaptureMap();
            }
            case "Hide Capture Map" ->
            {
                JButton button = (JButton) e.getSource();
                button.setText("Show Capture Map");
                panel.hideCaptureMap();
            }
        }
    }
}
