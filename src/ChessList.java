import java.util.List;

public class ChessList {
    List<int[]> list;
    ChessList(List<int[]> movesList)
    {
        list = movesList;
    }

    boolean contains(int[] coordinate)
    {
        if (list == null) return false;
        for (int[] move: list)
        {
            if (coordinate[0] == move[0] && coordinate[1] == move[1]) return true;
        }
        return false;
    }
}
