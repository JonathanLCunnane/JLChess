import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ChessBoardPanel extends JPanel {
    private final int boardSize = 512;
    private final int marginSize = 16;
    private Integer[] clickIndicatorLocation = {null, null};

    ChessBoardPanel()
    {
        super();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

                int extraTopMargin = (getHeight() - boardSize)/2;
                int extraSideMargin = (getWidth() - boardSize)/2;
                int xBoard = mouseEvent.getX() - extraSideMargin;
                int yBoard = mouseEvent.getY() - extraTopMargin;

                int xIdx = xBoard / 64;
                int yIdx = yBoard / 64;

                if ((xIdx >= 0 && xIdx < 8) && (yIdx >= 0 && yIdx < 8))
                {
                    clickIndicatorLocation[0] = xIdx;
                    clickIndicatorLocation[1] = yIdx;
                }
                else
                {
                    clickIndicatorLocation[0] = null;
                    clickIndicatorLocation[1] = null;
                }
                paintComponent(getGraphics());
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    BufferedImage boardImage = tryGetImage("/board.png");

    @Override
    protected void paintComponent(Graphics g) {
        // Draw board
        g.setColor(Color.DARK_GRAY);

        int extraTopMargin = (getHeight() - boardSize - (marginSize * 2))/2;
        int extraSideMargin = (getWidth() - boardSize - (marginSize * 2))/2;
        extraTopMargin = Math.max(extraTopMargin, 0);
        extraSideMargin = Math.max(extraSideMargin, 0);

        g.drawImage(boardImage, marginSize + extraSideMargin, marginSize + extraTopMargin, null);

        // Draw click indicator
        if (clickIndicatorLocation[0] != null && clickIndicatorLocation[1] != null)
        {
            g.setColor(Color.BLUE);
            g.fillRect(
                    marginSize + extraSideMargin + 24 + (clickIndicatorLocation[0] * 64),
                    marginSize + extraTopMargin + 24 + (clickIndicatorLocation[1] * 64),
                    16,
                    16
            );
        }
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
