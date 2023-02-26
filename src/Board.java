import java.util.*;

public class Board {
    Piece[][] board;
    Map<Piece, List<int[]>> possibleMoves = new HashMap<>();
    Piece[][] captureMap = new Piece[8][8];
    static Piece[][] defaultBoard = {
            {new Piece(PieceType.ROOK, false), new Piece(PieceType.KNIGHT, false), new Piece(PieceType.BISHOP, false), new Piece(PieceType.QUEEN, false), new Piece(PieceType.KING, false), new Piece(PieceType.BISHOP, false), new Piece(PieceType.KNIGHT, false), new Piece(PieceType.ROOK, false)},
            {new Piece(PieceType.PAWN, false), new Piece(PieceType.PAWN, false), new Piece(PieceType.PAWN, false), new Piece(PieceType.PAWN, false), new Piece(PieceType.PAWN, false), new Piece(PieceType.PAWN, false), new Piece(PieceType.PAWN, false), new Piece(PieceType.PAWN, false)},
            {new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE)},
            {new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE)},
            {new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE)},
            {new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE), new Piece(PieceType.NONE)},
            {new Piece(PieceType.PAWN, true), new Piece(PieceType.PAWN, true), new Piece(PieceType.PAWN, true), new Piece(PieceType.PAWN, true), new Piece(PieceType.PAWN, true), new Piece(PieceType.PAWN, true), new Piece(PieceType.PAWN, true), new Piece(PieceType.PAWN, true)},
            {new Piece(PieceType.ROOK, true), new Piece(PieceType.KNIGHT, true), new Piece(PieceType.BISHOP, true), new Piece(PieceType.QUEEN, true), new Piece(PieceType.KING, true), new Piece(PieceType.BISHOP, true), new Piece(PieceType.KNIGHT, true), new Piece(PieceType.ROOK, true)}
    };
    Board(Piece[][] startingBoard)
    {
        board = startingBoard;
        configureBoard();
    }
    Board()
    {
        board = defaultBoard;
        configureBoard();
    }

    private void configureBoard()
    {
        // Fill capture map with NULL Pieces
        for (int row = 0; row < board.length; row++)
        {
            for (int column = 0; column < board[row].length; column++)
            {
                captureMap[row][column] = new Piece(PieceType.NONE);
            }
        }
        int[] wKingIDXS = new int[] {-1, -1};
        int[] bKingIDXS = new int[] {-1, -1};
        // Get all piece moves apart from king.
        for (int row = 0; row < board.length; row++)
        {
            for (int column = 0; column < board[row].length; column++)
            {
                Piece currPiece = board[row][column];
                List<int[]> currPossibleMoves = new ArrayList<>();
                switch (currPiece.type)
                {
                    case PieceType.NONE ->
                    {}
                    case PieceType.PAWN -> currPossibleMoves = getPawnMoves(row, column, currPiece);
                    case PieceType.KNIGHT -> currPossibleMoves = getKnightMoves(row, column, currPiece);
                    case PieceType.BISHOP ->
                    {

                    }
                    case PieceType.ROOK -> {}
                    case PieceType.QUEEN -> {}
                    case PieceType.KING ->
                    {
                        if (currPiece.isWhite)
                        {
                            wKingIDXS = new int[] {row, column};
                        }
                        else
                        {
                            bKingIDXS = new int[] {row, column};
                        }
                    } // King moves are dealt with later.
                }
                if (currPossibleMoves != null) possibleMoves.put(currPiece, currPossibleMoves);
            }
        }
        // Get possible king moves.
    }

    private List<int[]> getPawnMoves(int row, int column, Piece currPiece)
    {
        List<int[]> moves = new ArrayList<>();
        if (currPiece.isWhite)
        {
            // Moves
            if (row > 0 && board[row - 1][column].type == PieceType.NONE)
            {
                moves.add(new int[] {row - 1, column});
                if (row > 1 && currPiece.moveCount == 0 && board[row - 2][column].type == PieceType.NONE) moves.add(new int[] {row - 2, column});
            }
            // Captures
            if (row > 0 && column > 0)
            {
                if (!board[row - 1][column - 1].isWhite) moves.add(new int[] {row - 1, column - 1});
                captureMap[row - 1][column - 1] = currPiece;
            }
            if (row > 0 && column < 7)
            {
                if (!board[row - 1][column + 1].isWhite) moves.add(new int[] {row - 1, column + 1});
                captureMap[row - 1][column + 1] = currPiece;
            }
            // En passant
            if (column > 0)
            {
                Piece underPassantLeft = board[row][column - 1];
                if (!underPassantLeft.isWhite && underPassantLeft.moveCount == 1)
                {
                    moves.add(new int[] {row - 1, column - 1});
                }
            }
            if (column < 7)
            {
                Piece underPassantRight = board[row][column + 1];
                if (!underPassantRight.isWhite && underPassantRight.moveCount == 1)
                {
                    moves.add(new int[] {row - 1, column + 1});
                }
            }
        }
        else //isBlack
        {
            // Moves
            if (row < 7 && board[row + 1][column].type == PieceType.NONE)
            {
                moves.add(new int[] {row + 1, column});
                if (row < 6 && currPiece.moveCount == 0 && board[row + 2][column].type == PieceType.NONE) moves.add(new int[] {row + 2, column});
            }
            // Captures
            if (row < 7 && column > 0)
            {
                if (board[row + 1][column - 1].type != PieceType.NONE  && board[row + 1][column - 1].isWhite) moves.add(new int[] {row + 1, column - 1});
                captureMap[row + 1][column - 1] = currPiece;
            }
            if (row < 7 && column < 7)
            {
                if (board[row + 1][column + 1].type != PieceType.NONE && board[row + 1][column + 1].isWhite) moves.add(new int[] {row + 1, column + 1});
                captureMap[row + 1][column + 1] = currPiece;
            }
            // En passant
            if (column > 0)
            {
                Piece underPassantLeft = board[row][column - 1];
                if (underPassantLeft.type != PieceType.NONE && underPassantLeft.isWhite && underPassantLeft.moveCount == 1)
                {
                    moves.add(new int[] {row + 1, column - 1});
                }
            }
            if (column < 7)
            {
                Piece underPassantRight = board[row][column + 1];
                if (underPassantRight.type != PieceType.NONE && underPassantRight.isWhite && underPassantRight.moveCount == 1)
                {
                    moves.add(new int[] {row + 1, column + 1});
                }
            }

        }
        return moves;
    }

    private List<int[]> getKnightMoves(int row, int column, Piece currPiece)
    {
        List<int[]> moves = new ArrayList<>();

        // Get moves.
        moves.add(new int[] {row + 2, column + 1});
        moves.add(new int[] {row + 1, column + 2});
        moves.add(new int[] {row - 1, column + 2});
        moves.add(new int[] {row - 2, column + 1});
        moves.add(new int[] {row - 2, column - 1});
        moves.add(new int[] {row - 1, column - 2});
        moves.add(new int[] {row + 1, column - 2});
        moves.add(new int[] {row + 2, column - 1});

        moves.removeIf(move -> {
            if (move[0] < 0 || move[0] >= 8 || move[1] < 0 || move[1] >= 8) return true;
            Piece comparingPiece = board[move[0]][move[1]];
            if (comparingPiece.type != PieceType.NONE)
            {
                return currPiece.isWhite == comparingPiece.isWhite;
            }
            return false;
        });

        // Add moves to capture map
        for (int[] move: moves) captureMap[move[0]][move[1]] = currPiece;
        return moves;
    }
}
