import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChessBoardPanel extends JPanel {
    private final int boardSize = 512;
    private final int marginSize = 16;
    private Integer[] clickIndicatorLocation = {null, null};
    private Integer[] previousClickIndicatorLocation = {null, null};
    boolean captureMapOn = false;
    boolean moveIndicator = false;
    Board chessBoard;
    Game chessGame;
    BufferedImage boardImage = ImageGetter.tryGetImage("/img/board.png", getClass());

    ChessBoardPanel(Game game)
    {
        super();
        chessGame = game;
        chessBoard = game.board;
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                int extraTopMargin = (getHeight() - boardSize)/2;
                int extraSideMargin = (getWidth() - boardSize)/2;
                int columnBoard = mouseEvent.getX() - extraSideMargin;
                int rowBoard = mouseEvent.getY() - extraTopMargin;

                int columnIdx = columnBoard / 64;
                int rowIdx = rowBoard / 64;

                if ((columnIdx >= 0 && columnIdx < 8) && (rowIdx >= 0 && rowIdx < 8))
                {
                    clickIndicatorLocation[0] = rowIdx;
                    clickIndicatorLocation[1] = columnIdx;
                }
                else
                {
                    clickIndicatorLocation[0] = null;
                    clickIndicatorLocation[1] = null;
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                Integer[] newClickIndicatorLocation = new Integer[] {null, null};

                int extraTopMargin = (getHeight() - boardSize)/2;
                int extraSideMargin = (getWidth() - boardSize)/2;
                int columnBoard = mouseEvent.getX() - extraSideMargin;
                int rowBoard = mouseEvent.getY() - extraTopMargin;

                int columnIdx = columnBoard / 64;
                int rowIdx = rowBoard / 64;

                if ((columnIdx >= 0 && columnIdx < 8) && (rowIdx >= 0 && rowIdx < 8))
                {
                    newClickIndicatorLocation[0] = rowIdx;
                    newClickIndicatorLocation[1] = columnIdx;
                }
                if (Arrays.equals(clickIndicatorLocation, newClickIndicatorLocation))
                {
                    boolean moved = game.tryMove(previousClickIndicatorLocation, clickIndicatorLocation);
                    if (moved) moveIndicator = true;
                    if (Objects.equals(clickIndicatorLocation[0], previousClickIndicatorLocation[0]) && Objects.equals(clickIndicatorLocation[1], previousClickIndicatorLocation[1])) clickIndicatorLocation = new Integer[] {null, null};

                    paintComponent(getGraphics());
                    Integer[][] AIMove = null;
                    if (moved && game.versusAI) {
                        AIMove = game.doBlacksBestMove(5000);
                        if (AIMove != null)
                        {
                            previousClickIndicatorLocation = AIMove[0];
                            clickIndicatorLocation = AIMove[1];
                        }
                    }
                    paintComponent(getGraphics());
                    previousClickIndicatorLocation = clickIndicatorLocation.clone();
                    if (moved || AIMove != null) clickIndicatorLocation = new Integer[]{null, null};
                    moveIndicator = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

   public void showCaptureMap()
   {
       captureMapOn = true;
       paintComponent(getGraphics());
   }

   public void hideCaptureMap()
   {
       captureMapOn = false;
       paintComponent(getGraphics());
   }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw board
        g.setColor(Color.DARK_GRAY);

        int extraTopMargin = (getHeight() - boardSize - (marginSize * 2))/2;
        int extraSideMargin = (getWidth() - boardSize - (marginSize * 2))/2;
        extraTopMargin = Math.max(extraTopMargin, 0);
        extraSideMargin = Math.max(extraSideMargin, 0);

        g.drawImage(boardImage, marginSize + extraSideMargin, marginSize + extraTopMargin, null);


        // Draw move indicator
        if (moveIndicator && (clickIndicatorLocation[0] != null && clickIndicatorLocation[1] != null && previousClickIndicatorLocation[0] != null && previousClickIndicatorLocation[1] != null))
        {
            drawMoveIndicator(g, previousClickIndicatorLocation, clickIndicatorLocation);
        }

        // Draw pieces
        if (chessBoard != null)
        {
            drawPieces(g, extraSideMargin, extraTopMargin);

            // Draw capture map
            if (chessBoard.captureMap != null && captureMapOn)
            {
                drawCaptureMap(g, extraSideMargin, extraTopMargin);
            }
        }

        if (clickIndicatorLocation[0] != null && clickIndicatorLocation[1] != null && !moveIndicator)
        {
            // Draw click indicator
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.BLUE);
            g2.drawArc(
                    marginSize + extraSideMargin + 24 + (clickIndicatorLocation[1] * 64),
                    marginSize + extraTopMargin + 24 + (clickIndicatorLocation[0] * 64),
                    16,
                    16,
                    0,
                    360
            );

            // Draw move indicator(s)
            Piece currPiece = chessBoard.board[clickIndicatorLocation[0]][clickIndicatorLocation[1]];
            List<int[]> moves = chessBoard.possibleMoves.get(currPiece);
            g2.setColor(currPiece.isWhite == chessBoard.isWhitesMove ? Color.RED : Color.LIGHT_GRAY);
            if (moves != null) for (int[] move: moves)
            {
                g2.drawArc(
                        marginSize + extraSideMargin + 24 + (move[1] * 64),
                        marginSize + extraTopMargin + 24 + (move[0] * 64),
                        16,
                        16,
                        0,
                        360
                );
            }
        }

    }

    private void drawPieces(Graphics g, int extraSideMargin, int extraTopMargin)
    {
        for (int row = 0; row < chessBoard.board.length; row++)
        {
            for (int column = 0; column < chessBoard.board[row].length; column++)
            {
                Piece currPiece = chessBoard.board[row][column];
                if (currPiece.type != PieceType.NONE)
                {
                    g.drawImage(
                            PieceImages.getImage(currPiece.pieceChar.toString().toUpperCase().toCharArray()[0], currPiece.isWhite),
                            marginSize + extraSideMargin + (column * 64) + 1,
                            marginSize + extraTopMargin + (row * 64) + 1,
                            null
                    );
                }
            }
        }
    }

    private void drawCaptureMap(Graphics g, int extraSideMargin, int extraTopMargin)
    {
        Color whiteCaptureMapColour = Color.YELLOW;
        whiteCaptureMapColour = new Color(whiteCaptureMapColour.getRed()/255F, whiteCaptureMapColour.getGreen()/255F, whiteCaptureMapColour.getBlue()/255F, 0.2F);
        Color blackCaptureMapColour = Color.BLUE;
        blackCaptureMapColour = new Color(blackCaptureMapColour.getRed()/255F, blackCaptureMapColour.getGreen()/255F, blackCaptureMapColour.getBlue()/255F, 0.2F);
        Color bothCaptureMapColour = Color.GREEN;
        bothCaptureMapColour = new Color(bothCaptureMapColour.getRed()/255F, bothCaptureMapColour.getGreen()/255F, bothCaptureMapColour.getBlue()/255F, 0.2F);
        Color currColour;
        boolean blackPresent;
        boolean whitePresent;
        for (int row = 0; row < chessBoard.board.length; row++)
        {
            for (int column = 0; column < chessBoard.board[row].length; column++)
            {
                whitePresent = false;
                blackPresent = false;
                for (Piece piece: chessBoard.captureMap[row][column].pieces)
                {
                    if (piece.isWhite)
                    {
                        whitePresent = true;
                    }
                    else
                    {
                        blackPresent = true;
                    }
                }
                currColour = null;
                if (whitePresent && blackPresent) currColour = bothCaptureMapColour;
                if (whitePresent && !blackPresent) currColour = whiteCaptureMapColour;
                if (!whitePresent && blackPresent) currColour = blackCaptureMapColour;
                if (currColour == null) continue;
                g.setColor(currColour);
                g.fillRect(
                        marginSize + extraSideMargin + (column * 64),
                        marginSize + extraTopMargin + (row * 64),
                        64,
                        64
                );
            }
        }
    }

    private void drawMoveIndicator(Graphics g, Integer[] from, Integer[] to)
    {
        int extraTopMargin = (getHeight() - boardSize - (marginSize * 2))/2;
        int extraSideMargin = (getWidth() - boardSize - (marginSize * 2))/2;
        extraTopMargin = Math.max(extraTopMargin, 0);
        extraSideMargin = Math.max(extraSideMargin, 0);

        Color moveColour = Color.RED;
        moveColour = new Color(moveColour.getRed()/255F, moveColour.getGreen()/255F, moveColour.getBlue()/255F, 0.2F);
        g.setColor(moveColour);
        g.fillRect(
                marginSize + extraSideMargin + (from[1] * 64),
                marginSize + extraTopMargin + (from[0] * 64),
                64,
                64
        );
        g.fillRect(
                marginSize + extraSideMargin + (to[1] * 64),
                marginSize + extraTopMargin + (to[0] * 64),
                64,
                64
        );
    }
}
