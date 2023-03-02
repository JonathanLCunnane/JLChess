import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    Piece[][] board;
    Map<Piece, List<int[]>> possibleMoves = new HashMap<>();
    PieceCollection[][] captureMap = new PieceCollection[8][8];
    boolean isWhitesMove;
    boolean singleCheck;
    boolean doubleCheck;
    int[] wKingIDXS;
    int[] bKingIDXS;
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
        isWhitesMove = true;
        singleCheck = false;
        doubleCheck = false;
        configureBoard();
    }
    Board()
    {
        board = defaultBoard;
        isWhitesMove = true;
        singleCheck = false;
        doubleCheck = false;
        wKingIDXS = new int[] {7, 4};
        bKingIDXS = new int[] {0, 4};
        configureBoard();
    }

    boolean tryMove(Integer[] from, Integer[] to)
    {
        if (from[0] == null || from[1] == null || to[0] == null || to[1] == null) return false;
        if (from[0] < 0 || from[0] >= 8 || from[1] < 0 || from[1] >= 8 || to[0] < 0 || to[0] >= 8 || to[1] < 0 || to[1] >= 8) return false;
        if (from == to) return false;
        if (board[from[0]][from[1]].type == PieceType.NONE) return false;
        if (board[from[0]][from[1]].isWhite != isWhitesMove) return false;
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
            // Check for queen side castling
            else if (board[from[0]][from[1]].type == PieceType.KING && (from[1] - to[1] == 2))
            {
                // Move king
                board[from[0]][from[1]].moveCount++;
                board[to[0]][to[1]] = board[from[0]][from[1]];
                board[from[0]][from[1]] = new Piece(PieceType.NONE);

                // Move rook
                board[from[0]][from[1] - 4].moveCount++;
                board[to[0]][to[1] + 1] = board[from[0]][from[1] - 4];
                board[from[0]][from[1] - 4] = new Piece(PieceType.NONE);
            }
            // Check for king side castling
            else if (board[from[0]][from[1]].type == PieceType.KING && from[1] - to[1] == -2)
            {
                // Move king
                if (board[from[0]][from[1]].isWhite)
                {
                    wKingIDXS = new int[] {to[0], to[1]};
                }
                else
                {
                    bKingIDXS = new int[] {to[0], to[1]};
                }
                board[from[0]][from[1]].moveCount++;
                board[to[0]][to[1]] = board[from[0]][from[1]];
                board[from[0]][from[1]] = new Piece(PieceType.NONE);

                // Move rook
                board[from[0]][from[1] + 3].moveCount++;
                board[to[0]][to[1] - 1] = board[from[0]][from[1] + 3];
                board[from[0]][from[1] + 3] = new Piece(PieceType.NONE);
            }
            else
            {
                if (board[from[0]][from[1]].type == PieceType.KING)
                {
                    if (board[from[0]][from[1]].isWhite)
                    {
                        wKingIDXS = new int[] {to[0], to[1]};
                    }
                    else
                    {
                        bKingIDXS = new int[] {to[0], to[1]};
                    }
                }
                board[from[0]][from[1]].moveCount++;
                board[to[0]][to[1]] = board[from[0]][from[1]];
                board[from[0]][from[1]] = new Piece(PieceType.NONE);
            }

            long start = System.nanoTime();
            configureBoard();
            long end = System.nanoTime();
            float ms = (end - start)/1000000F;
            int count = 0;
            for (List<int[]> moves: possibleMoves.values())
            {
                count += moves.size();
            }
            System.out.printf("%fms for %d moves \n", ms, count);

            isWhitesMove = !isWhitesMove;
            int checkNumber = numberOfKingCheckingPieces();
            if (checkNumber > 0)
            {
                if (checkNumber > 1) doubleCheck = true;
                else singleCheck = true;
            }

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
        // Get all piece moves apart from king.
        for (int row = 0; row < board.length; row++)
        {
            for (int column = 0; column < board[row].length; column++)
            {
                Piece currPiece = board[row][column];
                List<int[]> currPossibleMoves = new ArrayList<>();
                switch (currPiece.type)
                {
                    case PieceType.NONE, PieceType.KING ->
                    {} // King moves are dealt with later.
                    case PieceType.PAWN -> currPossibleMoves = getPawnMoves(row, column, currPiece);
                    case PieceType.KNIGHT -> currPossibleMoves = getKnightMoves(row, column, currPiece);
                    case PieceType.BISHOP -> currPossibleMoves = getBishopMoves(row, column, currPiece);
                    case PieceType.ROOK -> currPossibleMoves = getRookMoves(row, column, currPiece);
                    case PieceType.QUEEN -> currPossibleMoves = getQueenMoves(row, column, currPiece);
                }
                possibleMoves.put(currPiece, currPossibleMoves);
            }
        }
        // Get unvalidated king moves so that the capture map is correctly updated.
        Piece wKing = board[wKingIDXS[0]][wKingIDXS[1]];
        List<int[]> unvalidatedWKingMoves = getUnvalidatedKingMoves(wKingIDXS[0], wKingIDXS[1], wKing);
        Piece bKing = board[bKingIDXS[0]][bKingIDXS[1]];
        List<int[]> unvalidatedBKingMoves = getUnvalidatedKingMoves(bKingIDXS[0], bKingIDXS[1], bKing);

        // Validate the king moves.
        possibleMoves.put(wKing, getValidatedKingMoves(wKing, unvalidatedWKingMoves, wKingIDXS[0], wKingIDXS[1]));
        possibleMoves.put(bKing, getValidatedKingMoves(bKing, unvalidatedBKingMoves, bKingIDXS[0], bKingIDXS[1]));

        // Remove self-'discovered check' moves.
        removeRemainingIllegalMoves(wKingIDXS[0], wKingIDXS[1]);
        removeRemainingIllegalMoves(bKingIDXS[0], bKingIDXS[1]);
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
                    captureMap[row][column - 1].pieces.add(currPiece);
                }
            }
            if (column < 7)
            {
                Piece underPassantRight = board[row][column + 1];
                if (!underPassantRight.isWhite && underPassantRight.moveCount == 1)
                {
                    moves.add(new int[] {row - 1, column + 1});
                    captureMap[row][column + 1].pieces.add(currPiece);
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
        List<int[]> moves = getUnvalidatedKnightMoves(row, column);

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
            if (shouldBreakMoves(moves, move, currPiece))
            {
                if (board[move[0]][move[1]].type == PieceType.KING)
                {
                    if (move[0] > 0 && move[1] > 0) captureMap[move[0] - 1][move[1] - 1].pieces.add(currPiece);
                }
                break;
            }
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
            if (shouldBreakMoves(moves, move, currPiece))
            {
                if (board[move[0]][move[1]].type == PieceType.KING)
                {
                    if (move[0] > 0 && move[1] < 7) captureMap[move[0] - 1][move[1] + 1].pieces.add(currPiece);
                }
                break;
            }
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
            if (shouldBreakMoves(moves, move, currPiece))
            {
                if (board[move[0]][move[1]].type == PieceType.KING)
                {
                    if (move[0] < 7 && move[1] < 7) captureMap[move[0] + 1][move[1] + 1].pieces.add(currPiece);
                }
                break;
            }
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
            if (shouldBreakMoves(moves, move, currPiece))
            {
                if (board[move[0]][move[1]].type == PieceType.KING)
                {
                    if (move[0] < 7 && move[1] > 0) captureMap[move[0] + 1][move[1] - 1].pieces.add(currPiece);
                }
                break;
            }
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
            if (shouldBreakMoves(moves, move, currPiece))
            {
                if (board[move[0]][move[1]].type == PieceType.KING)
                {
                    if (move[0] > 0) captureMap[move[0] - 1][move[1]].pieces.add(currPiece);
                }
                break;
            }
            moves.add(move);
            rowScan--;
        }

        // Right direction.
        columnScan = column + 1;
        while (columnScan <= 7)
        {
            int[] move = new int[] {row, columnScan};
            if (shouldBreakMoves(moves, move, currPiece))
            {
                if (board[move[0]][move[1]].type == PieceType.KING)
                {
                    if (move[1] < 7) captureMap[move[0]][move[1] + 1].pieces.add(currPiece);
                }
                break;
            }
            moves.add(move);
            columnScan++;
        }

        // Down direction.
        rowScan = row + 1;
        while (rowScan <= 7)
        {
            int[] move = new int[] {rowScan, column};
            if (shouldBreakMoves(moves, move, currPiece))
            {
                if (board[move[0]][move[1]].type == PieceType.KING)
                {
                    if (move[0] < 7) captureMap[move[0] + 1][move[1]].pieces.add(currPiece);
                }
                break;
            }
            moves.add(move);
            rowScan++;
        }

        // Left direction.
        columnScan = column - 1;
        while (columnScan >= 0)
        {
            int[] move = new int[] {row, columnScan};
            if (shouldBreakMoves(moves, move, currPiece))
            {
                if (board[move[0]][move[1]].type == PieceType.KING)
                {
                    if (move[1] > 0) captureMap[move[0]][move[1] - 1].pieces.add(currPiece);
                }
                break;
            }
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

    private List<int[]> getUnvalidatedKnightMoves(int row, int column)
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

        // Add castling
        if (column != 4) return moves;
        Piece queenRook = board[row][column - 4];
        Piece kingRook = board[row][column + 3];
        if (currPiece.moveCount == 0 && queenRook.type == PieceType.ROOK && queenRook.moveCount == 0) moves.add(new int[] {row, column - 2});
        if (currPiece.moveCount == 0 && kingRook.type == PieceType.ROOK && kingRook.moveCount == 0) moves.add(new int[] {row, column + 2});
        return moves;
    }

    private List<int[]> getValidatedKingMoves(Piece currPiece, List<int[]> unvalidatedMoves, int kingRow, int kingColumn)
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

        // Remove invalid castling moves.
        unvalidatedMoves.removeIf(move -> {
            if (move[1] == kingColumn + 2 && !new ChessList(unvalidatedMoves).contains(new int[] {kingRow, kingColumn + 1})) return true;
            return (move[1] == kingColumn - 2 && (!new ChessList(unvalidatedMoves).contains(new int[]{kingRow, kingColumn - 1}) || board[move[0]][kingColumn - 3].type != PieceType.NONE));
        });

        return unvalidatedMoves;
    }

    private void removeRemainingIllegalMoves(int kingRow, int kingColumn)
    {
        // Send out rays in each direction to see if a piece is pinned. If so remove all possible moves not in the
        // direction of the ray.
        Piece king = board[kingRow][kingColumn];

        int rowScan;
        int columnScan;

        boolean possiblePin;
        int[] possiblePinMove;
        Piece possiblePinPiece;

        // Right
        columnScan = kingColumn + 1;
        possiblePin = false;
        possiblePinPiece = null;
        possiblePinMove = null;
        while (columnScan <= 7)
        {
            Piece currPiece = board[kingRow][columnScan];
            if (currPiece.type == PieceType.NONE) { columnScan++; continue; }
            if (currPiece.isWhite == king.isWhite)
            {
                if (possiblePin) break;
                possiblePin = true;
                possiblePinMove = new int[] {kingRow, columnScan};
                possiblePinPiece = board[kingRow][columnScan];
                columnScan++;
            }
            else
            {
                if (possiblePin && new ChessList(possibleMoves.get(currPiece)).contains(possiblePinMove)) {

                    possibleMoves.get(possiblePinPiece).removeIf(move -> move[0] != kingRow);
                }
                break;
            }
        }

        // Up Right
        columnScan = kingColumn + 1;
        rowScan = kingRow - 1;
        possiblePin = false;
        possiblePinPiece = null;
        possiblePinMove = null;
        while (columnScan <= 7 && rowScan >= 0)
        {
            Piece currPiece = board[rowScan][columnScan];
            if (currPiece.type == PieceType.NONE) { rowScan--; columnScan++; continue; }
            if (currPiece.isWhite == king.isWhite)
            {
                if (possiblePin) break;
                possiblePin = true;
                possiblePinMove = new int[] {rowScan, columnScan};
                possiblePinPiece = board[rowScan][columnScan];
                rowScan--;
                columnScan++;
            }
            else
            {
                if (possiblePin && new ChessList(possibleMoves.get(currPiece)).contains(possiblePinMove)) {

                    possibleMoves.get(possiblePinPiece).removeIf(move -> kingRow - move[0] != move[1] -  kingColumn);
                }
                break;
            }
        }

        // Up
        rowScan = kingRow - 1;
        possiblePin = false;
        possiblePinPiece = null;
        possiblePinMove = null;
        while (rowScan >= 0)
        {
            Piece currPiece = board[rowScan][kingColumn];
            if (currPiece.type == PieceType.NONE) { rowScan--; continue; }
            if (currPiece.isWhite == king.isWhite)
            {
                if (possiblePin) break;
                possiblePin = true;
                possiblePinMove = new int[] {rowScan, kingColumn};
                possiblePinPiece = board[rowScan][kingColumn];
                rowScan--;
            }
            else
            {
                if (possiblePin && new ChessList(possibleMoves.get(currPiece)).contains(possiblePinMove)) {

                    possibleMoves.get(possiblePinPiece).removeIf(move -> move[1] != kingColumn);
                }
                break;
            }
        }

        // Up Left
        columnScan = kingColumn - 1;
        rowScan = kingRow - 1;
        possiblePin = false;
        possiblePinPiece = null;
        possiblePinMove = null;
        while (columnScan >= 0 && rowScan >= 0)
        {
            Piece currPiece = board[rowScan][columnScan];
            if (currPiece.type == PieceType.NONE) { rowScan--; columnScan--; continue; }
            if (currPiece.isWhite == king.isWhite)
            {
                if (possiblePin) break;
                possiblePin = true;
                possiblePinMove = new int[] {rowScan, columnScan};
                possiblePinPiece = board[rowScan][columnScan];
                rowScan--;
                columnScan--;
            }
            else
            {
                if (possiblePin && new ChessList(possibleMoves.get(currPiece)).contains(possiblePinMove)) {

                    possibleMoves.get(possiblePinPiece).removeIf(move -> kingRow - move[0] != kingColumn - move[1]);
                }
                break;
            }
        }

        // Left
        columnScan = kingColumn - 1;
        possiblePin = false;
        possiblePinPiece = null;
        possiblePinMove = null;
        while (columnScan >= 0)
        {
            Piece currPiece = board[kingRow][columnScan];
            if (currPiece.type == PieceType.NONE) { columnScan--; continue; }
            if (currPiece.isWhite == king.isWhite)
            {
                if (possiblePin) break;
                possiblePin = true;
                possiblePinMove = new int[] {kingRow, columnScan};
                possiblePinPiece = board[kingRow][columnScan];
                columnScan--;
            }
            else
            {
                if (possiblePin && new ChessList(possibleMoves.get(currPiece)).contains(possiblePinMove)) {

                    possibleMoves.get(possiblePinPiece).removeIf(move -> move[0] != kingRow);
                }
                break;
            }
        }

        // Down Left
        columnScan = kingColumn - 1;
        rowScan = kingRow + 1;
        possiblePin = false;
        possiblePinPiece = null;
        possiblePinMove = null;
        while (columnScan >= 0 && rowScan <= 7)
        {
            Piece currPiece = board[rowScan][columnScan];
            if (currPiece.type == PieceType.NONE) { rowScan++; columnScan--; continue; }
            if (currPiece.isWhite == king.isWhite)
            {
                if (possiblePin) break;
                possiblePin = true;
                possiblePinMove = new int[] {rowScan, columnScan};
                possiblePinPiece = board[rowScan][columnScan];
                rowScan++;
                columnScan--;
            }
            else
            {
                if (possiblePin && new ChessList(possibleMoves.get(currPiece)).contains(possiblePinMove)) {

                    possibleMoves.get(possiblePinPiece).removeIf(move -> kingRow - move[0] != move[1] - kingColumn);
                }
                break;
            }
        }

        // Down
        rowScan = kingRow + 1;
        possiblePin = false;
        possiblePinPiece = null;
        possiblePinMove = null;
        while (rowScan <= 7)
        {
            Piece currPiece = board[rowScan][kingColumn];
            if (currPiece.type == PieceType.NONE) { rowScan++; continue; }
            if (currPiece.isWhite == king.isWhite)
            {
                if (possiblePin) break;
                possiblePin = true;
                possiblePinMove = new int[] {rowScan, kingColumn};
                possiblePinPiece = board[rowScan][kingColumn];
                rowScan++;
            }
            else
            {
                if (possiblePin && new ChessList(possibleMoves.get(currPiece)).contains(possiblePinMove)) {

                    possibleMoves.get(possiblePinPiece).removeIf(move -> move[1] != kingColumn);
                }
                break;
            }
        }

        // Down Right
        columnScan = kingColumn + 1;
        rowScan = kingRow + 1;
        possiblePin = false;
        possiblePinPiece = null;
        possiblePinMove = null;
        while (columnScan <= 7 && rowScan <= 7)
        {
            Piece currPiece = board[rowScan][columnScan];
            if (currPiece.type == PieceType.NONE) { rowScan++; columnScan++; continue; }
            if (currPiece.isWhite == king.isWhite)
            {
                if (possiblePin) break;
                possiblePin = true;
                possiblePinMove = new int[] {rowScan, columnScan};
                possiblePinPiece = board[rowScan][columnScan];
                rowScan++;
                columnScan++;
            }
            else
            {
                if (possiblePin && new ChessList(possibleMoves.get(currPiece)).contains(possiblePinMove)) {

                    possibleMoves.get(possiblePinPiece).removeIf(move -> kingRow - move[0] != kingColumn - move[1]);
                }
                break;
            }
        }

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

    private int numberOfKingCheckingPieces()
    {
        // Send out rays in all directions to check for checks from queens, bishops, and rooks.
        // Then manually check for knight and pawn checks.
        Piece king = isWhitesMove ? board[wKingIDXS[0]][wKingIDXS[1]] : board[bKingIDXS[0]][bKingIDXS[1]];
        int kingRow = isWhitesMove ? wKingIDXS[0] : bKingIDXS[0];
        int kingColumn = isWhitesMove ? wKingIDXS[1] : bKingIDXS[1];

        int attackerCount = 0;

        int rowScan;
        int columnScan;

        // Right
        columnScan = kingColumn + 1;
        while (columnScan <= 7)
        {
            Piece currPiece = board[kingRow][columnScan];
            if (currPiece.type == PieceType.NONE) { columnScan++; continue; }
            if (currPiece.isWhite == king.isWhite) break;
            if (currPiece.type == PieceType.QUEEN || currPiece.type == PieceType.ROOK)
            {
                attackerCount++;
                break;
            }
            break;
        }

        // Up Right
        columnScan = kingColumn + 1;
        rowScan = kingRow - 1;
        while (columnScan <= 7 && rowScan >= 0)
        {
            Piece currPiece = board[rowScan][columnScan];
            if (currPiece.type == PieceType.NONE) { rowScan--; columnScan++; continue; }
            if (currPiece.isWhite == king.isWhite) break;
            if (currPiece.type == PieceType.QUEEN || currPiece.type == PieceType.BISHOP)
            {
                attackerCount++;
                break;
            }
            break;
        }

        // Up
        rowScan = kingRow - 1;

        while (rowScan >= 0)
        {
            Piece currPiece = board[rowScan][kingColumn];
            if (currPiece.type == PieceType.NONE) { rowScan--; continue; }
            if (currPiece.isWhite == king.isWhite) break;
            if (currPiece.type == PieceType.QUEEN || currPiece.type == PieceType.ROOK)
            {
                attackerCount++;
                break;
            }
            break;
        }

        // Up Left
        columnScan = kingColumn - 1;
        rowScan = kingRow - 1;
        while (columnScan >= 0 && rowScan >= 0)
        {
            Piece currPiece = board[rowScan][columnScan];
            if (currPiece.type == PieceType.NONE) { rowScan--; columnScan--; continue; }
            if (currPiece.isWhite == king.isWhite) break;
            if (currPiece.type == PieceType.QUEEN || currPiece.type == PieceType.BISHOP)
            {
                attackerCount++;
                break;
            }
            break;
        }

        // Left
        columnScan = kingColumn - 1;
        while (columnScan >= 0)
        {
            Piece currPiece = board[kingRow][columnScan];
            if (currPiece.type == PieceType.NONE) { columnScan--; continue; }
            if (currPiece.isWhite == king.isWhite) break;
            if (currPiece.type == PieceType.QUEEN || currPiece.type == PieceType.ROOK)
            {
                attackerCount++;
                break;
            }
            break;
        }

        // Down Left
        columnScan = kingColumn - 1;
        rowScan = kingRow + 1;
        while (columnScan >= 0 && rowScan <= 7)
        {
            Piece currPiece = board[rowScan][columnScan];
            if (currPiece.type == PieceType.NONE) { rowScan++; columnScan--; continue; }
            if (currPiece.isWhite == king.isWhite) break;
            if (currPiece.type == PieceType.QUEEN || currPiece.type == PieceType.BISHOP)
            {
                attackerCount++;
                break;
            }
            break;
        }

        // Down
        rowScan = kingRow + 1;
        while (rowScan <= 7)
        {
            Piece currPiece = board[rowScan][kingColumn];
            if (currPiece.type == PieceType.NONE) { rowScan++; continue; }
            if (currPiece.isWhite == king.isWhite) break;
            if (currPiece.type == PieceType.QUEEN || currPiece.type == PieceType.ROOK)
            {
                attackerCount++;
                break;
            }
            break;
        }

        // Down Right
        columnScan = kingColumn + 1;
        rowScan = kingRow + 1;
        while (columnScan <= 7 && rowScan <= 7)
        {
            Piece currPiece = board[rowScan][columnScan];
            if (currPiece.type == PieceType.NONE) { rowScan++; columnScan++; continue; }
            if (currPiece.isWhite == king.isWhite) break;
            if (currPiece.type == PieceType.QUEEN || currPiece.type == PieceType.BISHOP)
            {
                attackerCount++;
                break;
            }
            break;
        }

        // Check for pawn checks
        Piece leftPawn;
        Piece rightPawn;
        if (isWhitesMove)
        {
            leftPawn = board[kingRow - 1][kingColumn - 1];
            rightPawn = board[kingRow - 1][kingColumn + 1];
        }
        else
        {
            leftPawn = board[kingRow + 1][kingColumn - 1];
            rightPawn = board[kingRow + 1][kingColumn + 1];
        }
        if (leftPawn.type == PieceType.PAWN && leftPawn.isWhite != isWhitesMove) attackerCount++;
        if (rightPawn.type == PieceType.PAWN && rightPawn.isWhite != isWhitesMove) attackerCount++;

        // Check for knight checks.
        List<int[]> knightMoves = getUnvalidatedKnightMoves(kingRow, kingColumn);

        // Get moves.
        knightMoves.removeIf(move -> {
            if (move[0] < 0 || move[0] >= 8 || move[1] < 0 || move[1] >= 8) return true;
            Piece comparingPiece = board[move[0]][move[1]];
            if (comparingPiece.type == PieceType.KNIGHT)
            {
                return king.isWhite == comparingPiece.isWhite;
            }
            return true;
        });

        attackerCount += knightMoves.size();

        return attackerCount;
    }
}
