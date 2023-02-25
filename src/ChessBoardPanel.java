import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class ChessBoardPanel extends JPanel {
    private Integer[] clickIndicatorLocation = {null, null};
    private final int boardSize = 512;
    private final int marginSize = 16;
    Board chessBoard;

    ChessBoardPanel(Board board)
    {
        super();
        chessBoard = board;
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

                int extraTopMargin = (getHeight() - boardSize)/2;
                int extraSideMargin = (getWidth() - boardSize)/2;
                int xBoard = mouseEvent.getX() - extraSideMargin;
                int yBoard = mouseEvent.getY() - extraTopMargin;

                int xIdx = xBoard / 64;
                int yIdx = yBoard / 64;

                if ((xIdx >= 0 && xIdx < 8) && (yIdx >= 0 && yIdx < 8))
                {
                    clickIndicatorLocation[0] = xIdx;
                    clickIndicatorLocation[1] = yIdx;
                }
                else
                {
                    clickIndicatorLocation[0] = null;
                    clickIndicatorLocation[1] = null;
                }
                paintComponent(getGraphics());
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    BufferedImage boardImage = ImageGetter.tryGetImage("/img/board.png", getClass());

    @Override
    protected void paintComponent(Graphics g) {
        // Draw board
        g.setColor(Color.DARK_GRAY);

        int extraTopMargin = (getHeight() - boardSize - (marginSize * 2))/2;
        int extraSideMargin = (getWidth() - boardSize - (marginSize * 2))/2;
        extraTopMargin = Math.max(extraTopMargin, 0);
        extraSideMargin = Math.max(extraSideMargin, 0);

        g.drawImage(boardImage, marginSize + extraSideMargin, marginSize + extraTopMargin, null);

        // Draw pieces
        if (chessBoard != null)
        {
            drawPieces(g, extraSideMargin, extraTopMargin);
        }

        // Draw click indicator
        if (clickIndicatorLocation[0] != null && clickIndicatorLocation[1] != null)
        {
            g.setColor(Color.BLUE);
            g.fillRect(
                    marginSize + extraSideMargin + 24 + (clickIndicatorLocation[0] * 64),
                    marginSize + extraTopMargin + 24 + (clickIndicatorLocation[1] * 64),
                    16,
                    16
            );
        }
    }

    private void drawPieces(Graphics g, int extraSideMargin, int extraTopMargin)
    {
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        for (int row = 0; row < chessBoard.board.length; row++)
        {
            for (int column = 0; column < chessBoard.board[row].length; column++)
            {
                Piece currPiece = chessBoard.board[row][column];
                if (currPiece.type != PieceType.NONE)
                {
                    g.drawImage(
                            currPiece.piecePNG,
                            marginSize + extraSideMargin + (column * 64) + 1,
                            marginSize + extraTopMargin + (row * 64) + 1,
                            null
                    );
                }
            }
        }
    }
}
