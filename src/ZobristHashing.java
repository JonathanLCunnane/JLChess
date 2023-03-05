import java.util.Random;

public class ZobristHashing {
    final static int WPAWN = 0;
    final static int WKNIGHT = 1;
    final static int WBISHOP = 2;
    final static int WROOK = 3;
    final static int WQUEEN = 4;
    final static int WKING = 5;
    final static int WEPAWN = 6;
    final static int WCROOK = 7;
    final static int WCKING = 8;
    final static int BPAWN = 9;
    final static int BKNIGHT = 10;
    final static int BBISHOP = 11;
    final static int BROOK = 12;
    final static int BQUEEN = 13;
    final static int BKING = 14;
    final static int BEPAWN = 15;
    final static int BCROOK = 16;
    final static int BCKING = 17;
    long[][] hashTable = new long[64][18];
    // Each long is 64 bits, which is the most commonly used bit string length.
    long blackToMoveHash;

    ZobristHashing()
    {
        // We first need to generate all the random bit strings.
        Random random = new Random();
        random.setSeed(9810197110115L);
        for (int square = 0; square < 64; square++)
        {
            for (int piece = 0; piece < 18; piece++)
            {
                hashTable[square][piece] = random.nextLong();
            }
        }
        blackToMoveHash = random.nextLong();
    }

    long hash(Board board)
    {
        long hash = 0;
        if (!board.isWhitesMove) hash ^= blackToMoveHash;
        for (int row = 0; row < board.board.length; row++)
        {
            for (int column = 0; column < board.board[row].length; column++)
            {
                Piece currPiece = board.board[row][column];
                int squareNumber = row * 8 + column;
                if (currPiece.type == PieceType.NONE) continue;
                int pieceIDX = getPieceIDX(currPiece);
                hash ^= hashTable[squareNumber][pieceIDX];
            }
        }
        return hash;
    }

    long switchMovingPlayer(long hash)
    {
        return hash ^ blackToMoveHash;
    }

    long updateHash(long prevHash, Integer[] square, Piece piece)
    {
        int squareNumber = square[0] * 8 + square[1];
        int pieceIDX = getPieceIDX(piece);
        prevHash ^= hashTable[squareNumber][pieceIDX];
        return prevHash;
    }

    private int getPieceIDX(Piece piece)
    {
        return switch (piece.type)
        {
            case PieceType.PAWN -> piece.isWhite ? (piece.pieceChar == 'p' ? WEPAWN : WPAWN) : (piece.pieceChar == 'p' ? BEPAWN : BPAWN);
            case PieceType.KNIGHT -> piece.isWhite ? WKNIGHT : BKNIGHT;
            case PieceType.BISHOP -> piece.isWhite ? WBISHOP : BBISHOP;
            case PieceType.ROOK -> piece.isWhite ? (piece.moveCount == 0 ? WCKING : WKING) : (piece.moveCount == 0 ? BCKING : BKING);
            case PieceType.QUEEN -> piece.isWhite ? WQUEEN : BQUEEN;
            case PieceType.KING -> piece.isWhite ? (piece.moveCount == 0 ? WCROOK : WROOK) : (piece.moveCount == 0 ? BCROOK : BROOK);
            default -> 0;
        };
    }
}
