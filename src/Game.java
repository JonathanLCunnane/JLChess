public class Game {
    Board board;
    Minimax minimax = new Minimax(4);
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

    public Integer[][] doBlacksBestMove()
    {
        if (board.isWhitesMove) return null;

        long start = System.nanoTime();
        Integer[][] bestMove = minimax.getBestMove(board);
        long end = System.nanoTime();
        float ms = (end - start)/1000000F;
        System.out.printf("AI's move took %fms\n", ms);

        boolean move = board.tryMove(bestMove[0], bestMove[1], true, true);
        if (move) return bestMove;
        return null;
    }
}
