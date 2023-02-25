import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageGetter {
    public static BufferedImage tryGetImage(String path, Class<?> callingClass)
    {
        BufferedImage img = null;
        try
        {
            URL url = callingClass.getResource(path);
            if (url == null) { throw new NullPointerException(String.format("Resource '%s' is null.", path)); }
            img = ImageIO.read(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return img;
    }
}
