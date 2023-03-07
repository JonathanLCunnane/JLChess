public class Piece {
    boolean isWhite;
    int moveCount;
    int type;
    Character pieceChar; // A piece char of 'p' rather than 'P' is a pawn that can capture en passant.

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

    public void promoteTo(int pieceType)
    {
        pieceChar = switch (pieceType)
        {
            case PieceType.PAWN -> 'P';
            case PieceType.KNIGHT -> 'N';
            case PieceType.BISHOP -> 'B';
            case PieceType.ROOK -> 'R';
            case PieceType.QUEEN -> 'Q';
            case PieceType.KING -> 'K';
            default -> pieceChar;
        };
        type = pieceType;
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
