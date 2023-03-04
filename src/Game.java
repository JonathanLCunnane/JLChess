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
        return board.tryMove(from, to, true);
    }

    public void doBlacksBestMove()
    {
        if (board.isWhitesMove) return;
        Integer[][] bestMove = minimax.getBestMove(board, 3);
        boolean move = board.tryMove(bestMove[0], bestMove[1], true);
    }
}
