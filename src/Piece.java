public class Piece {
    boolean isWhite;
    int moveCount;
    int type;
    Character pieceChar;

    Piece(int pieceTypeCopy, boolean whiteCopy, int moveCountCopy, Character pieceCharCopy)
    {
        type = pieceTypeCopy;
        isWhite = whiteCopy;
        moveCount = moveCountCopy;
        pieceChar = pieceCharCopy;
    }

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

    public Piece copy()
    {
        return new Piece(type, isWhite, moveCount, pieceChar);
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
    }
}
