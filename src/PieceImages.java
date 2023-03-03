import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PieceImages {
    static Map<Character, BufferedImage> whiteImages = new HashMap<>()

    {{
        put('P', ImageGetter.tryGetImage("/img/white_P.png", getClass()));
        put('N', ImageGetter.tryGetImage("/img/white_N.png", getClass()));
        put('B', ImageGetter.tryGetImage("/img/white_B.png", getClass()));
        put('R', ImageGetter.tryGetImage("/img/white_R.png", getClass()));
        put('Q', ImageGetter.tryGetImage("/img/white_Q.png", getClass()));
        put('K', ImageGetter.tryGetImage("/img/white_K.png", getClass()));
    }};
    static Map<Character, BufferedImage> blackImages = new HashMap<>()

    {{
        put('P', ImageGetter.tryGetImage("/img/black_P.png", getClass()));
        put('N', ImageGetter.tryGetImage("/img/black_N.png", getClass()));
        put('B', ImageGetter.tryGetImage("/img/black_B.png", getClass()));
        put('R', ImageGetter.tryGetImage("/img/black_R.png", getClass()));
        put('Q', ImageGetter.tryGetImage("/img/black_Q.png", getClass()));
        put('K', ImageGetter.tryGetImage("/img/black_K.png", getClass()));
    }};
    public static BufferedImage getImage(char pieceChar, boolean isWhite)
    {
        if (isWhite) return whiteImages.get(pieceChar);
        return blackImages.get(pieceChar);
    }
}
