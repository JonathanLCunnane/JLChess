import java.util.*;

public class Minimax {
    private Map.Entry<Piece, int[]> bestMove;
    private Map.Entry<Piece, int[]> getBestMove(Board board)
    {
        minimax(board, 3, Float.MIN_VALUE, Float.MAX_VALUE, false);
        return bestMove;
    }
    private float minimax(Board board, int depth, float alpha, float beta, boolean maximisingPlayer)
    {
        if (depth == 0 || board.checkMate)
        {
            return basicBoardEval(board);
        }
        if (maximisingPlayer)
        {
            float maxEval = Float.MIN_VALUE;
            boolean isBreaking = false;
            for (int row = 0; row < board.board.length; row++)
            {
                if (isBreaking) break;
                for (int column = 0; column < board.board[row].length; column++)
                {
                    if (isBreaking) break;

                    Piece movingPiece = board.board[row][column];
                    if (movingPiece.type == PieceType.NONE) continue;
                    Integer[] from = new Integer[] {row, column};
                    for (int[] to: board.possibleMoves.get(movingPiece))
                    {
                        Integer[] nTo = new Integer[] {to[0], to[1]};
                        Board prevBoard = board.copy();
                        board.tryMove(from, nTo);

                        float childEval = minimax(board, depth - 1, alpha, beta, false);

                        board = prevBoard;
                        if (childEval > maxEval)
                        {
                            maxEval = childEval;
                            bestMove = new AbstractMap.SimpleEntry<>(movingPiece, to);

                            alpha = Math.max(alpha, maxEval);
                            if (alpha >= beta)
                            {
                                isBreaking = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            float minEval = Float.MAX_VALUE;
            boolean isBreaking = false;
            for (int row = 0; row < board.board.length; row++)
            {
                if (isBreaking) break;
                for (int column = 0; column < board.board[row].length; column++)
                {
                    if (isBreaking) break;

                    Piece movingPiece = board.board[row][column];
                    if (movingPiece.type == PieceType.NONE) continue;
                    Integer[] from = new Integer[] {row, column};
                    for (int[] to: board.possibleMoves.get(movingPiece))
                    {
                        Integer[] nTo = new Integer[] {to[0], to[1]};
                        Board prevBoard = board.copy();
                        board.tryMove(from, nTo);

                        float childEval = minimax(board, depth - 1, alpha, beta, true);

                        board = prevBoard;
                        if (childEval < minEval)
                        {
                            minEval = childEval;
                            bestMove = new AbstractMap.SimpleEntry<>(movingPiece, to);

                            beta = Math.max(beta, minEval);
                            if (beta <= alpha)
                            {
                                isBreaking = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static float basicBoardEval(Board board)
    {
        if (board.checkMate)
        {
             if (board.isWhitesMove) return Float.MAX_VALUE;
             else return Float.MIN_VALUE;
        }
        float eval = 0;
        float multiplier;
        for(Piece[] row: board.board)
        {
            for(Piece piece: row)
            {
                if (piece.isWhite) multiplier = 1F;
                else multiplier = -1F;
                switch (piece.type)
                {

                    case PieceType.PAWN -> eval += multiplier;
                    case PieceType.KNIGHT -> eval += 3.05F * multiplier;
                    case PieceType.BISHOP -> eval += 3.33F * multiplier;
                    case PieceType.ROOK -> eval += 5.63F * multiplier;
                    case PieceType.QUEEN -> eval += 9.5F * multiplier;
                }
            }
        }
        return eval;
    }
}
