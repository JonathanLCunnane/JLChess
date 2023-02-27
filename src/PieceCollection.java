import java.util.ArrayList;
import java.util.List;

public class PieceCollection {
    List<Piece> pieces;
    PieceCollection(List<Piece> startPieces)
    {
        pieces = startPieces;
    }

    PieceCollection()
    {
        pieces = new ArrayList<>();
    }
}
