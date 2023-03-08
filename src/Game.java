public class Game {
    Board board;
    Minimax minimax = new Minimax();
    boolean versusAI;
    public Game(boolean isVersusAI, Board gameBoard)
    {
        versusAI = isVersusAI;
        board = gameBoard;
    }

    public boolean tryMove(Integer[] from, Integer[] to)
    {
        if (from[0] == null || from[1] == null || to[0] == null || to[1] == null) return false;
        if (versusAI && !board.board[from[0]][from[1]].isWhite) return false;
        return board.tryMove(from, to, true, true);
    }

    public Integer[][] doBlacksBestMove(int msForMove)
    {
        if (board.isWhitesMove) return null;

        int depth = 1;
        long start = System.nanoTime();
        long elapsedMS;
        Integer[][] bestMove;
        do
        {
            depth++;
            bestMove = minimax.getBestMove(board, depth);
            elapsedMS = (System.nanoTime() - start) / 1000000;
        } while (elapsedMS <= msForMove);
        elapsedMS = (System.nanoTime() - start) / 1000000;
        System.out.printf("AI's move took %dms for a total depth of %d\n", elapsedMS, depth);

        boolean move = board.tryMove(bestMove[0], bestMove[1], true, true);
        if (move) return bestMove;
        return null;
    }
}
