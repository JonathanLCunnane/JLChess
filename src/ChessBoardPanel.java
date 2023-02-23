import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ChessBoardPanel extends JPanel {
    int boardSize = 512;
    int marginSize = 16;

    BufferedImage boardImage = tryGetImage("/board.png");

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.DARK_GRAY);

        int extraTopMargin = (getHeight() - boardSize - (marginSize * 2))/2;
        int extraSideMargin = (getWidth() - boardSize - (marginSize * 2))/2;
        extraTopMargin = Math.max(extraTopMargin, 0);
        extraSideMargin = Math.max(extraSideMargin, 0);

        g.drawImage(boardImage, marginSize + extraSideMargin, marginSize + extraTopMargin, null);
    }

    private BufferedImage tryGetImage(String path)
    {
        BufferedImage img = null;
        try
        {
            URL url = getClass().getResource(path);
            if (url == null) { throw new NullPointerException("Resource is null."); }
            img = ImageIO.read(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return img;
    }
}
