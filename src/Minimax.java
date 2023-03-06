import java.util.*;

public class Minimax {
    Map<Long, Double> hashTable;
    ZobristHashing hashGen;
    Minimax()
    {
        hashTable = new HashMap<>();
        hashGen = new ZobristHashing();
    }
    public Integer[][] getBestMove(Board board, int depth)
    {
        Map.Entry<Integer[][], Double> minimaxOut = minimax(board.copy(), depth, -Float.MAX_VALUE, Float.MAX_VALUE, false, null);
        Integer[][] bestMove = minimaxOut.getKey();
        Double eval = minimaxOut.getValue();
        System.out.printf("from [%d, %d] to [%d, %d], EVAL: %f\n", bestMove[0][0], bestMove[0][1], bestMove[1][0], bestMove[1][1], eval);
        return bestMove;
    }
    private Map.Entry<Integer[][], Double> minimax(Board board, int depth, double alpha, double beta, boolean maximisingPlayer, Long initialHash)
    {
        long hash;
        if (initialHash == null)
        {
            hash = hashGen.hash(board);
        }
        else hash = initialHash;
        Double minimaxHashEval = hashTable.get(hash);
        if (depth == 0 || board.checkMate)
        {
            if (minimaxHashEval != null) return new AbstractMap.SimpleEntry<>(null, minimaxHashEval);
            double minimaxEval = basicBoardEval(board);
            hashTable.put(board.hash, minimaxEval);
            return new AbstractMap.SimpleEntry<>(null, minimaxEval);
        }

        List<Integer[][]> moves = board.get_moves();
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

                board.tryMove(move[0], move[1], false);

                Double childEval = minimax(board, depth - 1, alpha, beta, false, board.hash).getValue();

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

                board.tryMove(move[0], move[1], false);

                Double childEval = minimax(board, depth - 1, alpha, beta, true, board.hash).getValue();

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
