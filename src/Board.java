import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    Piece[][] board;
    Map<Piece, List<int[]>> possibleMoves = new HashMap<>();
    PieceCollection[][] captureMap = new PieceCollection[8][8];
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

    boolean tryMove(Integer[] from, Integer[] to)
    {
        if (from[0] == null || from[1] == null || to[0] == null || to[1] == null) return false;
        if (from[0] < 0 || from[0] >= 8 || from[1] < 0 || from[1] >= 8 || to[0] < 0 || to[0] >= 8 || to[1] < 0 || to[1] >= 8) return false;
        if (from == to) return false;
        if (board[from[0]][from[1]].type == PieceType.NONE) return false;
        if (new ChessList(possibleMoves.get(board[from[0]][from[1]])).contains(new int[] {to[0], to[1]}))
        {
            // Check for en passant.
            if (board[from[0]][from[1]].type == PieceType.PAWN && board[to[0]][to[1]].type == PieceType.NONE && !from[1].equals(to[1]))
            {
                board[from[0]][from[1]].moveCount++;
                board[to[0]][to[1]] = board[from[0]][from[1]];
                board[from[0]][from[1]] = new Piece(PieceType.NONE);
                // En passant-ed piece.
                if (from[0] == 3) // If white piece is capturing
                {
                    board[to[0] + 1][to[1]] = new Piece(PieceType.NONE);
                }
                else // If black piece is capturing.
                {
                    board[to[0] - 1][to[1]] = new Piece(PieceType.NONE);
                }
            }
            else
            {
                board[from[0]][from[1]].moveCount++;
                board[to[0]][to[1]] = board[from[0]][from[1]];
                board[from[0]][from[1]] = new Piece(PieceType.NONE);
            }
            configureBoard();
            return true;
        }
        return false;
    }

    private void configureBoard()
    {
        // Fill capture map with NULL Pieces
        for (int row = 0; row < board.length; row++)
        {
            for (int column = 0; column < board[row].length; column++)
            {
                captureMap[row][column] = new PieceCollection();
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
                    case PieceType.BISHOP -> currPossibleMoves = getBishopMoves(row, column, currPiece);
                    case PieceType.ROOK -> currPossibleMoves = getRookMoves(row, column, currPiece);
                    case PieceType.QUEEN -> currPossibleMoves = getQueenMoves(row, column, currPiece);
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
        // Get unvalidated king moves so that the capture map is correctly updated.
        Piece wKing = board[wKingIDXS[0]][wKingIDXS[1]];
        List<int[]> unvalidatedWKingMoves = getUnvalidatedKingMoves(wKingIDXS[0], wKingIDXS[1], wKing);
        Piece bKing = board[bKingIDXS[0]][bKingIDXS[1]];
        List<int[]> unvalidatedBKingMoves = getUnvalidatedKingMoves(bKingIDXS[0], bKingIDXS[1], bKing);

        // Validate the king moves.
        possibleMoves.put(wKing, getValidatedKingMoves(wKing, unvalidatedWKingMoves));
        possibleMoves.put(bKing, getValidatedKingMoves(bKing, unvalidatedBKingMoves));

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
                captureMap[row - 1][column - 1].pieces.add(currPiece);
            }
            if (row > 0 && column < 7)
            {
                if (!board[row - 1][column + 1].isWhite) moves.add(new int[] {row - 1, column + 1});
                captureMap[row - 1][column + 1].pieces.add(currPiece);
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
                captureMap[row + 1][column - 1].pieces.add(currPiece);
            }
            if (row < 7 && column < 7)
            {
                if (board[row + 1][column + 1].type != PieceType.NONE && board[row + 1][column + 1].isWhite) moves.add(new int[] {row + 1, column + 1});
                captureMap[row + 1][column + 1].pieces.add(currPiece);
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
        for (int[] move: moves) captureMap[move[0]][move[1]].pieces.add(currPiece);
        return moves;
    }

    private List<int[]> getBishopMoves(int row, int column, Piece currPiece)
    {
        List<int[]> moves = new ArrayList<>();

        int rowScan;
        int columnScan;
        // Left-Up direction.
        rowScan = row - 1;
        columnScan = column - 1;
        while (rowScan >= 0 && columnScan >= 0)
        {
            int[] move = new int[] {rowScan, columnScan};
            if (shouldBreakMoves(moves, move, currPiece)) break;
            moves.add(move);
            rowScan--;
            columnScan--;
        }

        // Right-Up direction.
        rowScan = row - 1;
        columnScan = column + 1;
        while (rowScan >= 0 && columnScan <= 7)
        {
            int[] move = new int[] {rowScan, columnScan};
            if (shouldBreakMoves(moves, move, currPiece)) break;
            moves.add(move);
            rowScan--;
            columnScan++;
        }

        // Right-Down direction.
        rowScan = row + 1;
        columnScan = column + 1;
        while (rowScan <= 7 && columnScan <= 7)
        {
            int[] move = new int[] {rowScan, columnScan};
            if (shouldBreakMoves(moves, move, currPiece)) break;
            moves.add(move);
            rowScan++;
            columnScan++;
        }

        // Left-Down direction.
        rowScan = row + 1;
        columnScan = column - 1;
        while (rowScan <= 7 && columnScan >= 0)
        {
            int[] move = new int[] {rowScan, columnScan};
            if (shouldBreakMoves(moves, move, currPiece)) break;
            moves.add(move);
            rowScan++;
            columnScan--;
        }

        // Add moves to capture map
        for (int[] move: moves) captureMap[move[0]][move[1]].pieces.add(currPiece);
        return moves;
    }

    private List<int[]> getRookMoves(int row, int column, Piece currPiece)
    {
        List<int[]> moves = new ArrayList<>();

        int rowScan;
        int columnScan;
        // Up direction.
        rowScan = row - 1;
        while (rowScan >= 0)
        {
            int[] move = new int[] {rowScan, column};
            if (shouldBreakMoves(moves, move, currPiece)) break;
            moves.add(move);
            rowScan--;
        }

        // Right direction.
        columnScan = column + 1;
        while (columnScan <= 7)
        {
            int[] move = new int[] {row, columnScan};
            if (shouldBreakMoves(moves, move, currPiece)) break;
            moves.add(move);
            columnScan++;
        }

        // Down direction.
        rowScan = row + 1;
        while (rowScan <= 7)
        {
            int[] move = new int[] {rowScan, column};
            if (shouldBreakMoves(moves, move, currPiece)) break;
            moves.add(move);
            rowScan++;
        }

        // Left direction.
        columnScan = column - 1;
        while (columnScan >= 0)
        {
            int[] move = new int[] {row, columnScan};
            if (shouldBreakMoves(moves, move, currPiece)) break;
            moves.add(move);
            columnScan--;
        }

        // Add moves to capture map
        for (int[] move: moves) captureMap[move[0]][move[1]].pieces.add(currPiece);
        return moves;
    }

    private List<int[]> getQueenMoves(int row, int column, Piece currPiece)
    {
        List<int[]> moves = new ArrayList<>();
        moves.addAll(getBishopMoves(row, column, currPiece));
        moves.addAll(getRookMoves(row, column, currPiece));

        return moves;
    }

    private List<int[]> getUnvalidatedKingMoves(int row, int column, Piece currPiece)
    {
        List<int[]> moves = new ArrayList<>();

        // Get moves.
        moves.add(new int[] {row, column + 1});
        moves.add(new int[] {row - 1, column + 1});
        moves.add(new int[] {row - 1, column});
        moves.add(new int[] {row - 1, column - 1});
        moves.add(new int[] {row, column - 1});
        moves.add(new int[] {row + 1, column - 1});
        moves.add(new int[] {row + 1, column});
        moves.add(new int[] {row + 1, column + 1});

        moves.removeIf(move -> move[0] < 0 || move[0] >= 8 || move[1] < 0 || move[1] >= 8);

        // Add moves to capture map
        for (int[] move: moves) captureMap[move[0]][move[1]].pieces.add(currPiece);
        return moves;
    }

    private List<int[]> getValidatedKingMoves(Piece currPiece, List<int[]> unvalidatedMoves)
    {
        unvalidatedMoves.removeIf(move -> {
            Piece comparingPiece = board[move[0]][move[1]];
            // Check if the piece is the opposing colour.
            if (comparingPiece.type != PieceType.NONE)
            {
                return currPiece.isWhite == comparingPiece.isWhite;
            }
            // Check if moving would put king in check.
            for (Piece canCaptureSquare : captureMap[move[0]][move[1]].pieces)
            {
                if (canCaptureSquare.isWhite != currPiece.isWhite) return true;
            }

            return false;
        });

        return unvalidatedMoves;
    }

    private boolean shouldBreakMoves(List<int[]> moves, int[] move, Piece currPiece)
    {
        if (board[move[0]][move[1]].type != PieceType.NONE)
        {
            if (board[move[0]][move[1]].isWhite != currPiece.isWhite)
            {
                moves.add(move);
                captureMap[move[0]][move[1]].pieces.add(currPiece);
            }
            return true;
        }
        return false;
    }
}
