import java.awt.image.BufferedImage;

public class Piece {
    boolean isWhite;
    int moveCount;
    int type;
    Character pieceChar;
    BufferedImage piecePNG;
    Piece(int pieceType, boolean white)
    {
        isWhite = white;
        type = pieceType;
        moveCount = 0;
        configurePiece();
    }
    Piece(int pieceType)
    {
        if (pieceType != PieceType.NONE)
        {
            throw new IllegalArgumentException("pieceType must be PieceType.NONE if boolean white is undefined.");
        }
        type = PieceType.NONE;
        isWhite = true;
        pieceChar = null;
        moveCount = 0;
    }

    private void configurePiece()
    {
        switch (type)
        {
            case PieceType.NONE -> pieceChar = null;
            case PieceType.PAWN -> pieceChar = 'P';
            case PieceType.KNIGHT -> pieceChar = 'N';
            case PieceType.BISHOP -> pieceChar = 'B';
            case PieceType.ROOK -> pieceChar = 'R';
            case PieceType.QUEEN -> pieceChar = 'Q';
            case PieceType.KING -> pieceChar = 'K';
        }
        if (pieceChar != null)
        {
            if (isWhite)
            {
                piecePNG = ImageGetter.tryGetImage(
                        String.format("/img/white_%c.png", pieceChar),
                        getClass()
                );
            }
            else
            {
                piecePNG = ImageGetter.tryGetImage(
                        String.format("/img/black_%c.png", pieceChar),
                        getClass()
                );
            }
        }
    }
}
