import java.util.*;

public class Minimax {
    private Map.Entry<Integer[], Integer[]> bestMove;
    public Map.Entry<Integer[], Integer[]> getBestMove(Board board, int depth)
    {
        System.out.println(minimax(board.copy(), depth, -Float.MAX_VALUE, Float.MAX_VALUE, false));
        System.out.printf("from [%d, %d] to [%d, %d]", bestMove.getKey()[0], bestMove.getKey()[1], bestMove.getValue()[0], bestMove.getValue()[1]);
        return bestMove;
    }
    private double minimax(Board board, int depth, double alpha, double beta, boolean maximisingPlayer)
    {
        if (depth == 0 || board.checkMate)
        {
            return basicBoardEval(board);
        }
        if (maximisingPlayer)
        {
            double maxEval = -Float.MAX_VALUE;
            for (int row = 0; row < board.board.length; row++)
            {
                for (int column = 0; column < board.board[row].length; column++)
                {
                    Piece movingPiece = board.board[row][column];
                    if (movingPiece.type == PieceType.NONE) continue;
                    if (movingPiece.isWhite) continue;
                    Integer[] from = new Integer[] {row, column};
                    for (int[] to: board.possibleMoves.get(movingPiece))
                    {
                        Integer[] nTo = new Integer[] {to[0], to[1]};
                        Board prevBoard = board.copy();
                        board.tryMove(from, nTo, false);

                        double childEval = minimax(board, depth - 1, alpha, beta, false);

                        board = prevBoard;
                        if (childEval > maxEval)
                        {
                            maxEval = childEval;
                            bestMove = new AbstractMap.SimpleEntry<>(from, nTo);
                        }
                        alpha = Math.max(alpha, childEval);
                        if (alpha >= beta)
                        {
                            return maxEval;
                        }
                    }
                }
            }
            return maxEval;
        }
        else
        {
            double minEval = Float.MAX_VALUE;
            for (int row = 0; row < board.board.length; row++)
            {
                for (int column = 0; column < board.board[row].length; column++)
                {
                    Piece movingPiece = board.board[row][column];
                    if (movingPiece.type == PieceType.NONE) continue;
                    if (!movingPiece.isWhite) continue;
                    Integer[] from = new Integer[] {row, column};
                    for (int[] to: board.possibleMoves.get(movingPiece))
                    {
                        Integer[] nTo = new Integer[] {to[0], to[1]};
                        Board prevBoard = board.copy();
                        board.tryMove(from, nTo, false);

                        double childEval = minimax(board, depth - 1, alpha, beta, true);

                        board = prevBoard;
                        if (childEval < minEval)
                        {
                            minEval = childEval;
                            bestMove = new AbstractMap.SimpleEntry<>(from, nTo);
                        }
                        beta = Math.min(beta, childEval);
                        if (beta <= alpha)
                        {
                            return minEval;
                        }
                    }
                }
            }
            return minEval;
        }
    }

    public static double basicBoardEval(Board board)
    {
        if (board.checkMate)
        {
             if (board.isWhitesMove) return Double.MIN_VALUE;
             else return Double.MAX_VALUE;
        }
        double eval = 0;
        double multiplier;
        for(Piece[] row: board.board)
        {
            for(Piece piece: row)
            {
                if (piece.isWhite) multiplier = 1D;
                else multiplier = -1D;
                switch (piece.type)
                {
                    case PieceType.PAWN -> eval += multiplier;
                    case PieceType.KNIGHT -> eval += 3.05 * multiplier;
                    case PieceType.BISHOP -> eval += 3.33 * multiplier;
                    case PieceType.ROOK -> eval += 5.63 * multiplier;
                    case PieceType.QUEEN -> eval += 9.5 * multiplier;
                }
            }
        }
        return eval;
    }
}
