import javax.swing.*;
import java.awt.event.*;

public class ChessActionListener implements ActionListener {
    ChessBoardPanel panel;
    Board chessBoard;
    ChessActionListener(ChessBoardPanel boardPanel, Board board)
    {
        panel = boardPanel;
        chessBoard = board;
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
            case "Pawn Promotes to: Queen" ->
            {
                JButton button = (JButton) e.getSource();
                button.setText("Pawn Promotes to: Rook");
                chessBoard.setPromotionType(PieceType.ROOK);
            }
            case "Pawn Promotes to: Rook" ->
            {
                JButton button = (JButton) e.getSource();
                button.setText("Pawn Promotes to: Bishop");
                chessBoard.setPromotionType(PieceType.BISHOP);
            }
            case "Pawn Promotes to: Bishop" ->
            {
                JButton button = (JButton) e.getSource();
                button.setText("Pawn Promotes to: Knight");
                chessBoard.setPromotionType(PieceType.KNIGHT);
            }
            case "Pawn Promotes to: Knight" ->
            {
                JButton button = (JButton) e.getSource();
                button.setText("Pawn Promotes to: Queen");
                chessBoard.setPromotionType(PieceType.QUEEN);
            }
        }
    }
}
