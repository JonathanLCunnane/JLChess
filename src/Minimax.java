import java.util.*;

public class Minimax {
    Map<Long, Double> hashTable;
    ZobristHashing hashGen;
    final int DEPTH;
    Minimax(int depth)
    {
        DEPTH = depth;
        hashTable = new HashMap<>();
        hashGen = new ZobristHashing();
    }
    public Integer[][] getBestMove(Board board)
    {
        Map.Entry<Integer[][], Double> minimaxOut = minimax(board.copy(), DEPTH, -Float.MAX_VALUE, Float.MAX_VALUE, false, null, 0);
        Integer[][] bestMove = minimaxOut.getKey();
        Double eval = minimaxOut.getValue();
        System.out.printf("from [%d, %d] to [%d, %d], EVAL: %f\n", bestMove[0][0], bestMove[0][1], bestMove[1][0], bestMove[1][1], eval);
        return bestMove;
    }
    private Map.Entry<Integer[][], Double> minimax(Board board, int depth, double alpha, double beta, boolean maximisingPlayer, Long initialHash, int addedDepth)
    {
        long hash;
        if (initialHash == null)
        {
            hash = hashGen.hash(board);
        }
        else hash = initialHash;
        Double minimaxHashEval = hashTable.get(hash);
        List<Integer[][]> moves = new ArrayList<>();
        if (!(board.draw || board.stalemate)) moves = board.get_moves();
        if (depth == 0 || board.checkMate || board.draw || board.stalemate)
        {
            if (minimaxHashEval != null) return new AbstractMap.SimpleEntry<>(null, minimaxHashEval);
            double minimaxEval = basicBoardEval(board);
            hashTable.put(board.hash, minimaxEval);
            return new AbstractMap.SimpleEntry<>(null, minimaxEval);
        }

        if (moves.size() == 0)
        {
            System.out.println(board);
            return new AbstractMap.SimpleEntry<>(null, 0D);
        }
        Integer[][] bestMove = moves.get(0);

        if (maximisingPlayer)
        {
            double maxEval = -Float.MAX_VALUE;
            for (Integer[][] move: moves)
            {
                Board prevBoard = board.copy();

                // We do not need to generate all the next moves for leaves.
                board.tryMove(move[0], move[1], false, true);
                Double childEval;
                if (board.lastMoveWasCapture && addedDepth <= DEPTH) // Evaluate more moves after piece get traded.
                {
                    childEval = minimax(board, depth, alpha, beta, false, board.hash, addedDepth + 1).getValue();
                }
                else
                {
                    childEval = minimax(board, depth - 1, alpha, beta, false, board.hash, addedDepth).getValue();
                }

                board = prevBoard;

                if (childEval > maxEval)
                {
                    maxEval = childEval;
                    bestMove = move;
                }

                if (maxEval >= beta) break;

                alpha = Math.max(alpha, maxEval);
            }
            return new AbstractMap.SimpleEntry<>(bestMove, maxEval);
        }
        else
        {
            double minEval = Float.MAX_VALUE;
            for (Integer[][] move: moves)
            {
                Board prevBoard = board.copy();

                // We do not need to generate all the next moves for leaves.
                board.tryMove(move[0], move[1], false, true);
                Double childEval;
                if (board.lastMoveWasCapture && addedDepth <= DEPTH) // Evaluate more moves after a piece gets traded.
                {
                    childEval = minimax(board, depth, alpha, beta, true, board.hash, addedDepth + 1).getValue();
                }
                else
                {
                    childEval = minimax(board, depth - 1, alpha, beta, true, board.hash, addedDepth).getValue();
                }

                board = prevBoard;

                if (childEval < minEval)
                {
                    minEval = childEval;
                    bestMove = move;
                }

                if (minEval <= alpha) break;

                beta = Math.min(beta, minEval);
            }
            return new AbstractMap.SimpleEntry<>(bestMove, minEval);
        }
    }

    public static double basicBoardEval(Board board)
    {
        if (board.checkMate)
        {
             if (board.isWhitesMove) return -Double.MAX_VALUE;
             else return Double.MAX_VALUE;
        }
        if (board.draw || board.stalemate)
        {
            return 0;
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
